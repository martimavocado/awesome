package at.martimavocado.awesome.data

import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.hypixel.data.type.GameType
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

enum class HypixelGame(
    private val internalName: GameType,
    private val gameMode: String,
    val prettyName: String,
) {
    SHEEP_WARS(GameType.WOOL_GAMES, "sheep_wars", "Sheep Wars"),
    ;

    override fun toString(): String = prettyName

    fun isPlaying(): Boolean = currentGame == this

    @LoadModule
    companion object {
        var currentGame: HypixelGame? = null
            private set

        @SubscribeEvent
        fun onHypixelData(event: HypixelServerChangeEvent) {
            currentGame =
                HypixelGame.entries.firstOrNull {
                    it.internalName == event.serverType &&
                        (event.mode?.startsWith(it.gameMode) == true)
                }
        }
    }
}
