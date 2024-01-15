package at.martimavocado.awesome.hooks

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.config.categories.debug
import at.martimavocado.awesome.events.ShowTitleEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiIngame
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.minecraftforge.fml.relauncher.ReflectionHelper


class ShowTitleHook {
    private var storedTitle = ""
    private var storedSubTile = ""
    val config: debug get() = Awesome.config.debug

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        val currentTitle = getCurrentTitle()
        val currentSubTitle = getCurrentSubTitle()
        if (currentTitle != storedTitle || currentSubTitle != storedSubTile) {
            if (currentTitle != storedTitle) {
                storedTitle = currentTitle
                if (config.logTitles) println("Showing title: $storedTitle")
            }
            if (currentSubTitle != storedSubTile) {
                storedSubTile = currentSubTitle
                if (config.logTitles) println("Showing subtitle: $storedSubTile")
            }
            val showTitleEvent: Event = ShowTitleEvent(storedTitle,storedSubTile)
            MinecraftForge.EVENT_BUS.post(showTitleEvent)
        }
    }

    private fun getCurrentTitle(): String {
        try {
            return ReflectionHelper.findField(
                GuiIngame::class.java,
                "displayedTitle",
                "field_175201_x"
            )[Minecraft.getMinecraft().ingameGUI] as String
        } catch (_: IllegalAccessException) {
        }
        return ""
    }
    private fun getCurrentSubTitle(): String {
        try {
            return ReflectionHelper.findField(
                GuiIngame::class.java,
                "displayedSubTitle",
                "field_175200_y"
            )[Minecraft.getMinecraft().ingameGUI] as String
        } catch (_: IllegalAccessException) {
        }
        return ""
    }
}