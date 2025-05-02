package gg.rsmod.net.codec.game

import gg.rsmod.net.codec.StatefulFrameDecoder
import gg.rsmod.net.packet.GamePacket
import gg.rsmod.net.packet.IPacketMetadata
import gg.rsmod.net.packet.PacketType
import gg.rsmod.util.io.IsaacRandom
import io.github.oshai.kotlinlogging.KotlinLogging
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext

class GamePacketDecoder(
    private val random: IsaacRandom?,
    private val packetMetadata: IPacketMetadata
) : StatefulFrameDecoder<GameDecoderState>(GameDecoderState.OPCODE) {

    private var opcode = 0
    private var length = 0
    private var type = PacketType.FIXED
    private var ignore = false

    override fun decode(
        ctx: ChannelHandlerContext,
        buf: ByteBuf,
        out: MutableList<Any>,
        state: GameDecoderState
    ) {
        when (state) {
            GameDecoderState.OPCODE  -> decodeOpcode(ctx, buf, out)
            GameDecoderState.LENGTH  -> decodeLength(buf, out)
            GameDecoderState.PAYLOAD -> decodePayload(buf, out)
        }
    }

    private fun decodeOpcode(
        ctx: ChannelHandlerContext,
        buf: ByteBuf,
        out: MutableList<Any>
    ) {
        if (!buf.isReadable) return

        // Lees opcode, decypher met ISAAC
        opcode = (buf.readUnsignedByte().toInt() - (random?.nextInt() ?: 0)) and 0xFF

        val packetType = packetMetadata.getType(opcode)
        if (packetType == null) {
            logger.warn("Channel {} sent message met ongeldige opcode: {}", ctx.channel(), opcode)
            buf.skipBytes(buf.readableBytes())
            return
        }

        type   = packetType
        ignore = packetMetadata.shouldIgnore(opcode)

        when (type) {
            PacketType.FIXED -> {
                length = packetMetadata.getLength(opcode)
                if (length > 0) {
                    setState(GameDecoderState.PAYLOAD)
                } else {
                    if (!ignore) {
                        out.add(GamePacket(opcode, type, Unpooled.EMPTY_BUFFER))
                    }
                    // state blijft OPCODE
                }
            }
            PacketType.VARIABLE_BYTE,
            PacketType.VARIABLE_SHORT -> {
                setState(GameDecoderState.LENGTH)
            }
            else -> throw IllegalStateException("Onverwacht packet type $type voor opcode $opcode")
        }
    }

    private fun decodeLength(
        buf: ByteBuf,
        out: MutableList<Any>
    ) {
        when (type) {
            PacketType.VARIABLE_SHORT -> {
                // Wacht tot je zeker bent dat er 2 bytes zijn om uit te lezen
                if (buf.readableBytes() < 2) {
                    return
                }
                length = buf.readUnsignedShort().toInt()
            }
            PacketType.VARIABLE_BYTE -> {
                if (!buf.isReadable) {
                    return
                }
                length = buf.readUnsignedByte().toInt()
            }
            else -> {
                // Zou niet moeten gebeuren
                return
            }
        }

        if (length > 0) {
            setState(GameDecoderState.PAYLOAD)
        } else {
            // nul-lengte variabele-packet
            if (!ignore) {
                out.add(GamePacket(opcode, type, Unpooled.EMPTY_BUFFER))
            }
            setState(GameDecoderState.OPCODE)
        }
    }

    private fun decodePayload(
        buf: ByteBuf,
        out: MutableList<Any>
    ) {
        if (buf.readableBytes() < length) {
            return
        }
        val payload = buf.readBytes(length)
        setState(GameDecoderState.OPCODE)

        if (!ignore) {
            out.add(GamePacket(opcode, type, payload))
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
