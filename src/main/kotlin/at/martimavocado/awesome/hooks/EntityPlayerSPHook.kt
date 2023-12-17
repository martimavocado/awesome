package at.martimavocado.awesome.hooks

import at.martimavocado.awesome.events.ClientMessageEvent
import at.martimavocado.awesome.utils.CommandsSentToServerLogger
import net.minecraftforge.common.MinecraftForge

fun sendChatMessage(message: String) {
    CommandsSentToServerLogger.logCommandsToServer(message)
    MinecraftForge.EVENT_BUS.post(ClientMessageEvent(message))
}
