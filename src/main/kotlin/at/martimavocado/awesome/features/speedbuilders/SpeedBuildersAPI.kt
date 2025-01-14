package at.martimavocado.awesome.features.speedbuilders

import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.events.DebugDataCollectionEvent
import at.martimavocado.awesome.events.ScoreboardUpdateEvent
import at.martimavocado.awesome.events.chat.ChatReceiveEvent
import at.martimavocado.awesome.events.games.speedbuilders.SpeedBuildersPhaseChange
import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.events.render.TitleReceivedEvent
import at.martimavocado.awesome.events.render.WorldRenderEvent
import at.martimavocado.awesome.features.speedbuilders.data.SpeedBuildersPlot
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.AwesomeColor
import at.martimavocado.awesome.utils.BlockUtils.getBlockAt
import at.martimavocado.awesome.utils.EventUtils.post
import at.martimavocado.awesome.utils.PlayerUtils
import at.martimavocado.awesome.utils.RegexUtils.matchMatcher
import at.martimavocado.awesome.utils.RegexUtils.matches
import at.martimavocado.awesome.utils.render.RenderUtils.drawFilledBoundingBoxNea
import at.martimavocado.awesome.utils.render.RenderUtils.highlightBlock
import net.minecraft.network.play.server.S45PacketTitle
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object SpeedBuildersAPI {
    var gameState: GameState? = null
        private set

    private val roundScoreboardPattern = "Round: §.(?<round>\\d+)".toPattern()

    private val buildEndPattern = "§aYour builds are being judged\\.\\.\\.".toPattern()
    private val playerEliminated = "§cPlayer eliminated: .*".toPattern()
    private val noPlayerEliminated = "§cNo players were eliminated this round!".toPattern()

    private val gameEndPattern = "§e§r§f {36}§r§fWinners".toPattern()

    var plot: SpeedBuildersPlot? = null
        private set

    @SubscribeEvent
    fun onScoreboard(event: ScoreboardUpdateEvent.Content) {
        if (!isPlaying()) return

        for (line in event.newLines) {
            roundScoreboardPattern.matchMatcher(line) {
                if (plot != null) return
                val playerLocation = PlayerUtils.getPlayerLocation() ?: return

                changeState(GameState.LOOKING_AT_BUILD)
                val position = playerLocation.add(x = -1, z = -1).copy(y = 71.0).roundToInt()

                plot = SpeedBuildersPlot(position)
                return
            }
        }
    }

    @SubscribeEvent
    fun onTitle(event: TitleReceivedEvent) {
        if (event.type != S45PacketTitle.Type.TITLE) return
        if (event.formattedText != "§cView Time Over!") return

        changeState(GameState.BUILDING)
    }

    @SubscribeEvent
    fun onChat(event: ChatReceiveEvent) {
        if (!isPlaying()) return

        if (buildEndPattern.matches(event.message)) {
            changeState(GameState.EVALUATING)
            return
        }

        if (gameEndPattern.matches(event.message)) {
            changeState(GameState.POST_GAME)
            return
        }

        if (playerEliminated.matches(event.message) || noPlayerEliminated.matches(event.message)) {
            changeState(GameState.LOOKING_AT_BUILD)
            return
        }
    }

    @SubscribeEvent
    fun onServerChange(event: HypixelServerChangeEvent) {
        plot = null

        if (isPlaying()) {
            changeState(GameState.PRE_GAME)
        } else {
            changeState(null)
        }
    }

    fun changeState(newState: GameState?) {
        val oldState = gameState
        gameState = newState

        if (oldState == newState) return
        SpeedBuildersPhaseChange(oldState, newState).post()
    }

    fun isPlaying() = HypixelGame.SPEED_BUILDERS.isPlaying()

    @SubscribeEvent
    fun onWorldRender(event: WorldRenderEvent) {
        if (!isPlaying()) return

        plot?.let {
            event.highlightBlock(
                it.centerPoint,
                AwesomeColor.BLACK.color,
            )

            event.drawFilledBoundingBoxNea(
                it.boundingBox,
                AwesomeColor.LIME.color,
                alphaMultiplier = 0.1f,
            )
        }
    }

    @SubscribeEvent
    fun onDebug(event: DebugDataCollectionEvent) {
        event.title("Speed Builders API")

        if (!isPlaying()) {
            event.addIrrelevant("not playing")
        } else {
            event.addData {
                add("gameState: $gameState")
                add("plotCenter: ${plot?.centerPoint}")
                add("plotCenter block: ${plot?.centerPoint?.getBlockAt()}")
                add("plotBB: ${plot?.boundingBox}")
            }
        }
    }

    enum class GameState {
        PRE_GAME,
        LOOKING_AT_BUILD,
        BUILDING,
        EVALUATING,
        POST_GAME,
    }
}
