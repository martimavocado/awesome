package at.martimavocado.awesome.utils.render

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import org.lwjgl.opengl.GL11

object GuiRenderUtils {
    private val mc get() = Minecraft.getMinecraft()

    private fun drawStringCentered(
        str: String?,
        fr: FontRenderer,
        x: Float,
        y: Float,
    ) {
        val strLen = fr.getStringWidth(str)
        val x2 = x - strLen / 2f
        val y2 = y - fr.FONT_HEIGHT / 2f
        GL11.glTranslatef(x2, y2, 0f)
        fr.drawString(str, 0f, 0f, 16777215, true)
        GL11.glTranslatef(-x2, -y2, 0f)
    }

    fun drawStringCentered(
        str: String?,
        x: Int,
        y: Int,
    ) {
        drawStringCentered(str, mc.fontRendererObj, x.toFloat(), y.toFloat())
    }

    fun isPointInRect(
        x: Int,
        y: Int,
        left: Int,
        top: Int,
        width: Int,
        height: Int,
    ): Boolean {
        val inX = x in left..width
        val inY = y in top..height

        return inX && inY
    }
}
