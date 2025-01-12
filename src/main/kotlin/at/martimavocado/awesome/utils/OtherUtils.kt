package at.martimavocado.awesome.utils

import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@LoadModule
object OtherUtils {
    private fun showTitle(
        title: String,
        subtitle: String,
        timeFadeIn: Int,
        displayTime: Int,
        timeFadeOut: Int,
    ) {
        Minecraft.getMinecraft().ingameGUI.displayTitle(title, subtitle, timeFadeIn, displayTime, timeFadeOut)
    }

    fun tryShowTitle(array: Array<String>) {
        if (array.size == 5) {
            val title = array[0]
            val subtitle = array[1]
            val fadeIn = array[2].toIntOrNull() ?: 0
            val stay = array[3].toIntOrNull() ?: 20
            val fadeOut = array[4].toIntOrNull() ?: 0
            showTitle(title, subtitle, fadeIn, stay, fadeOut)
        } else {
            ChatUtils.chat("Wrong Usage! /showtitle title subtitle fadeIn displayTime fadeOut")
        }
    }

    fun Event.post() {
        MinecraftForge.EVENT_BUS.post(this)
    }

    fun Event.cancel() {
        if (!this.isCancelable) return
        this.isCanceled = true
    }

    private var totalTicks = 0

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.END) return
        if (Minecraft.getMinecraft().thePlayer == null) return

        totalTicks++
        AwesomeTickEvent(totalTicks).post()
    }
}
