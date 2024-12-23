package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.events.BlockChangeEvent
import at.martimavocado.awesome.events.WorldRenderEvent
import at.martimavocado.awesome.events.chat.ChatReceiveEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.BlockUtils.getBlockAt
import at.martimavocado.awesome.utils.ColorUtils.toColor
import at.martimavocado.awesome.utils.RenderUtils.highlightBlock
import at.martimavocado.awesome.utils.StringUtils.matches
import net.minecraft.block.BlockColored
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

@LoadModule
object HighlightMagicWool {
    private val config get() = Awesome.config.sheepWars.magicWool
    private var magicWoolLocation: PositionVec? = null
    private var magicWoolColor: Color? = null

    private val magicWoolHitPattern = "^§5§lMAGIC WOOL!.*\$".toPattern()

    @SubscribeEvent
    fun onBlockChange(event: BlockChangeEvent) {
        if (!isEnabled()) return
        if (event.old != Blocks.air) return
        if (event.new != Blocks.wool) return

        if (!checkSurroundingBlocks(event.location)) return

        magicWoolLocation = event.location
        magicWoolColor = event.newState.getValue(BlockColored.COLOR).toColor()
    }

    private fun checkSurroundingBlocks(blockPosition: PositionVec): Boolean {
        return blockPosition.add(x = 1).getBlockAt() == Blocks.air
                && blockPosition.add(x = -1).getBlockAt() == Blocks.air
                && (blockPosition.add(y = 1).getBlockAt() == Blocks.air || blockPosition.add(y = 1).getBlockAt() == Blocks.fire)
                && blockPosition.add(y = -1).getBlockAt() == Blocks.air
                && blockPosition.add(z = 1).getBlockAt() == Blocks.air
                && blockPosition.add(z = -1).getBlockAt() == Blocks.air
    }

    @SubscribeEvent
    fun onChat(event: ChatReceiveEvent) {
        if (!isEnabled()) return
        if (!magicWoolHitPattern.matches(event.message)) return

        magicWoolLocation = null
        magicWoolColor = null
    }

    @SubscribeEvent
    fun onRender(event: WorldRenderEvent) {
        if (!isEnabled()) return

        val location = magicWoolLocation ?: return
        val color: Color = if (config.colorMatch) magicWoolColor ?: return
                        else config.color.toColor()

        event.highlightBlock(
            location,
            color,
            config.beacon,
            config.beacon,
            thickness = 1.0f
        )
    }

    private fun isEnabled() = HypixelGame.SHEEP_WARS.isPlaying() && config.enabled
}