package at.martimavocado.awesome.utils.render

import net.minecraft.client.Minecraft
import java.awt.Color

data class Position(
    val x: Int,
    val y: Int,
) {
    fun renderString(
        string: String,
    ) {
        if (string.isBlank()) return
        val fontRenderer = Minecraft.getMinecraft().fontRendererObj

        fontRenderer.drawString(string, x, y, Color.WHITE.rgb)
    }
}
