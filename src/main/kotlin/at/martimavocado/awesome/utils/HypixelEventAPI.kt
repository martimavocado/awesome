package at.martimavocado.awesome.utils

import at.martimavocado.awesome.events.CommandRegistrationEvent
import at.martimavocado.awesome.events.hypixel.HypixelJoinEvent
import at.martimavocado.awesome.events.hypixel.HypixelPartyEvent
import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.hypixel.modapi.HypixelModAPI
import net.hypixel.modapi.packet.impl.clientbound.ClientboundHelloPacket
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPartyInfoPacket
import net.hypixel.modapi.packet.impl.clientbound.ClientboundPingPacket
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.jvm.optionals.getOrNull

@LoadModule
object HypixelEventAPI {
    init {
        val modApi = HypixelModAPI.getInstance()
        modApi.subscribeToEventPacket(ClientboundLocationPacket::class.java)
        modApi.createHandler(ClientboundHelloPacket::class.java, ::onHelloPacket)
        modApi.createHandler(ClientboundLocationPacket::class.java, ::onLocationPacket)
        modApi.createHandler(ClientboundPartyInfoPacket::class.java, ::onPartyPacket)
        modApi.createHandler(ClientboundPingPacket::class.java, ::onPingPacket)
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

    private var pingTimer = SimpleTimeMark.farPast()

    private fun onPingPacket(packet: ClientboundPingPacket) {
        ChatUtils.chat(pingTimer.passedSince())
    }

    @Suppress("UnstableApiUsage")
    private fun pingCommand() {
        val packet = ClientboundPingPacket("pong")

        pingTimer = SimpleTimeMark.now()
        HypixelModAPI.getInstance().sendPacket(packet)
    }

    @SubscribeEvent
    fun onCommandRegistration(event: CommandRegistrationEvent) {
        event.register("awping") {
            description = "Checks your ping using the Hypixel modAPI"
            callback { pingCommand() }
        }
    }
}
