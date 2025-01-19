package at.martimavocado.awesome.utils.render

import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.events.render.GuiOverlayRenderEvent
import at.martimavocado.awesome.events.render.WorldRenderEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.EntityUtils.getLocation
import at.martimavocado.awesome.utils.EventUtils.post
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.Entity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.cos
import kotlin.math.sin

@LoadModule
object RenderUtils {
    private val beaconBeam = ResourceLocation("textures/entity/beacon_beam.png")

    private val mc get() = Minecraft.getMinecraft()

    private fun canRender() = mc.fontRendererObj != null

    @SubscribeEvent
    fun onRenderWorld(event: RenderWorldLastEvent) {
        if (!canRender()) return
        WorldRenderEvent(event.partialTicks).post()
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderGameOverlayEvent.Pre) {
        if (!canRender()) return
        if (event.type != RenderGameOverlayEvent.ElementType.HOTBAR) return

        GlStateManager.translate(0f, 0f, -3f)
        GuiOverlayRenderEvent(event.partialTicks).post()
        GlStateManager.translate(0f, 0f, 3f)
    }

    fun WorldRenderEvent.drawWaypointFilled(
        location: PositionVec,
        color: Color,
        seeThroughBlocks: Boolean = false,
        beacon: Boolean = false,
        extraSize: Double = 0.0,
        extraSizeTopY: Double = extraSize,
        extraSizeBottomY: Double = extraSize,
        minimumAlpha: Float = 0.2f,
        inverseAlphaScale: Boolean = false,
    ) {
        val (viewerX, viewerY, viewerZ) = getViewerPos(partialTicks)
        val x = location.x - viewerX
        val y = location.y - viewerY
        val z = location.z - viewerZ
        val distSq = x * x + y * y + z * z

        if (seeThroughBlocks) {
            GlStateManager.disableDepth()
        }

        GlStateManager.disableCull()
        drawFilledBoundingBox(
            AxisAlignedBB(
                x - extraSize,
                y - extraSizeBottomY,
                z - extraSize,
                x + 1 + extraSize,
                y + 1 + extraSizeTopY,
                z + 1 + extraSize,
            ).expandBlock(),
            color,
            if (inverseAlphaScale) {
                (1.0f - 0.005f * distSq.toFloat()).coerceAtLeast(minimumAlpha)
            } else {
                (0.1f + 0.005f * distSq.toFloat()).coerceAtLeast(minimumAlpha)
            },
        )
        GlStateManager.disableTexture2D()
        if (distSq > 5 * 5 && beacon) renderBeaconBeam(x, y + 1, z, color.rgb, 1.0f, partialTicks)
        GlStateManager.disableLighting()
        GlStateManager.enableTexture2D()
        GlStateManager.enableCull()

        if (seeThroughBlocks) {
            GlStateManager.enableDepth()
        }
    }

    fun WorldRenderEvent.highlightBlock(
        location: PositionVec,
        color: Color,
        seeThroughBlocks: Boolean = false,
        beaconAbove: Boolean = false,
        beaconBelow: Boolean = false,
        thickness: Float = 1.0f,
        extraSize: Double = 0.0,
        extraSizeTopY: Double = extraSize,
        extraSizeBottomY: Double = extraSize,
        minimumAlpha: Float = 0.2f,
        inverseAlphaScale: Boolean = false,
    ) {
        val (viewerX, viewerY, viewerZ) = getViewerPos(partialTicks)
        val x = location.x - viewerX
        val y = location.y - viewerY
        val z = location.z - viewerZ
        val distSq = x * x + y * y + z * z

        if (seeThroughBlocks) {
            GlStateManager.disableDepth()
        }

        val alpha =
            if (inverseAlphaScale) {
                (1.0f - 0.005f * distSq.toFloat()).coerceAtLeast(minimumAlpha)
            } else {
                (0.1f + 0.005f * distSq.toFloat()).coerceAtLeast(minimumAlpha)
            }

        GlStateManager.disableCull()
        drawBoundingBox(
            AxisAlignedBB(
                x - extraSize,
                y - extraSizeBottomY,
                z - extraSize,
                x + 1 + extraSize,
                y + 1 + extraSizeTopY,
                z + 1 + extraSize,
            ).expandBlock(),
            color,
            alpha,
            thickness,
        )
        GlStateManager.disableTexture2D()
        if ((beaconAbove || beaconBelow)) {
            renderBeaconBeam(x, y, z, color.rgb, alpha, partialTicks, beaconAbove, beaconBelow)
        }
        GlStateManager.disableLighting()
        GlStateManager.enableTexture2D()
        GlStateManager.enableCull()

        if (seeThroughBlocks) {
            GlStateManager.enableDepth()
        }
    }

