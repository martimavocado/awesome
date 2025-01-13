package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.config.elements.ConfigColor
import at.martimavocado.awesome.data.GameStatus
import at.martimavocado.awesome.events.BlockChangeEvent
import at.martimavocado.awesome.events.entity.EntityHealthUpdateEvent
import at.martimavocado.awesome.events.games.sheepwars.SheepWarsStatusEvent
import at.martimavocado.awesome.features.sheepwars.SheepWarsAPI.getTeamColor
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.mixins.hooks.RenderLivingEntityHelper
import at.martimavocado.awesome.utils.AwesomeColor
import at.martimavocado.awesome.utils.ColorUtils.withAlpha
import at.martimavocado.awesome.utils.EntityUtils
import at.martimavocado.awesome.utils.PlayerUtils
import at.martimavocado.awesome.utils.SimpleTimeMark
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color
import kotlin.time.Duration.Companion.seconds

@LoadModule
object PlayerHealthHighlighter {
    private val config get() = Awesome.config.sheepWars.healthHighlight
    private var lastRecolor = SimpleTimeMark.farPast()

    @SubscribeEvent
    fun onEntityUpdate(event: EntityHealthUpdateEvent) {
        if (!config.enabled) return
        if (!SheepWarsAPI.isPlaying()) return
        val player = event.entity as? EntityPlayer ?: return

        player.getTeamColor() ?: return

        colorPlayer(player, event.health)
    }

    private fun colorPlayer(
        entity: EntityPlayer,
        health: Float,
    ) {
        val maxHealth = entity.maxHealth

        var color =
            if (!config.dynamicColor) {
                when (health) {
                    in 0f..6f -> AwesomeColor.RED.color
                    in 6f..10f -> AwesomeColor.YELLOW.color
                    in 20f..maxHealth -> AwesomeColor.GREEN.color
                    else -> {
                        RenderLivingEntityHelper.removeEntityColor(entity)
                        return
                    }
                }
            } else {
                getBlendedColor(health, maxHealth)
            }

        RenderLivingEntityHelper.setEntityColor(
            entity,
            color.withAlpha(125),
        ) { SheepWarsAPI.isPlaying() }
    }

    private fun getBlendedColor(
        health: Float,
        maxHealth: Float,
    ): Color {
        val highColor = ConfigColor(config.highHPColor).toColor()
        val lowColor = ConfigColor(config.lowHPColor).toColor()

        return blendColors(highColor, lowColor, ((health / maxHealth).toDouble().coerceIn(0.0..1.0)))
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

    @SubscribeEvent
    fun onGameStatus(event: SheepWarsStatusEvent) {
        val playerList = EntityUtils.getPlayers()

        if (event.gameStatus == GameStatus.POST_GAME) {
            removeColors()
        }
    }

    @SubscribeEvent
    fun onBlockEvent(event: BlockChangeEvent) {
        if (lastRecolor.passedSince() < 10.seconds) return
        if (event.old != Blocks.glass) return
        if (event.new != Blocks.air) return

        lastRecolor = SimpleTimeMark.now()
        colorPlayers()
    }

    private fun colorPlayers() {
        val playerList = EntityUtils.getPlayers()

        for (player in playerList) {
            if (player == null || player == PlayerUtils.getPlayer()) continue
            player.getTeamColor() ?: continue

            colorPlayer(player, player.health)
        }
    }

    private fun removeColors() {
        val playerList = EntityUtils.getPlayers()

        for (player in playerList) {
            if (player == null) continue

            RenderLivingEntityHelper.removeEntityColor(player)
        }
    }
}
