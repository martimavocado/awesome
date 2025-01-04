package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.render.WorldRenderEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ColorUtils.toColor
import at.martimavocado.awesome.utils.render.RenderUtils.highlightBlock
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

@LoadModule
object MagicWoolHighlight {
    private val config get() = Awesome.config.sheepWars.magicWoolHighlight

    @SubscribeEvent
    fun onRender(event: WorldRenderEvent) {
        if (!isEnabled()) return

        val location = SheepWarsAPI.magicWool?.location ?: return
        val color: Color = if (config.colorMatch) SheepWarsAPI.magicWool?.type?.color?.color ?: return
                        else config.color.toColor()

        event.highlightBlock(
            location,
            color,
            config.beacon,
            config.beacon,
            thickness = 1.0f
        )
    }

    private fun isEnabled() = SheepWarsAPI.isPlaying() && config.enabled
}