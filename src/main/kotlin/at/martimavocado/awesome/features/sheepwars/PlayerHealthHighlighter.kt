package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.config.elements.ConfigColor
import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.features.sheepwars.SheepWarsAPI.getTeamColor
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.mixins.hooks.RenderLivingEntityHelper
import at.martimavocado.awesome.utils.AwesomeColor
import at.martimavocado.awesome.utils.ColorUtils.withAlpha
import at.martimavocado.awesome.utils.EntityUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

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

            var color =
                if (!config.dynamicColor) {
                    when (player.health) {
                        in 0f..6f -> AwesomeColor.RED.color
                        in 6f..10f -> AwesomeColor.YELLOW.color
                        in 20f..24f -> AwesomeColor.GREEN.color
                        else -> {
                            RenderLivingEntityHelper.removeEntityColor(player)
                            continue
                        }
                    }
                } else {
                    getBlendedColor(player.health)
                }

            RenderLivingEntityHelper.setEntityColor(
                player,
                color.withAlpha(125),
            ) { SheepWarsAPI.isPlaying() }
        }
    }

    private fun getBlendedColor(health: Float): Color {
        val highColor = ConfigColor(config.highHPColor).toColor()
        val lowColor = ConfigColor(config.lowHPColor).toColor()

        return blendColors(highColor, lowColor, ((health / 20f).toDouble().coerceIn(0.0..1.0)))
    }

    private fun blendColors(
        color1: Color,
        color2: Color,
        amount: Double,
    ): Color {
        val red = (color1.red * amount) + (color2.red * (1.0 - amount))
        val green = (color1.green * amount) + (color2.green * (1.0 - amount))
        val blue = (color1.blue * amount) + (color2.blue * (1.0 - amount))
        val alpha = (color1.alpha * amount) + (color2.alpha * (1.0 - amount))

        return Color(red.toInt(), green.toInt(), blue.toInt(), alpha.toInt())
    }
}
