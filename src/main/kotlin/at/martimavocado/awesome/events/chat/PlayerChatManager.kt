package at.martimavocado.awesome.events.chat

import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.OtherUtils.post
import at.martimavocado.awesome.utils.StringUtils.matchMatcher
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object PlayerChatManager {
    private val partyMessagePattern =
        "§9P(?:arty)? §8> §.(?:\\[.*] )?(?<author>\\w+)§f: (?:(?:§r)?)+(?<message>.*)".toPattern()
    private val privateMessagePattern = "§d(?<receive>From|To) §r§.(?:.* )?(?<author>\\w+)§r§7: §r(?:§7)?(?<message>.*)".toPattern()
    private val normalMessagePattern = "^§.(?:\\[.*] )?(?<author>\\w+)§.+: (?<message>.*)\$".toPattern()

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

        val chatEvent = ChatReceiveEvent(message, original)
        chatEvent.post()
        event.message = chatEvent.chatComponent

        if (chatEvent.isCanceled) event.isCanceled = true
    }

    @SubscribeEvent
    fun onChat(event: ChatReceiveEvent) {
        partyMessagePattern.matchMatcher(event.message) {
            val author = group("author")
            val message = group("message")
            val chatComponent = event.chatComponent.siblings[1]

            val newEvent = PlayerChatEvent.Party(
                message,
                author,
                chatComponent
            )
            newEvent.post()
            if (newEvent.isCanceled) event.isCanceled = true
        }

        privateMessagePattern.matchMatcher(event.message) {
            val author = group("author")
            val message = group("message")
            val chatComponent = event.chatComponent.siblings[2]
            val isSending = group("receive") == "To"

            val newEvent = PlayerChatEvent.DirectMessage(
                message,
                author,
                chatComponent,
                isSending
            )
            newEvent.post()
            if (newEvent.isCanceled) event.isCanceled = true
        }

        normalMessagePattern.matchMatcher(event.message) {
            val author = group("author")
            val message = group("message")
            val chatComponent = event.chatComponent.siblings[1]

            val newEvent = PlayerChatEvent.Normal(
                message,
                author,
                chatComponent
            )
            newEvent.post()
            if (newEvent.isCanceled) event.isCanceled = true
        }
    }
}
