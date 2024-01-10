package at.martimavocado.awesome.features.commands

import at.martimavocado.awesome.features.FakeBan
import at.martimavocado.awesome.utils.ChatUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

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
        if ((message.startsWith("§r§9Party §8>")) || (message.startsWith("§9Party §8>"))) {
            val index = formattedMessage.indexOf("§f: §r")+6
            val ign = formattedMessage.replace("§r","").split(" ").toTypedArray().firstOrNull { it.contains(":") }
                ?.substringBefore("§f:")
                ?.trim()
            if (formattedMessage[index] == '?') {
                val substring = formattedMessage.substring(index).replace("§r","")
                if (substring.startsWith("?aw")) {
                    val array = substring.split(" ").toTypedArray()
                    if (config.enabled && ign != null) { handleCommand(array, ign) }
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
            "say" -> if (config.say) sayMessage(newArray, ign)
            "ban" -> if (config.ban) showBanScreen(newArray)
            else -> ChatUtils.sendChatClient("Tried running unknown command! ${array[1]}")
        }
    }

    private fun showBanScreen(array: List<String>) {
        if (array.isEmpty()) FakeBan.showBanScreen()
        if (array.isNotEmpty()) {
            val newArray = array.joinToString(" ").split(" (?=([^']*'[^']*')*[^']*\$)".toRegex()).map { it.replace("'", "") }
//            println(newArray)
            if (newArray.size != 2) {
                FakeBan.showBanScreen()
            } else {
                FakeBan.showBanScreenArguments(newArray[1], newArray[0])
            }
        }
    }

    private fun sayMessage(newArray: List<String>, ign: String) {
        val message = newArray.joinToString(" ").replace("\$ign", ign)
        ChatUtils.sendChat(message)
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