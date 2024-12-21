package at.martimavocado.awesome.events

import net.minecraft.util.IChatComponent
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class ChatSendEvent(
    var message: String,
): Event()