    fun WorldRenderEvent.drawFilledBoundingBoxNea(
        aabb: AxisAlignedBB,
        c: Color,
        alphaMultiplier: Float = 1f,
        /**
         * If set to `true`, renders the box relative to the camera instead of relative to the world.
         * If set to `false`, will be relativized to [RenderUtils.getViewerPos].
         */
        renderRelativeToCamera: Boolean = false,
        drawVerticalBarriers: Boolean = true,
    ) {
        drawFilledBoundingBoxNea(aabb, c, alphaMultiplier, renderRelativeToCamera, drawVerticalBarriers, partialTicks)
    }

    fun drawFilledBoundingBoxNea(
        aabb: AxisAlignedBB,
        c: Color,
        alphaMultiplier: Float = 1f,
        /**
         * If set to `true`, renders the box relative to the camera instead of relative to the world.
         * If set to `false`, will be relativized to [RenderUtils.getViewerPos]. Setting this to `false` requires
         * specifying [partialTicks]]
         */
        renderRelativeToCamera: Boolean = true,
        drawVerticalBarriers: Boolean = true,
        partialTicks: Float = 0F,
    ) {
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
        GlStateManager.disableTexture2D()
        GlStateManager.disableCull()
        val effectiveAABB =
            if (!renderRelativeToCamera) {
                val vp = getViewerPos(partialTicks)
                AxisAlignedBB(
                    aabb.minX - vp.x,
                    aabb.minY - vp.y,
                    aabb.minZ - vp.z,
                    aabb.maxX - vp.x,
                    aabb.maxY - vp.y,
                    aabb.maxZ - vp.z,
                )
            } else {
                aabb
            }
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        // vertical
        if (drawVerticalBarriers) {
            GlStateManager.color(c.red / 255f, c.green / 255f, c.blue / 255f, c.alpha / 255f * alphaMultiplier)
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
            with(effectiveAABB) {
                worldRenderer.pos(minX, minY, minZ).endVertex()
                worldRenderer.pos(maxX, minY, minZ).endVertex()
                worldRenderer.pos(maxX, minY, maxZ).endVertex()
                worldRenderer.pos(minX, minY, maxZ).endVertex()
                tessellator.draw()
                worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
                worldRenderer.pos(minX, maxY, maxZ).endVertex()
                worldRenderer.pos(maxX, maxY, maxZ).endVertex()
                worldRenderer.pos(maxX, maxY, minZ).endVertex()
                worldRenderer.pos(minX, maxY, minZ).endVertex()
                tessellator.draw()
            }
        }
        GlStateManager.color(
            c.red / 255f * 0.8f,
            c.green / 255f * 0.8f,
            c.blue / 255f * 0.8f,
            c.alpha / 255f * alphaMultiplier,
        )

        // x
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        with(effectiveAABB) {
            worldRenderer.pos(minX, minY, maxZ).endVertex()
            worldRenderer.pos(minX, maxY, maxZ).endVertex()
            worldRenderer.pos(minX, maxY, minZ).endVertex()
            worldRenderer.pos(minX, minY, minZ).endVertex()
            tessellator.draw()
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
            worldRenderer.pos(maxX, minY, minZ).endVertex()
            worldRenderer.pos(maxX, maxY, minZ).endVertex()
            worldRenderer.pos(maxX, maxY, maxZ).endVertex()
            worldRenderer.pos(maxX, minY, maxZ).endVertex()
        }
        tessellator.draw()
        GlStateManager.color(
            c.red / 255f * 0.9f,
            c.green / 255f * 0.9f,
            c.blue / 255f * 0.9f,
            c.alpha / 255f * alphaMultiplier,
        )
        // z
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        with(effectiveAABB) {
            worldRenderer.pos(minX, maxY, minZ).endVertex()
            worldRenderer.pos(maxX, maxY, minZ).endVertex()
            worldRenderer.pos(maxX, minY, minZ).endVertex()
            worldRenderer.pos(minX, minY, minZ).endVertex()
            tessellator.draw()
            worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
            worldRenderer.pos(minX, minY, maxZ).endVertex()
            worldRenderer.pos(maxX, minY, maxZ).endVertex()
            worldRenderer.pos(maxX, maxY, maxZ).endVertex()
            worldRenderer.pos(minX, maxY, maxZ).endVertex()
        }
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GlStateManager.enableCull()
        GlStateManager.disableBlend()
    }

