package at.martimavocado.awesome.features.commands

import at.martimavocado.awesome.features.FakeBan
import at.martimavocado.awesome.utils.ChatUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import tv.twitch.chat.Chat

class ChatCommands {
    private var isLeader = false
    private var myIGN = "null"
    private val config get() = at.martimavocado.awesome.Awesome.config.commands

    @SubscribeEvent
    fun onChatReceived(event: ClientChatReceivedEvent) {
        val message = event.message.unformattedText
        val formattedMessage = event.message.formattedText
        myIGN = Minecraft.getMinecraft().thePlayer.name
        handlePartyTransferMessages(event.message)
        if ((message.startsWith("§r§9Party §8>")) || (message.startsWith("§9P §8>"))) {
            val index = message.indexOf("§f: §r")+6
            val ign = message.replace("§r","").split(" ").toTypedArray().firstOrNull { it.contains(":") }
                ?.substringBefore("§f:")
                ?.trim()
            if (message[index] == '?') {
                val substring = message.substring(index).replace("§r","")
                if (substring.startsWith("?aw")) {
                    val array = substring.split(" ").toTypedArray()
                    if (ign != null && config.enabled) {
                        handleCommand(array, ign)
                    }
                }
            }
        }
    } //§eThe party was transferred to §r§b[MVP§r§5+§r§b] martimavocado §r§eby §r§a[VIP] mcavaco§r

    private fun handleCommand(array: Array<String>, ign: String) {
        val newArray = array.drop(2)
        if (ign == myIGN) return
        when (array[1]) {
            "warp" -> if (config.warping) warpParty()
            "transfer" -> if (config.transfer) transferParty(ign)
            "say" -> if (config.say) sayMessage(newArray)
            "ban" -> if (config.ban) showBanScreen(newArray)
            else -> ChatUtils.sendChatClient("Tried running unknown command! ${array[1]}")
        }
    }

    private fun showBanScreen(array: List<String>) {
        if (array.isEmpty()) FakeBan.showBanScreen()
        if (array.isNotEmpty()) {
            if (array.size != 2) {
                FakeBan.showBanScreen()
            } else FakeBan.showBanScreen(array[1], array[0])
        }
    }

    private fun sayMessage(newArray: List<String>) {
        ChatUtils.sendChat(newArray.joinToString(" "))
    }

    private fun warpParty() {
        if (isLeader) {
            ChatUtils.sendChat("/p warp")
        }
    }

    private fun transferParty(ign: String) {
        if (isLeader) {
            ChatUtils.sendChat("/p transfer $ign")
            isLeader = false
        }
    }

    private fun handlePartyTransferMessages(oldMessage: IChatComponent) {
        val message = oldMessage.unformattedText
        val formattedMessage = oldMessage.formattedText
        if (message.startsWith("You are not this party's leader!")) {
            isLeader = false
        }
        if (formattedMessage.startsWith("§eYou summoned")) {
            isLeader = true
        }
        if (formattedMessage.startsWith("§eThe party was transferred to")) {
            val words = message.split(" ")
            val byIndex = words.indexOf("by")
            isLeader = words[byIndex-1] == myIGN
        }
    }
}