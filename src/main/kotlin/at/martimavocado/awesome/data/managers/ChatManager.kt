package at.martimavocado.awesome.data.managers

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.chat.ActionBarEvent
import at.martimavocado.awesome.events.chat.ChatReceiveEvent
import at.martimavocado.awesome.events.chat.PlayerChatEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.EventUtils.cancel
import at.martimavocado.awesome.utils.EventUtils.post
import at.martimavocado.awesome.utils.RegexUtils.findMatcher
import at.martimavocado.awesome.utils.RegexUtils.matchMatcher
import at.martimavocado.awesome.utils.StringUtils.cleanupColors
import at.martimavocado.awesome.utils.system.AwesomeLogger
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Pattern

@LoadModule
object ChatManager {
    private val config get() = Awesome.Companion.config.debug
    private val chatLogger = AwesomeLogger("messages/chat")
    private val barLogger = AwesomeLogger("messages/actionbar")

    private val partyMessagePattern =
        "§9P(?:arty)? §8> §.(?:\\[.*] )?(?<author>\\w+)§f: (?:(?:§r)?)+(?<message>.*)".toPattern()
    private val privateMessagePattern =
        "§d(?<receive>From|To) §r§.(?:.* )?(?<author>\\w+)§r§7: §r(?:§7)?(?<message>.*)".toPattern()
    private val normalMessagePattern = "§.(?:\\[.*] )?(?<author>\\w+)§.+: (?<message>.*)\$".toPattern()

    @SubscribeEvent(receiveCanceled = true)
    fun onChatReceive(event: ClientChatReceivedEvent) {
        val original = event.message
        var message = original.formattedText

        message = message.cleanupColors()

        if (event.type.toInt() != 2) {
            val chatEvent = ChatReceiveEvent(message, original)
            chatEvent.post()
            event.message = chatEvent.chatComponent

            if (chatEvent.isCanceled) event.cancel()
        } else {
            val actionBarEvent = ActionBarEvent(message, original)
            actionBarEvent.post()
            event.message = actionBarEvent.chatComponent
            printDebugActionBar(actionBarEvent)

            if (actionBarEvent.isCanceled) event.cancel()
        }
    }

    @SubscribeEvent
    fun onChat(event: ChatReceiveEvent) {
        printDebugMessage(event)

        try {
            handleChatEvent(event, partyMessagePattern, 1) { message, author, chatComponent ->
                PlayerChatEvent.Party(
                    message,
                    author,
                    chatComponent,
                )
            }

            privateMessagePattern.matchMatcher(event.message) {
                handleChatEvent(event, privateMessagePattern, 2) { message, author, chatComponent ->
                    PlayerChatEvent.DirectMessage(
                        message,
                        author,
                        chatComponent,
                        group("receive") == "To",
                    )
                }
            }

            handleChatEvent(event, normalMessagePattern, 1) { message, author, chatComponent ->
                PlayerChatEvent.Normal(
                    message,
                    author,
                    chatComponent,
                )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun handleChatEvent(
        event: ChatReceiveEvent,
        pattern: Pattern,
        dropCount: Int,
        eventCreator: (String, String, List<IChatComponent>) -> PlayerChatEvent,
    ) {
        pattern.findMatcher(event.message) {
            val author = group("author")
            val message = group("message")
            var chatComponent =
                event.chatComponent.siblings
                    .toList()
                    .drop(dropCount)
                    .toMutableList()
            chatComponent.getOrNull(0) ?: return

            val needsCleanup =
                chatComponent[0].formattedText.startsWith("§f: ") || chatComponent[0].formattedText.startsWith("§7: ")
            val isGray = needsCleanup && chatComponent[0].formattedText[1] == '7'

            if (needsCleanup) chatComponent[0] = ChatComponentText("§f" + chatComponent[0].formattedText.drop(4))

            val newEvent = eventCreator(message, author, chatComponent.toList())
            newEvent.post()
            if (newEvent.isCanceled) event.cancel()

            val newComponents = newEvent.chatComponent.toMutableList()
            if (needsCleanup) {
                val string = if (isGray) "§7: " else "§f: "
                val component =
                    if (newComponents[0].formattedText.startsWith("§f")) {
                        newComponents[0].formattedText.drop(2)
                    } else {
                        newComponents[0].formattedText
                    }
                newComponents[0] = ChatComponentText(string + component)
            }

            val oldComponent = event.chatComponent.siblings[0]

            event.chatComponent.siblings.clear()
            event.chatComponent.siblings.addAll(oldComponent + newComponents)
        }
    }

    private fun printDebugMessage(event: ChatReceiveEvent) {
        if (config.printMessages) chatLogger.log("'${event.message}'")
        if (config.printChatComponents) chatLogger.log("'${event.chatComponent}'")
    }

    private fun printDebugActionBar(event: ActionBarEvent) {
        if (config.printMessages) barLogger.log("'${event.message}'")
        if (config.printChatComponents) barLogger.log("'${event.chatComponent}'")
    }
}
