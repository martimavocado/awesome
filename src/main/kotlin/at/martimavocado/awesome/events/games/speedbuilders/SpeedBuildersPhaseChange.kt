package at.martimavocado.awesome.events.games.speedbuilders

import at.martimavocado.awesome.features.speedbuilders.SpeedBuildersAPI
import net.minecraftforge.fml.common.eventhandler.Event

class SpeedBuildersPhaseChange(
    val oldPhase: SpeedBuildersAPI.GameState?,
    val newPhase: SpeedBuildersAPI.GameState?,
) : Event() {
    override fun toString() = "SpeedBuildersPhaseChange($oldPhase, $newPhase)"
}
