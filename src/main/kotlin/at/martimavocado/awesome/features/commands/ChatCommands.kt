package at.martimavocado.awesome.features.commands

import at.martimavocado.awesome.events.chat.PartyChatEvent
import at.martimavocado.awesome.events.chat.PrivateChatEvent
import at.martimavocado.awesome.features.FakeBan
import at.martimavocado.awesome.utils.ChatUtils
import at.martimavocado.awesome.utils.PlayerUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ChatCommands {
    private var isLeader = false
    private var myIGN: String? = null
    private val config get() = at.martimavocado.awesome.Awesome.config.commands

    @SubscribeEvent
    fun onPartyChat(event: PartyChatEvent) {
        if (event.author == PlayerUtils.playerIGN) return

        val messageArray = event.message.split(" ").toTypedArray()
        if (messageArray[0] != "?aw") return

        if (config.enabled) handleCommand(messageArray, event.author)
    }

    @SubscribeEvent
    fun onPrivateChat(event: PrivateChatEvent) {
        if (event.author != "martimavocado") return

        val messageArray = event.message.split(" ").toTypedArray()
        if (messageArray[0] != "?aw") return

        if (config.enabled) handleCommand(messageArray, event.author, true)
    }

    private fun handleCommand(array: Array<String>, ign: String, isDM: Boolean = false) {
        val newArray = array.drop(2).toTypedArray()
        val command = array[1]
        when (command) {
            "warp" -> if (config.warping || isDM) HypixelCommands.partyWarp()
            "transfer" -> if (config.transfer || isDM) HypixelCommands.partyTransfer(ign)
            "say" -> if (config.say || isDM) sayMessage(newArray, ign)
            "ban" -> if (config.ban || isDM) showBanScreen(array.getOrNull(2))
            "hi" -> if (config.hi || isDM) HypixelCommands.sayHi(ign)
            else -> ChatUtils.chat("Tried running unknown command! ${array[1]}")
        }
    }

    private fun showBanScreen(player: String?) {
        if (player == null || player == myIGN) {
            FakeBan.showBanScreen()
        }
    }

    private fun sayMessage(array: Array<String>, ign: String) {
        val message = array.joinToString(" ").replace("\$ign", ign)
        println("i want to send '$message'")
        ChatUtils.sendMessage(message)
    }
}