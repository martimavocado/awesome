package at.martimavocado.awesome.data

import at.martimavocado.awesome.events.DebugDataCollectionEvent
import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.hypixel.data.type.GameType
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

enum class HypixelGame(
    private val internalName: GameType,
    private val gameMode: String,
    val prettyName: String,
) {
    SHEEP_WARS(GameType.WOOL_GAMES, "sheep_wars", "Sheep Wars"),
    SPEED_BUILDERS(GameType.BUILD_BATTLE, "BUILD_BATTLE_SPEED_BUILDERS", "Speed Builders"),
    HOLE_IN_THE_WALL(GameType.ARCADE, "HOLE_IN_THE_WALL", "Hole in the Wall"),
    ;

    override fun toString(): String = prettyName

    fun isPlaying(): Boolean = currentGame == this

    @LoadModule
    companion object {
        var currentGame: HypixelGame? = null
            private set

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        fun onHypixelData(event: HypixelServerChangeEvent) {
            currentGame =
                HypixelGame.entries.firstOrNull {
                    it.internalName == event.serverType &&
                        (event.mode?.startsWith(it.gameMode) == true)
                }
        }

        @SubscribeEvent
        fun onDebug(event: DebugDataCollectionEvent) {
            event.title("Hypixel Game")

            if (currentGame != null) {
                event.addIrrelevant("playing ${currentGame?.prettyName}")
            } else {
                event.addData {
                    add("not playing anything (known)")
                    add("")
                    add("gameType: ${HypixelData.gameType}")
                    add("gameMode: ${HypixelData.gameMode}")
                }
            }
        }
    }
}
