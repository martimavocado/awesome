package at.martimavocado.awesome.events.games.sheepwars

import at.martimavocado.awesome.data.GameStatus
import net.minecraftforge.fml.common.eventhandler.Event

class SheepWarsStatusEvent(
    val gameStatus: GameStatus,
) : Event()