    fun AxisAlignedBB.expandBlock(n: Int = 1): AxisAlignedBB {
        val vec = PositionVec.expandVector * n
        return expand(vec.x, vec.y, vec.z)
    }

    fun AxisAlignedBB.inflate(n: Int = 1): AxisAlignedBB {
        val vec = PositionVec.expandVector * -n
        return expand(vec.x, vec.y, vec.z)
    }

    fun getViewerPos(partialTicks: Float) = mc.renderViewEntity?.let { exactLocation(it, partialTicks) } ?: PositionVec()

    fun exactLocation(
        entity: Entity,
        partialTicks: Float,
    ): PositionVec {
        if (entity.isDead) return entity.getLocation()
        val x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks
        val y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks
        val z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks
        return PositionVec(x, y, z)
    }

    fun drawFilledBoundingBox(
        aabb: AxisAlignedBB,
        c: Color,
        alphaMultiplier: Float = 1f,
    ) {
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
        GlStateManager.disableTexture2D()
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer
        GlStateManager.color(c.red / 255f, c.green / 255f, c.blue / 255f, c.alpha / 255f * alphaMultiplier)

        // vertical
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        tessellator.draw()
        GlStateManager.color(
            c.red / 255f * 0.8f,
            c.green / 255f * 0.8f,
            c.blue / 255f * 0.8f,
            c.alpha / 255f * alphaMultiplier,
        )

        // x
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        tessellator.draw()
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        tessellator.draw()
        GlStateManager.color(
            c.red / 255f * 0.9f,
            c.green / 255f * 0.9f,
            c.blue / 255f * 0.9f,
            c.alpha / 255f * alphaMultiplier,
        )
        // z
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        tessellator.draw()
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION)
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        tessellator.draw()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    fun drawBoundingBox(
        aabb: AxisAlignedBB,
        c: Color,
        alphaMultiplier: Float = 1f,
        thickness: Float = 1f,
    ) {
        GlStateManager.enableBlend()
        GlStateManager.disableLighting()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)
        GlStateManager.disableTexture2D()
        GL11.glLineWidth(thickness)
        GlStateManager.color(c.red / 255f, c.green / 255f, c.blue / 255f, c.alpha / 255f * alphaMultiplier)

        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer

