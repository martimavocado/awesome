package at.martimavocado.awesome.events.chat

import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class PartyChatEvent(
    val message: String,
    val author: String
): Event()