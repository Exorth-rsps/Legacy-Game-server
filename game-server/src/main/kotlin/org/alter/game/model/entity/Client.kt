package org.alter.game.model.entity

import com.google.common.base.MoreObjects
import org.alter.game.message.Message
import org.alter.game.model.EntityType
import org.alter.game.model.World
import org.alter.game.service.serializer.PlayerSerializerService
import org.alter.game.system.GameSystem
import gg.rsmod.net.codec.login.LoginRequest
import io.netty.channel.Channel

/**
 * A [Player] that is controlled by a human. A [Client] is responsible for
 * handling any network related job.
 *
 * Anything other than network logic should be added to [Player] instead.
 *
 * @param channel
 * The [Channel] used to write and read [Message]s to and from the client.
 *
 * @param world
 * The [World] that this client is registered to.
 *
 * @author Tom <rspsmods@gmail.com>
 */
class Client(val channel: Channel, world: World) : Player(world) {

    /**
     * The [System] that will handle [Message]s, write [Message]s and flush the
     * [Channel].
     */
    lateinit var gameSystem: GameSystem
    /**
     * The username that was used to register the [Player]. This username should
     * never be changed through the player's end.
     */
    lateinit var loginUsername: String

    /**
     * The encrypted password.
     */
    lateinit var passwordHash: String

    /**
     * The client's UUID.
     */
    lateinit var uuid: String

    /**
     * The xteas for the current log-in session.
     */
    lateinit var currentXteaKeys: IntArray

    /**
     * Is the applet focused on the player's computer?
     */
    var appletFocused = true

    /**
     * The applet's current width.
     */
    var clientWidth = 765

    /**
     * The applet's current height.
     */
    var clientHeight = 503

    /**
     * The pitch of the camera in the client's game UI.
     */
    var cameraPitch = 0

    /**
     * The yaw of the camera in the client's game UI.
     */
    var cameraYaw = 0

    /**
     * A flag which indicates that the client will have their incoming packets
     * ([org.alter.game.message.Message]s) logged.
     */
    var logPackets = true

    override val entityType: EntityType = EntityType.CLIENT

    override fun handleLogout() {
        super.handleLogout()
        world.getService(PlayerSerializerService::class.java, searchSubclasses = true)?.saveClientData(this)
    }

    override fun handleMessages() {
        gameSystem.handleMessages()
    }

    override fun write(vararg messages: Message) {
        messages.forEach { m -> gameSystem.write(m) }
    }

    override fun write(vararg messages: Any) {
        messages.forEach { m -> channel.write(m) }
    }

    override fun channelFlush() {
        gameSystem.flush()
    }

    override fun channelClose() {
        gameSystem.close()
    }
    /** Vorige public chat-inhoud */
    var lastPublicMessage: String? = null
    /** Timestamp (ms) van het vorige bericht */
    var lastPublicMessageTime: Long = 0L
    var floodMessageCount: Int = 0            // Aantal dezelfde berichten in het venster
    var floodWindowStart: Long = 0L           // Starttijd van het venster (ms)
    var floodWarned: Boolean = false          // Heeft de speler al een waarschuwing gekregen?
    var floodSinceWarningCount: Int = 0       // Aantal berichten sinds de waarschuwing
    var floodBanThreshold: Int = 0

    override fun toString(): String = MoreObjects.toStringHelper(this)
            .add("login_username", loginUsername)
            .add("username", username)
            .add("channel", channel)
            .toString()

    companion object {

        /**
         * Constructs a [Client] based on the [LoginRequest].
         */
        fun fromRequest(world: World, request: LoginRequest): Client {
            val client = Client(request.channel, world)
            client.clientWidth = request.clientWidth
            client.clientHeight = request.clientHeight
            client.loginUsername = request.username
            client.username = request.username
            client.uuid = request.uuid
            client.currentXteaKeys = request.xteaKeys
            return client
        }
    }
}
