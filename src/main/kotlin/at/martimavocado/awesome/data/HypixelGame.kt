package at.martimavocado.awesome.data

import net.hypixel.data.type.GameType

enum class HypixelGame(private val internalName: GameType, private val gameMode: String) {
    SHEEP_WARS(GameType.WOOL_GAMES, "sheep_wars"),
    ;

    fun isPlaying(): Boolean {
        val internalNameMatches = HypixelData.gameType == internalName
        val gameModeMatches = HypixelData.gameMode?.startsWith(gameMode) ?: false

        return internalNameMatches && gameModeMatches
    }
}