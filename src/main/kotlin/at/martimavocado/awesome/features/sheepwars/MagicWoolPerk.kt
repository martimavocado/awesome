package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.events.render.GuiOverlayRenderEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.render.Position
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

@LoadModule
object MagicWoolPerk {
    private val position = Position(10, 10)

    @SubscribeEvent
    fun onOverlay(event: GuiOverlayRenderEvent) {
        if (!isEnabled()) return
        val wool = SheepWarsAPI.magicWool ?: return

        val string = "§${wool.color.colorCode}$wool§7: §f${wool.perk}"

        position.renderString(string)
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    fun onTickEvent(event: ClientTickEvent) {
        if (!isEnabled()) return
        val age = SheepWarsAPI.magicWoolAge ?: return

        if (age % 7 != 0) return


    }

    private fun isEnabled() = HypixelGame.SHEEP_WARS.isPlaying()
}