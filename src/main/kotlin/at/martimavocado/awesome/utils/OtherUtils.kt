package at.martimavocado.awesome.utils

import net.minecraft.client.Minecraft

object OtherUtils {
    private fun showTitle (title: String, subtitle: String, timeFadeIn: Int, displayTime: Int, timeFadeOut: Int) {
        Minecraft.getMinecraft().ingameGUI.displayTitle(title, subtitle, timeFadeIn, displayTime, timeFadeOut)
    }

    fun tryShowTitle (array: Array<String>) {
        if (array.size == 5) {
            val title = array[0]
            val subtitle = array[1]
            val fadeIn = array[2].toIntOrNull() ?: 0
            val stay = array[3].toIntOrNull() ?: 20
            val fadeOut = array[4].toIntOrNull() ?: 0
            showTitle(title, subtitle, fadeIn, stay, fadeOut)
        } else {
            ChatUtils.sendChatClient("Wrong Usage! /showtitle title subtitle fadeIn displayTime fadeOut")
        }
    }

    fun checkIGN(string: String): Boolean {
        return Minecraft.getMinecraft().thePlayer.name == string
    }
}