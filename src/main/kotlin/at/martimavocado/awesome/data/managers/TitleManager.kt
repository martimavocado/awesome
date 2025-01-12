package at.martimavocado.awesome.data.managers

import at.martimavocado.awesome.events.PacketReceivedEvent
import at.martimavocado.awesome.events.render.TitleReceivedEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.OtherUtils.cancel
import at.martimavocado.awesome.utils.OtherUtils.post
import at.martimavocado.awesome.utils.StringUtils.cleanupColors
import net.minecraft.network.play.server.S45PacketTitle
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object TitleManager {
    @SubscribeEvent
    fun onPacket(event: PacketReceivedEvent) {
        val packet = event.packet

        if (packet !is S45PacketTitle) return
        val chatComponent = packet.message ?: return
        val formattedText = chatComponent.formattedText?.cleanupColors() ?: return

        if (formattedText.isEmpty()) return

        val newEvent = TitleReceivedEvent(formattedText, chatComponent, packet.type)

        newEvent.post()
        if (newEvent.isCanceled) event.cancel()
    }
}
