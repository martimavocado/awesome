package at.martimavocado.awesome.features

import at.martimavocado.awesome.awesome
import at.martimavocado.awesome.utils.ChatUtils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ChatFeatures {

    @SubscribeEvent(receiveCanceled = true)
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (event.type.toInt() == 2) return

        val message = event.message.formattedText
        if (awesome.config.secondCategory.chat.duplicateChatMessage) {
            ChatUtils.messageToChat(message)
        }
    }
}
