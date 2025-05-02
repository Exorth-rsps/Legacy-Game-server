// File: src/main/kotlin/org/alter/game/service/log/PublicChatLoggerService.kt
package org.alter.game.service.log

import org.alter.game.Server
import org.alter.game.model.World
import org.alter.game.service.Service
import gg.rsmod.util.ServerProperties
import org.alter.game.model.entity.Client
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PublicChatLoggerService : Service {

    private lateinit var logFilePath: Path
    private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    override fun init(server: Server, world: World, serviceProperties: ServerProperties) {
        // Maak directory en bestand klaar
        logFilePath = Paths.get("data", "public_chat.log")
        try {
            Files.createDirectories(logFilePath.parent)
            Files.write(
                logFilePath,
                "=== PublicChatLoggerService gestart op ${LocalDateTime.now()} ===\n".toByteArray(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND
            )
        } catch (e: IOException) {
            System.err.println("Could not create or write directory: ${e.message}")
        }
    }

    override fun postLoad(server: Server, world: World)  { /* no-op */ }
    override fun bindNet(server: Server, world: World)    { /* no-op */ }
    override fun terminate(server: Server, world: World)  { /* no-op */ }

    fun logPublicChat(client: Client, message: String) {
        val ts       = LocalDateTime.now().format(timeFormatter)
        val username = client.username
        val logLine  = "[$ts] [$username] $message${System.lineSeparator()}"
        try {
            Files.write(
                logFilePath,
                logLine.toByteArray(Charsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND
            )
        } catch (e: IOException) {
            System.err.println("Error while writing public_chat.log: ${e.message}")
        }
    }
}
