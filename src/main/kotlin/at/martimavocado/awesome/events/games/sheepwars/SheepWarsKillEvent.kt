package at.martimavocado.awesome.events.games.sheepwars

import at.martimavocado.awesome.features.sheepwars.SheepWarsAPI
import net.minecraftforge.fml.common.eventhandler.Event

class SheepWarsKillEvent(
    val killer: String?,
    val killerTeam: SheepWarsAPI.TeamType?,
    val player: String,
    val playerTeam: SheepWarsAPI.TeamType,
) : Event()
