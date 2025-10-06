package gg.rsmod.net.codec.login

import gg.rsmod.net.codec.StatefulFrameDecoder
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import java.math.BigInteger
import net.rsprot.buffer.extensions.toJagByteBuf
import net.rsprot.protocol.common.client.OldSchoolClientType
import net.rsprot.protocol.common.loginprot.incoming.codec.GameLoginDecoder
import net.rsprot.protocol.common.loginprot.incoming.codec.GameReconnectDecoder
import net.rsprot.protocol.common.loginprot.incoming.codec.shared.exceptions.InvalidVersionException
import net.rsprot.protocol.loginprot.incoming.util.AuthenticationType
import net.rsprot.protocol.loginprot.incoming.util.LoginBlock
import net.rsprot.protocol.loginprot.incoming.util.OtpAuthenticationType

/**
 * @author Tom <rspsmods@gmail.com>
 */
class LoginDecoder(
    private val serverRevision: Int,
    private val cacheCrcs: IntArray,
    private val serverSeed: Long,
    rsaExponent: BigInteger?,
    rsaModulus: BigInteger?,
) : StatefulFrameDecoder<LoginDecoderState>(LoginDecoderState.HANDSHAKE) {

    private val loginDecoder: GameLoginDecoder
    private val reconnectDecoder: GameReconnectDecoder

    private var payloadLength = -1
    private var reconnecting = false

    init {
        val exponent = requireNotNull(rsaExponent) { "RSA exponent must be configured when using RSProt." }
        val modulus = requireNotNull(rsaModulus) { "RSA modulus must be configured when using RSProt." }
        loginDecoder = GameLoginDecoder(SUPPORTED_CLIENT_TYPES, exponent, modulus)
        reconnectDecoder = GameReconnectDecoder(SUPPORTED_CLIENT_TYPES, exponent, modulus)
    }

    override fun decode(
        ctx: ChannelHandlerContext,
        buf: ByteBuf,
        out: MutableList<Any>,
        state: LoginDecoderState,
    ) {
        buf.markReaderIndex()
        when (state) {
            LoginDecoderState.HANDSHAKE -> decodeHandshake(ctx, buf)
            LoginDecoderState.HEADER -> decodeHeader(ctx, buf, out)
        }
    }

    private fun decodeHandshake(ctx: ChannelHandlerContext, buf: ByteBuf) {
        if (!buf.isReadable) {
            return
        }
        val opcode = buf.readUnsignedByte().toInt()
        if (opcode == LOGIN_OPCODE || opcode == RECONNECT_OPCODE) {
            reconnecting = opcode == RECONNECT_OPCODE
            setState(LoginDecoderState.HEADER)
        } else {
            ctx.writeResponse(LoginResultType.BAD_SESSION_ID)
        }
    }

    private fun ChannelHandlerContext.writeResponse(result: LoginResultType) {
        val response = channel().alloc().buffer(1)
        response.writeByte(result.id)
        writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    private fun decodeHeader(
        ctx: ChannelHandlerContext,
        buf: ByteBuf,
        out: MutableList<Any>,
    ) {
        if (buf.readableBytes() < HEADER_LENGTH) {
            buf.resetReaderIndex()
            return
        }
        val size = buf.readUnsignedShort()
        if (buf.readableBytes() < size) {
            buf.resetReaderIndex()
            return
        }

        val revision = buf.readInt()
        buf.readInt() // always 1
        buf.readUnsignedByte() // client type
        buf.readUnsignedByte() // param4

        if (revision != serverRevision) {
            ctx.writeResponse(LoginResultType.REVISION_MISMATCH)
            buf.skipBytes(size - HEADER_METADATA_LENGTH)
            return
        }

        payloadLength = size - HEADER_METADATA_LENGTH
        if (payloadLength <= 0) {
            ctx.writeResponse(LoginResultType.MALFORMED_PACKET)
            return
        }
        decodePayload(ctx, buf, out)
    }

    private fun decodePayload(
        ctx: ChannelHandlerContext,
        buf: ByteBuf,
        out: MutableList<Any>,
    ) {
        if (!buf.isReadable(payloadLength)) {
            buf.resetReaderIndex()
            return
        }

        val payload = buf.readSlice(payloadLength)
        try {
            if (reconnecting) {
                val reconnectMessage = reconnectDecoder.decode(payload.toJagByteBuf())
                val block = reconnectMessage.decoder.apply(reconnectMessage.buffer, false)
                handleDecodedBlock(ctx, out, block, password = "", authCode = -1, reconnecting = true)
            } else {
                val loginMessage = loginDecoder.decode(payload.toJagByteBuf())
                val block = loginMessage.decoder.apply(loginMessage.buffer, false)
                val authentication = block.authentication
                val (password, authCode) = extractCredentials(authentication)
                authentication.clear()
                handleDecodedBlock(ctx, out, block, password, authCode, reconnecting = false)
            }
        } catch (e: InvalidVersionException) {
            ctx.writeResponse(LoginResultType.REVISION_MISMATCH)
        } catch (t: Throwable) {
            logger.error(t) { "Failed to decode login request from channel ${ctx.channel()}." }
            ctx.writeResponse(LoginResultType.MALFORMED_PACKET)
        }
    }

    private fun handleDecodedBlock(
        ctx: ChannelHandlerContext,
        out: MutableList<Any>,
        block: LoginBlock<*>,
        password: String,
        authCode: Int,
        reconnecting: Boolean,
    ) {
        if (block.sessionId != serverSeed) {
            logger.info { "User '${block.username}' login request seed mismatch [receivedSeed=${block.sessionId}, expectedSeed=$serverSeed]." }
            ctx.writeResponse(LoginResultType.BAD_SESSION_ID)
            return
        }

        if (!block.crc.validate(cacheCrcs)) {
            logger.info { "User '${block.username}' login request CRC mismatch." }
            ctx.writeResponse(LoginResultType.REVISION_MISMATCH)
            return
        }

        val request =
            LoginRequest(
                channel = ctx.channel(),
                username = block.username,
                password = password,
                revision = serverRevision,
                xteaKeys = block.seed,
                resizableClient = block.resizable,
                auth = authCode,
                uuid = block.uuid.toHexString(),
                clientWidth = block.width,
                clientHeight = block.height,
                reconnecting = reconnecting,
            )
        logger.info { "User '${block.username}' login request from ${ctx.channel()}." }
        out += request
    }

    private fun extractCredentials(authentication: AuthenticationType): Pair<String, Int> =
        when (authentication) {
            is AuthenticationType.PasswordAuthentication ->
                authentication.password.asString() to authentication.otpAuthentication.extractOtp()

            is AuthenticationType.TokenAuthentication ->
                authentication.token.asString() to authentication.otpAuthentication.extractOtp()
        }

    private fun OtpAuthenticationType.extractOtp(): Int =
        when (this) {
            is OtpAuthenticationType.OtpAuthentication -> otp
            is OtpAuthenticationType.TrustedComputer -> identifier
            OtpAuthenticationType.NoMultiFactorAuthentication -> -1
            is OtpAuthenticationType.UntrustedAuthentication -> otp
            is OtpAuthenticationType.TrustedAuthenticator -> otp
        }

    private fun ByteArray.toHexString(): String =
        joinToString(separator = "") { byte -> "%02x".format(byte.toInt() and 0xFF) }

    companion object {
        private val logger = KotlinLogging.logger {}

        private const val LOGIN_OPCODE = 16
        private const val RECONNECT_OPCODE = 18
        private const val HEADER_LENGTH = Short.SIZE_BYTES
        private const val HEADER_METADATA_LENGTH = Int.SIZE_BYTES + Int.SIZE_BYTES + Byte.SIZE_BYTES + Byte.SIZE_BYTES
        private val SUPPORTED_CLIENT_TYPES = listOf(OldSchoolClientType.DESKTOP)
    }
}
