package org.alter.plugins.content

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

/**
 * Singleton voor dynamisch beheren van gebande IP-adressen en bijhouden van
 * de laatste login-IP per gebruiker.
 */
object IpBanService {

    // Bestand met gebande IPs (zelfde pad als in LoginWorker)
    private val file: Path = Paths.get("data", "bannedip")
    private val lock = ReentrantReadWriteLock()

    // In-memory set van gebande IPs
    private val ips = mutableSetOf<String>()

    // In-memory map van username -> laatste login IP
    private val userIpMap = mutableMapOf<String, String>()

    init {
        load()
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
        // Zorg dat parent-dir bestaat
        file.parent?.let { Files.createDirectories(it) }
        Files.write(
            file,
            ips.joinToString(System.lineSeparator()).toByteArray(),
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        )
    }

    /**
     * Voegt een IP toe aan de ban-lijst (in geheugen + op schijf).
     */
    fun add(ip: String) = lock.write {
        if (ips.add(ip)) save()
    }

    /**
     * Verwijdert een IP van de ban-lijst (in geheugen + op schijf).
     */
    fun remove(ip: String) = lock.write {
        if (ips.remove(ip)) save()
    }

    /**
     * Checkt of een IP geband is.
     */
    fun isBanned(ip: String): Boolean = lock.read { ips.contains(ip) }

    /**
     * Return het aantal gebande IPs (handig voor statusmeldingen).
     */
    fun count(): Int = lock.read { ips.size }

    /**
     * Slaat de login-IP op voor een gegeven gebruiker.
     */
    fun recordLogin(username: String, ip: String) = lock.write {
        userIpMap[username] = ip
    }

    /**
     * Haalt de login-IP op voor een gegeven gebruiker.
     */
    fun getIpForUser(username: String): String? = lock.read {
        userIpMap[username]
    }

    /**
     * Verwijdert de opgeslagen IP voor een gebruiker (bij logout).
     */
    fun removeUser(username: String) = lock.write {
        userIpMap.remove(username)
    }
}
