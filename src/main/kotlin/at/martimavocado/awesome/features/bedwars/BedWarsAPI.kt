package at.martimavocado.awesome.features.bedwars

import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ChatUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object BedWarsAPI {
    var lobbyType: GameType? = null
        private set

    @SubscribeEvent
    fun onHypixelData(event: HypixelServerChangeEvent) {
        lobbyType =
            if (!HypixelGame.BEDWARS.isPlaying()) {
                null
            } else {
                GameType.getTypeFromString(event.mode) ?: run {
                    ChatUtils.warning("unknown bedwars mode! pls report")
                    GameType.QUAD
                }
            }
    }

    enum class GameType(
        val mode: String?,
    ) {
        LOBBY(null),
        SOLO("BEDWARS_EIGHT_ONE"),
        DOUBLES("BEDWARS_EIGHT_TWO"),
        TRIPLE("BEDWARS_FOUR_THREE"),
        QUAD("BEDWARS_FOUR_FOUR"),
        FOUR_VS_FOUR("BEDWARS_TWO_FOUR"),
        PRACTICE("BEDWARS_PRACTICE"),
        ;

        fun isPlaying() = lobbyType == this

        companion object {
            fun getTypeFromString(gameMode: String?): GameType? {
                if (gameMode == null) return LOBBY

                return entries.firstOrNull { it.mode != null && gameMode.contains(it.mode) }
            }
        }
    }

    enum class DreamType(
        private val internalID: String,
        val validGameType: List<GameType>? = listOf(GameType.DOUBLES, GameType.QUAD),
    ) {
        RUSH(""),
        ULTIMATE(""),
        CASTLE("", null),
        VOIDLESS(""),
        ARMED("_ARMED"),
        LUCKY_BLOCK(""),
        SWAP(""),
        UNDERWORLD(""),
    }
}
