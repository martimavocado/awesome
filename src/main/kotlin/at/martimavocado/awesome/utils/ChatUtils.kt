package at.martimavocado.awesome.utils

import net.minecraft.client.Minecraft
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.network.play.client.C01PacketChatMessage
import net.minecraft.util.ChatComponentText

object ChatUtils {

    fun sendChatClient(message: String) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(message))
    }

    fun sendChatClientClickable(message: String, command: String) {
        val text = ChatComponentText(message)
        text.chatStyle.chatClickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, command)
        text.chatStyle.chatHoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText("§eExecute $command"))
        Minecraft.getMinecraft().thePlayer.addChatMessage(text)
    }

    fun sendChat(message: String) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message)
    }

    fun debug(message: String) {
        sendChatClient("[Debug] $message")
    }

    fun sendChatPacket(packet: C01PacketChatMessage) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet)
    }

    fun inArray(input: String, array: Array<Pair<String, String>>): Boolean {
        for ((leftHalf, _) in array) {
            if (input.contains(leftHalf)) {
                return true
            }
        }
        return false
    }
}