package at.martimavocado.awesome.events.chat

import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.OtherUtils.matchMatcher
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object PlayerChatManager {
    private val partyMessagePattern = "§9P(?:arty)? §8> §.(?:\\[(?:MVP|VIP)?(?:§.\\+§.)?] )?(?<author>\\w+)§f: (?:(?:§r)?)+(?<message>.*)".toPattern()
    private val privateMessagePattern = "§dFrom §r§.(?:.* )?(?<author>\\w+)§r§7: §r(?:§7)?(?<message>.*)".toPattern()

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
        val privateCanceled = handlePrivateChat(message)

        if (newEvent.isCanceled || partyCanceled || privateCanceled) event.isCanceled = true
    }

    private fun handlePartyChat(rawMessage: String): Boolean {
        var isCanceled = false
        partyMessagePattern.matchMatcher(rawMessage) {
            val message = group("message")
            val author = group("author")

            val event = PartyChatEvent(message, author)
            MinecraftForge.EVENT_BUS.post(event)
            isCanceled = event.isCanceled
        }
        return isCanceled
    }

    private fun handlePrivateChat(rawMessage: String): Boolean {
        var isCanceled = false
        privateMessagePattern.matchMatcher(rawMessage) {
            val message = group("message")
            val author = group("author")

            val event = PrivateChatEvent(message, author)
            MinecraftForge.EVENT_BUS.post(event)
            isCanceled = event.isCanceled
        }
        return isCanceled
    }
}