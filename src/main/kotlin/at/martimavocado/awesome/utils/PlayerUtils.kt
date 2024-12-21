package at.martimavocado.awesome.utils

import net.minecraft.client.Minecraft

object PlayerUtils {

    val playerIGN get() = Minecraft.getMinecraft().thePlayer.name
}