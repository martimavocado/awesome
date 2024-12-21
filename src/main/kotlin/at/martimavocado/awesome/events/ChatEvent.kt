package at.martimavocado.awesome.events

import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class ChatEvent (var message: String): Event()