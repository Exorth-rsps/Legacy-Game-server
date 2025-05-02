// File: src/main/kotlin/org/alter/game/message/handler/MessagePublicHandler.kt
package org.alter.game.message.handler

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
        println("🔥 [DEBUG] autoBan=${cfg.autoBanEnabled}, autoIPBan=${cfg.autoIPBanEnabled}, prev='${client.lastPublicMessage}', delta=${now - client.lastPublicMessageTime}ms")

        // 3) flood‐check + autoban (+ optioneel auto‐IP‐ban)
        if (cfg.autoBanEnabled &&
            unpacked == client.lastPublicMessage &&
            now - client.lastPublicMessageTime < cfg.autoBanIntervalMs) {

            println("🔥 [DEBUG] AUTO‐BAN triggered for ${client.username}")

            // 3.1) account‐ban
            client.privilege = world.privileges.get(-1)!!

            // 3.2) optioneel IP‐ban
            if (cfg.autoIPBanEnabled) {
                IpBanService.getIpForUser(client.username)?.let { ip ->
                    IpBanService.add(ip)
                    IpBanService.load()
                    println("🔥 [DEBUG] AUTO‐IP‐BAN: $ip")
                }
            }

            // 3.3) kick/logout
            client.requestLogout()
            client.channel.close()

            // 3.4) log auto‐ban (account + mogelijk IP)
            val suffix = if (cfg.autoIPBanEnabled) " (account+IP banned)" else " (account banned)"
            world.getService(PublicChatLoggerService::class.java, true)
                ?.logPublicChat(client, "[AUTO‐BAN flood] \"$unpacked\"$suffix")

            return
        }

        // 4) sla laatste bericht/tijd op
        client.lastPublicMessage     = unpacked
        client.lastPublicMessageTime = now

        // 5) normale chat‐flow
        val type = ChatMessage.ChatType.values.firstOrNull { it.id == message.type }
            ?: ChatMessage.ChatType.NONE
        val effect = ChatMessage.ChatEffect.values.firstOrNull { it.id == message.effect }
            ?: ChatMessage.ChatEffect.NONE
        val color = ChatMessage.ChatColor.values.firstOrNull { it.id == message.color }
            ?: ChatMessage.ChatColor.NONE

        client.blockBuffer.publicChat = ChatMessage(unpacked, client.privilege.icon, type, effect, color)
        client.addBlock(UpdateBlockType.PUBLIC_CHAT)

        // 6) log het bericht
        world.getService(PublicChatLoggerService::class.java, true)
            ?.logPublicChat(client, unpacked)
    }
}
