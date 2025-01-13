package at.martimavocado.awesome.features.speedbuilders

import at.martimavocado.awesome.data.GameStatus
import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.events.chat.ChatReceiveEvent
import at.martimavocado.awesome.events.render.WorldRenderEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.StringUtils.matches
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object SpeedBuildersAPI {
    var gameState: GameStatus? = null
        private set
    var isBuilding: Boolean? = null
        private set

    private val startGamePattern = "§e§r§f {29}§r§f§lSpeed Builders".toPattern()

    @SubscribeEvent
    fun onChat(event: ChatReceiveEvent) {
        if (!isPlaying()) return
        if (!startGamePattern.matches(event.message)) return

        gameState = GameStatus.IN_GAME
        isBuilding = false
    }

    private var location: PositionVec? = null

    @SubscribeEvent
    fun onWorldRender(event: WorldRenderEvent) {
//        if (!isPlaying()) return
//
//        location?.let {
//            event.highlightBlock(
//                it,
//                AwesomeColor.CYAN.color,
//                seeThroughBlocks = true,
//            )
//        }
    }

    fun isPlaying() = HypixelGame.SPEED_BUILDERS.isPlaying()
}
