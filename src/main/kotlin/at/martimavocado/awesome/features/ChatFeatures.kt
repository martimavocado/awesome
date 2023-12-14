package at.martimavocado.awesome.features

import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ChatFeatures {
    private val emojis = arrayOf(
            "a" to "§ca§r"
    )
    @SubscribeEvent(receiveCanceled = true)
    fun onChatReceive(event: ClientChatReceivedEvent) {
        val originalMessage = event.message
        var newMessage = originalMessage.unformattedText
        emojis.forEach { (search, replace) ->
            newMessage = newMessage.replace(search, replace)
        }
        event.message = ChatComponentText(newMessage)
    }
}
