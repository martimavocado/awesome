package at.martimavocado.awesome.data

import at.martimavocado.awesome.events.hypixel.HypixelJoinEvent
import at.martimavocado.awesome.events.hypixel.HypixelPartyEvent
import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ChatUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object HypixelData {

    @SubscribeEvent
    fun onHypixelJoin(event: HypixelJoinEvent) {

    }

    @SubscribeEvent
    fun onServerChange(event: HypixelServerChangeEvent) {
        var message = ""
        message += "map: ${event.map}\n"
        message += "mode: ${event.mode}\n"
        message += "lobbyName: ${event.lobbyName}\n"
        message += "serverName: ${event.serverName}\n"
        message += "serverType: ${event.serverType}"

        ChatUtils.chat(message)
    }

    @SubscribeEvent
    fun onPartyChange(event: HypixelPartyEvent) {
        var message = ""
        message += "inParty: ${event.inParty}\n"
        message += "leader: ${event.leader}\n"
        message += "members: ${event.members?.joinToString(",") ?: "null"}\n"

        ChatUtils.chat(message)
    }
}