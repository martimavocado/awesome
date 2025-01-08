package at.martimavocado.awesome.events.chat

import net.minecraft.util.IChatComponent
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class ChatReceiveEvent(
    val message: String,
    var chatComponent: IChatComponent,
) : Event()
