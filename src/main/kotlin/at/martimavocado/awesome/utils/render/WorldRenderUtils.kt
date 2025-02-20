package at.martimavocado.awesome.utils.render

import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.events.render.WorldRenderEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import org.lwjgl.opengl.GL11
import java.awt.Color
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

    fun WorldRenderEvent.drawString(
        location: PositionVec? = null,
        entity: Entity? = null,
        text: String,
        seeThroughBlocks: Boolean = false,
        color: Color? = null,
        partialTicks: Float,
        offset: PositionVec? = null,
    ) {
        val viewer = Minecraft.getMinecraft().renderViewEntity ?: return
        GlStateManager.alphaFunc(516, 0.1f)
        GlStateManager.pushMatrix()
        val renderManager = Minecraft.getMinecraft().renderManager

        val interpX = viewer.prevPosX + (viewer.posX - viewer.prevPosX) * partialTicks
        val interpY = viewer.prevPosY + (viewer.posY - viewer.prevPosY) * partialTicks
        val interpZ = viewer.prevPosZ + (viewer.posZ - viewer.prevPosZ) * partialTicks

        var (x, y, z) =
            if (entity != null) {
                val smoothX = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks
                val smoothY = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks
                val smoothZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks
                Triple(smoothX - interpX, smoothY - interpY - viewer.eyeHeight + entity.height + 0.5, smoothZ - interpZ)
            } else if (location != null) {
                Triple(location.x - interpX, location.y - interpY - viewer.eyeHeight, location.z - interpZ)
            } else {
                return
            }

        if (offset != null) {
            x += offset.x
            y += offset.y
            z += offset.z
        }

        val distSq = x * x + y * y + z * z
        val dist = sqrt(distSq)
        var modX = x
        var modY = y
        var modZ = z
        if (distSq > 144) {
            modX *= 12 / dist
            modY *= 12 / dist
            modZ *= 12 / dist
        }

        if (seeThroughBlocks) {
            GlStateManager.disableDepth()
            GlStateManager.disableCull()
        }

        GlStateManager.translate(modX, modY, modZ)
        GlStateManager.translate(0f, viewer.eyeHeight, 0f)
        drawNametag(text, color)
        GlStateManager.rotate(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        GlStateManager.translate(0f, -0.25f, 0f)
        GlStateManager.rotate(-renderManager.playerViewX, 1.0f, 0.0f, 0.0f)
        GlStateManager.rotate(renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.popMatrix()
        GlStateManager.disableLighting()

        if (seeThroughBlocks) {
            GlStateManager.enableDepth()
            GlStateManager.enableCull()
        }
    }

    /**
     * @author Mojang
     */
    fun drawNametag(
        str: String,
        color: Color?,
    ) {
        val fontRenderer = Minecraft.getMinecraft().fontRendererObj
        val f1 = 0.02666667f
        GlStateManager.pushMatrix()
        GL11.glNormal3f(0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(-Minecraft.getMinecraft().renderManager.playerViewY, 0.0f, 1.0f, 0.0f)
        GlStateManager.rotate(
            Minecraft.getMinecraft().renderManager.playerViewX,
            1.0f,
            0.0f,
            0.0f,
        )
        GlStateManager.scale(-f1, -f1, f1)
        GlStateManager.disableLighting()
        GlStateManager.depthMask(false)
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
        val tessellator = Tessellator.getInstance()
        val worldrenderer = tessellator.worldRenderer
        val i = 0
        val j = fontRenderer.getStringWidth(str) / 2
        GlStateManager.disableTexture2D()
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR)
        worldrenderer.pos((-j - 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        worldrenderer.pos((-j - 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        worldrenderer.pos((j + 1).toDouble(), (8 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        worldrenderer.pos((j + 1).toDouble(), (-1 + i).toDouble(), 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        val colorCode = color?.rgb ?: 553648127
        fontRenderer.drawString(str, -j, i, colorCode)
        GlStateManager.depthMask(true)
        fontRenderer.drawString(str, -j, i, -1)
        GlStateManager.enableBlend()
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f)
        GlStateManager.popMatrix()
    }
}
