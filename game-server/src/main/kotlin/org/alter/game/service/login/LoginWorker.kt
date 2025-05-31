package org.alter.game.service.login

import org.alter.game.model.entity.Client
import org.alter.game.service.GameService
import org.alter.game.service.serializer.PlayerLoadResult
import org.alter.game.service.world.WorldVerificationService
import gg.rsmod.net.codec.login.LoginResponse
import gg.rsmod.net.codec.login.LoginResultType
import gg.rsmod.util.io.IsaacRandom
import io.netty.channel.ChannelFutureListener
import io.github.oshai.kotlinlogging.KotlinLogging
import java.net.InetSocketAddress
import org.alter.plugins.content.IpBanService

/**
 * A worker voor de [LoginService] dat inkomende
 * [LoginServiceRequest]s afhandelt.
 */
class LoginWorker(
    private val boss: LoginService,
    private val verificationService: WorldVerificationService
) : Runnable {

    override fun run() {
        while (true) {
            val request = boss.requests.take()
            try {
                // --- IP-ban check vóór alle overige login logic ---
                val remoteAddress = request.login.channel.remoteAddress()
                if (remoteAddress is InetSocketAddress) {
                    val ip = remoteAddress.address.hostAddress
                    if (IpBanService.isBanned(ip)) {
                        request.login.channel
                            .writeAndFlush(LoginResultType.IP_BAN)
                            .addListener(ChannelFutureListener.CLOSE)
                        logger.info("Login attempt from banned IP '{}' denied.", ip)
                        continue
                    }
                }

                val world = request.world
                val client = Client.fromRequest(world, request.login)

                // --- Username sanitization én hoofdletter eerste letter ---
                // Verwijder spaties uit de username
                val withoutSpaces = client.username.replace(" ", "")
                // Maak de eerste letter een hoofdletter (als de string niet leeg is)
                val sanitizedUsername = if (withoutSpaces.isNotEmpty()) {
                    withoutSpaces.replaceFirstChar { it.uppercaseChar() }
                } else {
                    withoutSpaces
                }
                client.username = sanitizedUsername
                logger.debug { "Sanitized username, removed spaces and capitalized first letter: '${client.username}'" }

                // --- Username character validation ---
                // Alleen ASCII letters, cijfers, '-' en '_' zijn toegestaan (spaties zijn verwijderd)
                val usernamePattern = Regex("^[A-Za-z0-9_-]+$")
                if (!usernamePattern.matches(client.username)) {
                    request.login.channel
                        .writeAndFlush(LoginResultType.INVALID_CREDENTIALS)
                        .addListener(ChannelFutureListener.CLOSE)
                    logger.info("User '{}' login denied: invalid characters in username.", client.username)
                    continue
                }

                val loadResult: PlayerLoadResult = boss.serializer.loadClientData(client, request.login)

                if (loadResult == PlayerLoadResult.LOAD_ACCOUNT || loadResult == PlayerLoadResult.NEW_ACCOUNT) {
                    val decodeRandom = IsaacRandom(request.login.xteaKeys)
                    val encodeRandom = IsaacRandom(IntArray(request.login.xteaKeys.size) { request.login.xteaKeys[it] + 50 })

                    world.getService(GameService::class.java)?.submitGameThreadJob {
                        // Early-exit voor gebande accounts
                        if (client.privilege.id == -1) {
                            request.login.channel
                                .writeAndFlush(LoginResultType.ACCOUNT_BANNED)
                                .addListener(ChannelFutureListener.CLOSE)
                            logger.info("User '{}' login denied: banned account.", client.username)
                            return@submitGameThreadJob
                        }

                        // Intercept of register flow
                        val interceptedLoginResult = verificationService.interceptLoginResult(
                            world,
                            client.uid,
                            client.username,
                            client.loginUsername
                        )
                        val loginResult: LoginResultType = interceptedLoginResult
                            ?: if (client.register()) {
                                LoginResultType.ACCEPTABLE
                            } else {
                                LoginResultType.COULD_NOT_COMPLETE_LOGIN
                            }

                        if (loginResult == LoginResultType.ACCEPTABLE) {
                            // 1) Stuur accept en registreer in wereld
                            client.channel.write(LoginResponse(index = client.index, privilege = client.privilege.id))
                            boss.successfulLogin(client, world, encodeRandom, decodeRandom)

                            // 2) Haal het IP uit de oorspronkelijke login-channel
                            val remote = request.login.channel.remoteAddress()
                            val ip = (remote as? InetSocketAddress)?.address?.hostAddress
                            if (ip != null) {
                                // 3) Sla username→IP op in IpBanService
                                IpBanService.recordLogin(client.username, ip)
                                logger.debug { "Recorded login IP '$ip' for user '${client.username}'" }
                            }
                        } else {
                            request.login.channel.writeAndFlush(loginResult)
                                .addListener(ChannelFutureListener.CLOSE)
                            logger.info("User '{}' login denied with code {}.", client.username, loginResult)
                        }
                    }
                } else {
                    val errorCode = when (loadResult) {
                        PlayerLoadResult.INVALID_CREDENTIALS     -> LoginResultType.INVALID_CREDENTIALS
                        PlayerLoadResult.INVALID_RECONNECTION    -> LoginResultType.BAD_SESSION_ID
                        PlayerLoadResult.MALFORMED               -> LoginResultType.ACCOUNT_LOCKED
                        else                                     -> LoginResultType.COULD_NOT_COMPLETE_LOGIN
                    }
                    request.login.channel.writeAndFlush(errorCode)
                        .addListener(ChannelFutureListener.CLOSE)
                    logger.info(
                        "User '{}' login denied with code {} and channel {}.",
                        client.username,
                        loadResult,
                        client.channel
                    )
                }
            } catch (e: Exception) {
                logger.error("Error when handling request from ${request.login.channel}.", e)
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger{}
    }
}
