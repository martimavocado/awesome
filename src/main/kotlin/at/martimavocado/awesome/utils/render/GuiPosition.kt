package at.martimavocado.awesome.utils.render

import at.martimavocado.awesome.data.HypixelGame
import net.minecraft.client.Minecraft
import java.awt.Color

data class GuiPosition(
    val x: Int,
    val y: Int,
    val game: HypixelGame?,
) {
    fun renderString(
        string: String,
        dropShadow: Boolean = false,
    ) {
        if (string.isBlank()) return
        if (!(game != null && game == HypixelGame.currentGame)) return

        val fontRenderer = Minecraft.getMinecraft().fontRendererObj

        fontRenderer.drawString(string, x.toFloat(), y.toFloat(), Color.WHITE.rgb, dropShadow)
    }
}
