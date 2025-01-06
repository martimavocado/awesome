package at.martimavocado.awesome.events.chat

import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class ChatSendEvent(
    var message: String,
) : Event()
