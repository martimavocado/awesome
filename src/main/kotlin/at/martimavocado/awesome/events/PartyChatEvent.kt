package at.martimavocado.awesome.events

import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class PartyChatEvent(
    var message: String,
    val author: String
): Event()