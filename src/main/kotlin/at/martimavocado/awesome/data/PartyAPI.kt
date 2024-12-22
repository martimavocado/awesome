package at.martimavocado.awesome.data

import at.martimavocado.awesome.events.hypixel.HypixelJoinEvent
import at.martimavocado.awesome.events.hypixel.HypixelPartyEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.PlayerUtils
import at.martimavocado.awesome.utils.PlayerUtils.getPlayerName
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.serverbound.ServerboundPartyInfoPacket
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object PartyAPI {
    var inParty = false
        private set
    var partyLeader: String? = null
        private set
    var partyMembers = setOf<String>()
        private set

    @SubscribeEvent
    fun onHypixelJoin(event: HypixelJoinEvent) {
        sendPartyPacket()
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    fun onParty(event: HypixelPartyEvent) {
        inParty = event.inParty

        partyLeader = PlayerUtils.cachedUUID[event.leader]

        if (event.members == null) {
            partyMembers = emptySet()
            return
        }

        val members = mutableSetOf<String>()
        for (member in event.members) {
            val name = PlayerUtils.cachedUUID[member] ?: member.getPlayerName()
            members.add(name)
        }
        partyMembers = members.toSet()
    }

    fun sendPartyPacket() = HypixelModAPI.getInstance().sendPacket(ServerboundPartyInfoPacket())
}