        // Draw lines for each edge of the bounding box
        worldRenderer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION)

        // Bottom edges
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()

        // Top edges
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()

        // Vertical edges
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.minZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.maxX, aabb.maxY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.minY, aabb.maxZ).endVertex()
        worldRenderer.pos(aabb.minX, aabb.maxY, aabb.maxZ).endVertex()

        tessellator.draw()

        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    private fun renderBeaconBeam(
        x: Double,
        y: Double = 0.0,
        z: Double,
        rgb: Int,
        alpha: Float,
        partialTicks: Float,
        drawAbove: Boolean = true,
        drawBelow: Boolean = false,
    ) {
        val alphaMultiplier = alpha.coerceIn(0f..1f)
        val height = 300
        val bottomOffset = 1
        val tessellator = Tessellator.getInstance()
        val worldRenderer = tessellator.worldRenderer
        mc.textureManager.bindTexture(beaconBeam)
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0f)
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0f)
        GlStateManager.disableLighting()
        GlStateManager.enableCull()
        GlStateManager.enableTexture2D()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, 1, 1, 0)
        GlStateManager.enableBlend()
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0)

        val time = mc.theWorld.totalWorldTime + partialTicks.toDouble()
        val d1 = MathHelper.func_181162_h(-time * 0.2 - MathHelper.floor_double(-time * 0.1).toDouble())
        val r = (rgb shr 16 and 0xFF) / 255f
        val g = (rgb shr 8 and 0xFF) / 255f
        val b = (rgb and 0xFF) / 255f

        fun renderBeamSegment(
            startY: Double,
            endY: Double,
        ) {
            GlStateManager.disableCull()
            val d2 = time * 0.025 * -1.5
            val d4 = 0.5 + cos(d2 + 2.356194490192345) * 0.2
            val d5 = 0.5 + sin(d2 + 2.356194490192345) * 0.2
            val d6 = 0.5 + cos(d2 + Math.PI / 4.0) * 0.2
            val d7 = 0.5 + sin(d2 + Math.PI / 4.0) * 0.2
            val d8 = 0.5 + cos(d2 + 3.9269908169872414) * 0.2
            val d9 = 0.5 + sin(d2 + 3.9269908169872414) * 0.2
            val d10 = 0.5 + cos(d2 + 5.497787143782138) * 0.2
            val d11 = 0.5 + sin(d2 + 5.497787143782138) * 0.2
            val d14 = -1.0 + d1
            val d15 = height.toDouble() * 2.5 + d14
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
            worldRenderer
                .pos(x + d4, startY, z + d5)
                .tex(1.0, d15)
                .color(r, g, b, 1.0f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + d4, endY, z + d5)
                .tex(1.0, d14)
                .color(r, g, b, 1.0f)
                .endVertex()
            worldRenderer
                .pos(x + d6, endY, z + d7)
                .tex(0.0, d14)
                .color(r, g, b, 1.0f)
                .endVertex()
            worldRenderer
                .pos(x + d6, startY, z + d7)
                .tex(0.0, d15)
                .color(r, g, b, 1.0f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + d10, startY, z + d11)
                .tex(1.0, d15)
                .color(r, g, b, 1.0f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + d10, endY, z + d11)
                .tex(1.0, d14)
                .color(r, g, b, 1.0f)
                .endVertex()
            worldRenderer
                .pos(x + d8, endY, z + d9)
                .tex(0.0, d14)
                .color(r, g, b, 1.0f)
                .endVertex()
            worldRenderer
                .pos(x + d8, startY, z + d9)
                .tex(0.0, d15)
                .color(r, g, b, 1.0f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + d6, startY, z + d7)
                .tex(1.0, d15)
                .color(r, g, b, 1.0f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + d6, endY, z + d7)
                .tex(1.0, d14)
                .color(r, g, b, 1.0f)
                .endVertex()
            worldRenderer
                .pos(x + d10, endY, z + d11)
                .tex(0.0, d14)
                .color(r, g, b, 1.0f)
                .endVertex()
            worldRenderer
                .pos(x + d10, startY, z + d11)
                .tex(0.0, d15)
                .color(r, g, b, 1.0f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + d8, startY, z + d9)
                .tex(1.0, d15)
                .color(r, g, b, 1.0f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + d8, endY, z + d9)
                .tex(1.0, d14)
                .color(r, g, b, 1.0f)
                .endVertex()
            worldRenderer
                .pos(x + d4, endY, z + d5)
                .tex(0.0, d14)
                .color(r, g, b, 1.0f)
                .endVertex()
            worldRenderer
                .pos(x + d4, startY, z + d5)
                .tex(0.0, d15)
                .color(r, g, b, 1.0f * alphaMultiplier)
                .endVertex()
            tessellator.draw()
            GlStateManager.disableCull()
            val d12 = -1.0 + d1
            val d13 = height + d12
            worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
            worldRenderer
                .pos(x + 0.2, startY, z + 0.2)
                .tex(1.0, d13)
                .color(r, g, b, 0.25f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + 0.2, endY, z + 0.2)
                .tex(1.0, d12)
                .color(r, g, b, 0.25f)
                .endVertex()
            worldRenderer
                .pos(x + 0.8, endY, z + 0.2)
                .tex(0.0, d12)
                .color(r, g, b, 0.25f)
                .endVertex()
            worldRenderer
                .pos(x + 0.8, startY, z + 0.2)
                .tex(0.0, d13)
                .color(r, g, b, 0.25f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + 0.8, startY, z + 0.8)
                .tex(1.0, d13)
                .color(r, g, b, 0.25f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + 0.8, endY, z + 0.8)
                .tex(1.0, d12)
                .color(r, g, b, 0.25f)
                .endVertex()
            worldRenderer
                .pos(x + 0.2, endY, z + 0.8)
                .tex(0.0, d12)
                .color(r, g, b, 0.25f)
                .endVertex()
            worldRenderer
                .pos(x + 0.2, startY, z + 0.8)
                .tex(0.0, d13)
                .color(r, g, b, 0.25f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + 0.8, startY, z + 0.2)
                .tex(1.0, d13)
                .color(r, g, b, 0.25f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + 0.8, endY, z + 0.2)
                .tex(1.0, d12)
                .color(r, g, b, 0.25f)
                .endVertex()
            worldRenderer
                .pos(x + 0.8, endY, z + 0.8)
                .tex(0.0, d12)
                .color(r, g, b, 0.25f)
                .endVertex()
            worldRenderer
                .pos(x + 0.8, startY, z + 0.8)
                .tex(0.0, d13)
                .color(r, g, b, 0.25f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + 0.2, startY, z + 0.8)
                .tex(1.0, d13)
                .color(r, g, b, 0.25f * alphaMultiplier)
                .endVertex()
            worldRenderer
                .pos(x + 0.2, endY, z + 0.8)
                .tex(1.0, d12)
                .color(r, g, b, 0.25f)
                .endVertex()
            worldRenderer
                .pos(x + 0.2, endY, z + 0.2)
                .tex(0.0, d12)
                .color(r, g, b, 0.25f)
                .endVertex()
            worldRenderer
                .pos(x + 0.2, startY, z + 0.2)
                .tex(0.0, d13)
                .color(r, g, b, 0.25f * alphaMultiplier)
                .endVertex()
            tessellator.draw()
        }

        if (drawAbove) {
            val topOffset = bottomOffset + height
            renderBeamSegment(y + bottomOffset, y + topOffset)
        }

        if (drawBelow) {
            val topOffset = bottomOffset + height
            renderBeamSegment(y - bottomOffset + 1, y - topOffset)
        }

        GlStateManager.disableCull()
    }

    fun WorldRenderEvent.renderBlock(
        blockState: IBlockState,
        location: PositionVec,
        brightness: Float = 0.5f,
        transformations: (() -> Unit)? = null,
    ) {
        val player = mc.thePlayer ?: return
        val playerPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * this.partialTicks
        val playerPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * this.partialTicks
        val playerPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * this.partialTicks

        GlStateManager.pushMatrix()
        GlStateManager.disableLighting()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GlStateManager.depthMask(false)

        GlStateManager.translate(-playerPosX, -playerPosY, -playerPosZ)

        val blockRendererDispatcher = mc.blockRendererDispatcher

        GlStateManager.pushMatrix()
        GlStateManager.translate(location.x, location.y, location.z + 1)

        transformations?.invoke()

        blockRendererDispatcher.renderBlockBrightness(blockState, brightness)

        GlStateManager.popMatrix()

        GlStateManager.depthMask(true)
        GlStateManager.scale(1f, 1f, 1f)
        GlStateManager.enableLighting()
        GlStateManager.disableBlend()
        GlStateManager.popMatrix()
    }

    fun WorldRenderEvent.exactLocation(entity: Entity) = exactLocation(entity, partialTicks)
}
