package at.martimavocado.awesome.utils

import net.minecraft.client.Minecraft
import net.minecraft.network.INetHandler
import net.minecraft.network.Packet
import net.minecraft.network.play.client.C01PacketChatMessage
import net.minecraft.util.ChatComponentText

object ChatUtils {

    fun messageToChat(message: String) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(message))
    }
    fun sendChat(message: String) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message)
    }

    fun sendChatPacket(packet: C01PacketChatMessage) {
        Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(packet)
    }
}