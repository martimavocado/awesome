package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.events.render.GuiOverlayRenderEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

@LoadModule
object MagicWoolPerk {
    private val config get() = Awesome.config.sheepWars.magicWoolPerk

    @SubscribeEvent
    fun onOverlay(event: GuiOverlayRenderEvent) {
        if (!(isEnabled() && config.perkGUI)) return
        val wool = SheepWarsAPI.magicWool ?: return

        val string = "§${wool.color.colorCode}$wool§7: §f${wool.perk}"

        config.perkPosition.renderString(string)
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    fun onTickEvent(event: ClientTickEvent) {
        if (!isEnabled()) return
        val age = SheepWarsAPI.magicWoolAge ?: return

        if (age % 7 != 0) return


    }

    private fun isEnabled() = HypixelGame.SHEEP_WARS.isPlaying()
}