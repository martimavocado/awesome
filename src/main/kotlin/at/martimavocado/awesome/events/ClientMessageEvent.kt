package at.martimavocado.awesome.events

import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class ClientMessageEvent (var message: String) : Event()