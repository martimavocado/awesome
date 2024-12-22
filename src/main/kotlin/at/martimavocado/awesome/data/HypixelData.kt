package at.martimavocado.awesome.data

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.hypixel.HypixelPartyEvent
import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ChatUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object HypixelData {
    private val config get() = Awesome.config.debug

    @SubscribeEvent
    fun onServerChange(event: HypixelServerChangeEvent) {
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
}