package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.render.WorldRenderEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ColorUtils.toColor
import at.martimavocado.awesome.utils.render.RenderUtils.highlightBlock
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object MagicWoolHighlight {
    private val config get() = Awesome.config.sheepWars.magicWoolHighlight

    @SubscribeEvent
    fun onRender(event: WorldRenderEvent) {
        if (!isEnabled()) return
        if (!config.spectator && !SheepWarsAPI.isAlive) return

        val wool =
            SheepWarsAPI.magicWool ?: run {
//                println("no wool")
                return
            }
//        println("wool: ${wool.type}")
        val color =
            if (config.colorMatch) {
                wool.type.color.color
            } else {
                config.color.toColor()
            }

        event.highlightBlock(
            wool.location,
            color,
            beaconAbove = config.beacon,
            beaconBelow = config.beacon,
            thickness = 1.0f,
        )
    }

    private fun isEnabled() = SheepWarsAPI.isPlaying() && config.enabled
}
