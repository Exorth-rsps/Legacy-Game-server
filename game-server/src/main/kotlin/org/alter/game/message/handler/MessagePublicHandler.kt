package org.alter.game.message.handler

import org.alter.game.Server.Companion.logger
import org.alter.game.message.MessageHandler
import org.alter.game.message.impl.MessagePublicMessage
import org.alter.game.model.ChatMessage
import org.alter.game.model.World
import org.alter.game.model.entity.Client
import org.alter.game.service.log.PublicChatLoggerService
import org.alter.plugins.content.IpBanService
import org.alter.game.sync.block.UpdateBlockType

class MessagePublicHandler : MessageHandler<MessagePublicMessage> {

    override fun handle(client: Client, world: World, message: MessagePublicMessage) {
        // 1) decompressie + string
        val buf = ByteArray(230)
        world.huffman.decompress(message.data, buf, message.length)
        val unpacked = String(buf, 0, message.length)

        // 2) globale config uit GameContext
        val cfg = world.gameContext
        val now = System.currentTimeMillis()

        // ===== Flood‐controle + waarschuwing/banner =================================
        val windowDuration = 30_000L // 30 seconden

        if (unpacked == client.lastPublicMessage) {
            // Zelfde bericht binnen hetzelfde venster?
            if (now - client.floodWindowStart > windowDuration) {
                // Nieuw venster starten
                client.floodMessageCount = 1
                client.floodWindowStart = now
                client.floodWarned = false
                client.floodSinceWarningCount = 0
            } else {
                // Binnen venster, teller omhoog
                client.floodMessageCount++
            }
        } else {
            // Verschillend bericht: reset trackers
            client.lastPublicMessage = unpacked
            client.lastPublicMessageTime = now
            client.floodMessageCount = 1
            client.floodWindowStart = now
            client.floodWarned = false
            client.floodSinceWarningCount = 0
        }

        if (cfg.autoBanEnabled) {
            // 5 keer hetzelfde bericht binnen 30s → waarschuwing
            if (!client.floodWarned && client.floodMessageCount >= 5) {
                // Verstuur waarschuwing via chat-block
                client.blockBuffer.publicChat = ChatMessage(
                    "Hi, im a Spammer, with seconds away form my ban!",
                    client.privilege.icon,
                    ChatMessage.ChatType.NONE,
                    ChatMessage.ChatEffect.NONE,
                    ChatMessage.ChatColor.NONE
                )
                client.addBlock(UpdateBlockType.PUBLIC_CHAT)
                client.floodWarned = true
                client.floodBanThreshold = (1..4).random()
                return
            }


            // Na waarschuwing: random tussen 5–15 verdere berichten → ban
            if (client.floodWarned && unpacked == client.lastPublicMessage) {
                client.floodSinceWarningCount++
                if (client.floodSinceWarningCount >= client.floodBanThreshold) {
                    // ==== Ban‐logica (hergebruikt uit bestaande auto‐ban) ====
                    // 1) Account‐ban
                    client.privilege = world.privileges.get(-1)!!
                    // 2) Optioneel IP‐ban
                    if (cfg.autoIPBanEnabled) {
                        IpBanService.getIpForUser(client.username)?.let { ip ->
                            IpBanService.add(ip)
                            IpBanService.load()
                            logger.info("AUTO‐IP‐BAN: $ip")
                        }
                    }
                    // 3) Kick/logout
                    client.requestLogout()
                    client.channel.close()
                    // 4) Log auto‐ban
                    val suffix = if (cfg.autoIPBanEnabled) " (account+IP banned)" else " (account banned)"
                    world.getService(PublicChatLoggerService::class.java, true)
                        ?.logPublicChat(client, "[AUTO‐BAN flood after warning] \"$unpacked\"$suffix")
                    return
                }
            }
        }
        // =============================================================================

        // 3) sla laatste bericht/tijd op
        client.lastPublicMessage     = unpacked
        client.lastPublicMessageTime = now

        // 4) normale chat‐flow
        val type = ChatMessage.ChatType.values.firstOrNull { it.id == message.type }
            ?: ChatMessage.ChatType.NONE
        val effect = ChatMessage.ChatEffect.values.firstOrNull { it.id == message.effect }
            ?: ChatMessage.ChatEffect.NONE
        val color = ChatMessage.ChatColor.values.firstOrNull { it.id == message.color }
            ?: ChatMessage.ChatColor.NONE

        client.blockBuffer.publicChat = ChatMessage(unpacked, client.privilege.icon, type, effect, color)
        client.addBlock(UpdateBlockType.PUBLIC_CHAT)

        // 5) log het bericht
        world.getService(PublicChatLoggerService::class.java, true)
            ?.logPublicChat(client, unpacked)
    }
}
