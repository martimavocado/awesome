package at.martimavocado.awesome.utils

import at.martimavocado.awesome.events.hypixel.HypixelJoinEvent
import at.martimavocado.awesome.events.hypixel.HypixelPartyEvent
import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.ClientboundHelloPacket
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.minecraftforge.common.MinecraftForge
import kotlin.jvm.optionals.getOrNull

@LoadModule
object HypixelEventAPI {
    init {
        val modApi = HypixelModAPI.getInstance()
        modApi.subscribeToEventPacket(ClientboundLocationPacket::class.java)
        modApi.createHandler(ClientboundHelloPacket::class.java, ::onHelloPacket)
        modApi.createHandler(ClientboundLocationPacket::class.java, ::onLocationPacket)
        modApi.createHandler(ClientboundPartyInfoPacket::class.java, ::onPartyPacket)
    }

    private fun onHelloPacket(packet: ClientboundHelloPacket) {
        MinecraftForge.EVENT_BUS.post(HypixelJoinEvent(packet.environment))
    }

    private fun onLocationPacket(packet: ClientboundLocationPacket) {
        MinecraftForge.EVENT_BUS.post(
            HypixelServerChangeEvent(
                packet.serverName,
                packet.serverType.getOrNull(),
                packet.lobbyName.getOrNull(),
                packet.mode.getOrNull(),
                packet.map.getOrNull(),
            ),
        )
    }

    private fun onPartyPacket(packet: ClientboundPartyInfoPacket) {
        MinecraftForge.EVENT_BUS.post(
            HypixelPartyEvent(
                packet.isInParty,
                packet.leader.getOrNull(),
                packet.members,
            ),
        )
    }
}
