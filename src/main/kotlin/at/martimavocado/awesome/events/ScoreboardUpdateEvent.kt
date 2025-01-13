package at.martimavocado.awesome.events

import net.minecraftforge.fml.common.eventhandler.Event

open class ScoreboardUpdateEvent : Event() {
    open class Title(
        val title: String,
        val objective: String,
    ) : ScoreboardUpdateEvent()

    open class Content(
        val oldLines: List<String>,
        val newLines: List<String>,
    ) : ScoreboardUpdateEvent()
}
