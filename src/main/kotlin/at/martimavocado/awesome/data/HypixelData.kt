package at.martimavocado.awesome.data

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.config.ConfigManager
import at.martimavocado.awesome.events.hypixel.HypixelJoinEvent
import at.martimavocado.awesome.events.hypixel.HypixelPartyEvent
import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ChatUtils
import net.hypixel.data.type.ServerType
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object HypixelData {
    private val config get() = Awesome.config.debug

    var lobbyName: String? = null
        private set
    var gameMode: String? = null
        private set
    var serverName: String? = null
        private set
    var gameType: ServerType? = null
        private set
    var map: String? = null
        private set

    private var shownConfigMessages = false

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onServerChange(event: HypixelServerChangeEvent) {
        map = event.map
        gameMode = event.mode
        gameType = event.serverType
        lobbyName = event.lobbyName
        serverName = event.serverName

        if (!config.modAPI) return
        val message = "map: ${event.map}\n" +
                "mode: ${event.mode}\n" +
                "lobbyName: ${event.lobbyName}\n" +
                "serverName: ${event.serverName}\n" +
                "serverType: ${event.serverType}"

        ChatUtils.chat(message)
    }

    @SubscribeEvent
    fun onPartyChange(event: HypixelPartyEvent) {
        if (!config.modAPI) return

        val message = "inParty: ${event.inParty}\n" +
                "leader: ${event.leader}\n" +
                "members: ${event.members?.joinToString(",") ?: "null"}"

        ChatUtils.chat(message)
    }

    @SubscribeEvent
    fun onHypixelJoin(event: HypixelJoinEvent) {
        if (shownConfigMessages) return
        var message = ""

        if (ConfigManager.wasCorrupted)
            message += "config was corrupted, oops. "
        if (ConfigManager.loadedOld)
            message += "loaded config backup instead"

        if (message.isEmpty()) return
        ChatUtils.warning(message)
    }
}

