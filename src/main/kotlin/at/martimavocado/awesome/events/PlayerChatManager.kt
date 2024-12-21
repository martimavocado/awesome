package at.martimavocado.awesome.events

import at.martimavocado.awesome.utils.ChatUtils
import at.martimavocado.awesome.utils.OtherUtils.matchMatcher
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object PlayerChatManager {
    private val partyMessagePattern = "§9P(?:arty)? §8> §.(?:\\[(?:MVP|VIP)?(?:§.\\+§.)?] )?(?<author>\\w+)§f: (?:(?:§r)?)+(?<message>.*)".toPattern()

    @SubscribeEvent(receiveCanceled = true)
    fun onChat(event: ChatReceiveEvent) {
    }

    @SubscribeEvent(receiveCanceled = true)
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (event.type.toInt() == 2) return

        val original = event.message
        var message = original.formattedText

        while (message.startsWith("§r")) {
            message = message.substring(2)
        }
        while (message.endsWith("§r")) {
            message = message.substring(0, message.length - 2)
        }

        val newEvent = ChatReceiveEvent(message, original)
        MinecraftForge.EVENT_BUS.post(newEvent)
        val partyCanceled = handlePartyChat(message)

        if (newEvent.isCanceled || partyCanceled) event.isCanceled = true
    }

    private fun handlePartyChat(rawMessage: String): Boolean {
        ChatUtils.chat("this is a message")
        var isCanceled = false
        partyMessagePattern.matchMatcher(rawMessage) {
            val message = group("message")
            val author = group("author")

            ChatUtils.chat("hello!! '$message' '$author'")

            val event = PartyChatEvent(message, author)
            MinecraftForge.EVENT_BUS.post(event)
            isCanceled = event.isCanceled
        }
        return isCanceled
    }
}