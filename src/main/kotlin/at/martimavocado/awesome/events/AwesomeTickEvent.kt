package at.martimavocado.awesome.events

import net.minecraftforge.fml.common.eventhandler.Event

class AwesomeTickEvent(
    val totalTicks: Int,
) : Event()
