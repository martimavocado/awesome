package at.martimavocado.awesome.data.managers

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.PacketReceivedEvent
import at.martimavocado.awesome.events.render.TitleReceivedEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.EventUtils.cancel
import at.martimavocado.awesome.utils.EventUtils.post
import at.martimavocado.awesome.utils.StringUtils.cleanupColors
import at.martimavocado.awesome.utils.system.AwesomeLogger
import net.minecraft.network.play.server.S45PacketTitle
import net.minecraft.util.IChatComponent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object TitleManager {
    private val config get() = Awesome.Companion.config.debug
    private val logger = AwesomeLogger("titles")

    @SubscribeEvent
    fun onPacket(event: PacketReceivedEvent) {
        val packet = event.packet

        if (packet !is S45PacketTitle) return
        val chatComponent = packet.message ?: return
        val formattedText = chatComponent.formattedText?.cleanupColors() ?: return

        if (formattedText.isEmpty()) return

        val newEvent = TitleReceivedEvent(formattedText, chatComponent, packet.type)
        logTitles(formattedText, chatComponent, packet.type)

        newEvent.post()
        if (newEvent.isCanceled) event.cancel()
    }

    private fun logTitles(
        message: String,
        component: IChatComponent,
        type: S45PacketTitle.Type,
    ) {
        val typeString = type.toString()

        if (config.printMessages) logger.log("[$typeString] '$message'")
        if (config.printChatComponents) logger.log("[$typeString] '$component'")
    }
}
