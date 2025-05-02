package org.alter.plugins.content

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Singleton voor dynamisch beheren van gebande IP-adressen en bijhouden van
 * de laatste login-IP per gebruiker, inclusief aparte ban-log.
 */
object IpBanService {

    // Bestand met gebande IPs (zelfde pad als in LoginWorker)
    private val file: Path = Paths.get("data", "bannedip")
    // Nieuw: logfile voor IP-ban events
    private val logFile: Path = Paths.get("data", "ipban.log")

    private val lock = ReentrantReadWriteLock()

    // In-memory set van gebande IPs
    private val ips = mutableSetOf<String>()

    // In-memory map van username -> laatste login IP
    private val userIpMap = mutableMapOf<String, String>()

    // Voor het timestamp-formaat
    private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    init {
        // 1) Laad bestaande bans
        load()
        // 2) Zorg dat data-map én logfile bestaan
        file.parent?.let { Files.createDirectories(it) }
        if (!Files.exists(logFile)) {
            try {
                Files.createFile(logFile)
            } catch (e: IOException) {
                System.err.println("Kon ipban.log niet aanmaken: ${e.message}")
            }
        }
    }

    /**
     * (Her)laadt de ban-lijst vanaf schijf naar geheugen.
     */
    fun load() = lock.write {
        ips.clear()
        if (Files.exists(file)) {
            Files.readAllLines(file).forEach { line ->
                line.trim().takeIf { it.isNotEmpty() }?.let { ips += it }
            }
        }
    }

    /**
     * Slaat de huidige in-memory ban-lijst terug naar schijf.
     */
    private fun save() = lock.read {
        file.parent?.let { Files.createDirectories(it) }
        Files.write(
            file,
            ips.joinToString(System.lineSeparator()).toByteArray(),
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        )
    }

    /**
     * Voegt een IP toe aan de ban-lijst (in geheugen + op schijf) én logt welke speler daarbij hoort.
     */
    fun add(ip: String) = lock.write {
        if (ips.add(ip)) {
            save()

            // Bepaal gebruikersnaam(n) die corresponderen met dit IP
            val users = userIpMap.filterValues { it == ip }.keys
            val userList = if (users.isEmpty()) "unknown" else users.joinToString(", ")

            // Maak een logregel
            val ts = LocalDateTime.now().format(timeFormatter)
            val logLine = "[$ts] IP BANNED: $ip for user(s): $userList${System.lineSeparator()}"

            // Schrijf naar ipban.log
            try {
                Files.write(
                    logFile,
                    logLine.toByteArray(Charsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
                )
            } catch (e: IOException) {
                System.err.println("Fout bij wegschrijven ipban.log: ${e.message}")
            }
        }
    }

    /**
     * Verwijdert een IP van de ban-lijst (in geheugen + op schijf).
     */
    fun remove(ip: String) = lock.write {
        if (ips.remove(ip)) {
            save()
            // (Optioneel kun je hier ook een unban‐log maken)
        }
    }

    /** Checkt of een IP geband is. */
    fun isBanned(ip: String): Boolean = lock.read { ips.contains(ip) }

    /** Return het aantal gebande IPs (handig voor statusmeldingen). */
    fun count(): Int = lock.read { ips.size }

    /** Slaat de login-IP op voor een gegeven gebruiker. */
    fun recordLogin(username: String, ip: String) = lock.write {
        userIpMap[username] = ip
    }

    /** Haalt de login-IP op voor een gegeven gebruiker. */
    fun getIpForUser(username: String): String? = lock.read { userIpMap[username] }

    /** Verwijdert de opgeslagen IP voor een gebruiker (bij logout). */
    fun removeUser(username: String) = lock.write { userIpMap.remove(username) }
}
