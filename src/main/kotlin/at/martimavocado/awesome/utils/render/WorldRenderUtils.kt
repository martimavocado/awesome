package at.martimavocado.awesome.utils.render

import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.events.render.WorldRenderEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import org.lwjgl.opengl.GL11
import kotlin.math.sqrt

object WorldRenderUtils {
    fun WorldRenderEvent.drawDynamicText(
        location: PositionVec,
        text: String,
        scaleMultiplier: Double,
        yOff: Float = 0f,
        hideTooCloseAt: Double = 4.5,
        smallestDistanceVew: Double = 5.0,
        ignoreBlocks: Boolean = true,
        ignoreY: Boolean = false,
        maxDistance: Int? = null,
    ) {
        val viewer = Minecraft.getMinecraft().renderViewEntity ?: return
        val thePlayer = Minecraft.getMinecraft().thePlayer ?: return

        val x = location.x
        val y = location.y
        val z = location.z

        val renderOffsetX = viewer.lastTickPosX + (viewer.posX - viewer.lastTickPosX) * partialTicks
        val renderOffsetY = viewer.lastTickPosY + (viewer.posY - viewer.lastTickPosY) * partialTicks
        val renderOffsetZ = viewer.lastTickPosZ + (viewer.posZ - viewer.lastTickPosZ) * partialTicks
        val eyeHeight = thePlayer.eyeHeight

        val dX = (x - renderOffsetX) * (x - renderOffsetX)
        val dY = (y - (renderOffsetY + eyeHeight)) * (y - (renderOffsetY + eyeHeight))
        val dZ = (z - renderOffsetZ) * (z - renderOffsetZ)
        val distToPlayerSq = dX + dY + dZ
        var distToPlayer = sqrt(distToPlayerSq)
        // TODO this is optional maybe?
        distToPlayer = distToPlayer.coerceAtLeast(smallestDistanceVew)

        if (distToPlayer < hideTooCloseAt) return
        maxDistance?.let {
            if (ignoreBlocks && distToPlayer > it) return
        }

        val distRender = distToPlayer.coerceAtMost(50.0)

        var scale = distRender / 12
        scale *= scaleMultiplier

        val resultX = renderOffsetX + (x + 0.5 - renderOffsetX) / (distToPlayer / distRender)
        val resultY =
            if (ignoreY) {
                y * distToPlayer / distRender
            } else {
                renderOffsetY + eyeHeight +
                    (y + 20 * distToPlayer / 300 - (renderOffsetY + eyeHeight)) / (distToPlayer / distRender)
            }
        val resultZ = renderOffsetZ + (z + 0.5 - renderOffsetZ) / (distToPlayer / distRender)

        val renderLocation = PositionVec(resultX, resultY, resultZ)

        render(renderLocation, "§f$text", scale, !ignoreBlocks, true, yOff)
    }

    private fun render(
        location: PositionVec,
        text: String,
        scale: Double,
        depthTest: Boolean,
        shadow: Boolean,
        yOff: Float,
    ) {
        if (!depthTest) {
            GL11.glDisable(GL11.GL_DEPTH_TEST)
            GL11.glDepthMask(false)
        }
        GlStateManager.pushMatrix()
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)

        val minecraft = Minecraft.getMinecraft()
        val fontRenderer = minecraft.fontRendererObj
        val renderManager = minecraft.renderManager

        GlStateManager.translate(
            location.x - renderManager.viewerPosX,
            location.y - renderManager.viewerPosY,
            location.z - renderManager.viewerPosZ,
        )
        GlStateManager.color(1f, 1f, 1f, 0.5f)
        GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        GlStateManager.scale(-scale / 25, -scale / 25, scale / 25)
        val stringWidth = fontRenderer.getStringWidth(text)
        if (shadow) {
            fontRenderer.drawStringWithShadow(
                text,
                (-stringWidth / 2).toFloat(),
                yOff,
                0,
            )
        } else {
            fontRenderer.drawString(
                text,
                -stringWidth / 2,
                0,
                0,
            )
        }
        GlStateManager.color(1f, 1f, 1f)
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
        if (!depthTest) {
            GL11.glEnable(GL11.GL_DEPTH_TEST)
            GL11.glDepthMask(true)
        }
    }
}
