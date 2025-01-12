package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.features.sheepwars.SheepWarsAPI.getTeamColor
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.mixins.hooks.RenderLivingEntityHelper
import at.martimavocado.awesome.utils.AwesomeColor
import at.martimavocado.awesome.utils.ColorUtils.withAlpha
import at.martimavocado.awesome.utils.EntityUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object PlayerHealthHighlighter {
    private val config get() = Awesome.config.sheepWars.healthHighlight

    // todo: use a better event
    @SubscribeEvent
    fun onEntityUpdate(event: AwesomeTickEvent) {
        if (!config.enabled) return
        if (!SheepWarsAPI.isPlaying()) return
        if (event.totalTicks % 10 != 0) {
            return
        }

        val players = EntityUtils.getPlayers()

        for (player in players) {
            player.getTeamColor() ?: continue

            val color =
                when (player.health) {
                    in 0f..6f -> AwesomeColor.RED
                    in 6f..10f -> AwesomeColor.YELLOW
                    20f -> AwesomeColor.GREEN
                    else -> {
                        RenderLivingEntityHelper.removeEntityColor(player)
                        continue
                    }
                }

            RenderLivingEntityHelper.setEntityColor(
                player,
                color.color.withAlpha(125),
            ) { SheepWarsAPI.isPlaying() }
        }
    }
}
