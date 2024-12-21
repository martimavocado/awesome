package at.martimavocado.awesome.features.commands

import at.martimavocado.awesome.utils.ChatUtils

object HypixelCommands {
    fun sayHi(name: String) {
        ChatUtils.sendMessage("hi $name")
    }

    fun partyWarp() {
        ChatUtils.sendMessage("/p warp")
    }
}