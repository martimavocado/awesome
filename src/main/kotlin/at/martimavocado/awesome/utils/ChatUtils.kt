package at.martimavocado.awesome.utils

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.CommandRegistrationEvent
import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.network.play.client.C01PacketChatMessage
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ChatUtils {
    private val config get() = Awesome.config.debug

    fun testMessageCommand(array: Array<String>) {
        if (array.isEmpty()) {
            chat("cant test a message without one i think")
            return
        }
        val hidden = array.last() == "-s"
        var rawMessage = array.toList().joinToString(" ")
        if (!hidden) chat("Testing message: §7$rawMessage")
        if (hidden) rawMessage = rawMessage.replace(" -s", "")
        val formattedMessage = rawMessage.replace("&", "§")
        chat(formattedMessage)
    }

    fun warning(
        message: String,
        usePrefix: Boolean = true,
    ) {
        val prefix = if (usePrefix) "§c[Awesome] " else "§c"

        val finalMessage = prefix + message

        chat(ChatComponentText(finalMessage))
    }

    fun chat(
        message: String,
        usePrefix: Boolean = true,
    ) {
        val prefix = if (usePrefix) "§e[Awesome] " else "§e"

        val finalMessage = prefix + message

        chat(ChatComponentText(finalMessage))
    }

    fun chat(
        nonString: Any,
        usePrefix: Boolean = true,
    ) {
        chat(nonString.toString(), usePrefix)
    }

    fun chat(chatComponent: IChatComponent) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent)
    }

    fun chatClickable(
        message: String,
        command: String,
    ) {
        val text = ChatComponentText(message)
        text.chatStyle.chatClickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
        text.chatStyle.chatHoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText("§eRuns $command"))
        Minecraft.getMinecraft().thePlayer.addChatMessage(text)
    }

    fun sendMessage(message: String) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message)
    }

    fun debug(message: String) {
        if (!config.debugMessages) return

        chat("§7[Awesome Debug] $message", usePrefix = false)
    }

    fun debug(nonString: Any) {
        debug(nonString.toString())
    }

    fun sendChatPacket(packet: C01PacketChatMessage) {
        Minecraft
            .getMinecraft()
            .thePlayer.sendQueue
            .addToSendQueue(packet)
    }

    @SubscribeEvent
    fun onCommandRegistration(event: CommandRegistrationEvent) {
        event.register("awtestmessage") {
            description = "Prints a message in chat."
            callback { testMessageCommand(it) }
        }
    }
}
