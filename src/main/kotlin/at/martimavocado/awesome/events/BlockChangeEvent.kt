package at.martimavocado.awesome.events

import at.martimavocado.awesome.utils.BlockUtils.getBlockAt
import at.martimavocado.awesome.utils.BlockUtils.getBlockStateAt
import at.martimavocado.awesome.utils.BlockUtils.toPositionVec
import at.martimavocado.awesome.utils.RegexUtils.matchMatcher
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.Event

class BlockChangeEvent(
    blockPos: BlockPos,
    blockState: IBlockState,
) : Event() {
    val location by lazy { blockPos.toPositionVec() }
    val old by lazy { location.getBlockAt() }
    val oldState by lazy { location.getBlockStateAt() }
    val new: Block by lazy { blockState.block }
    val newState by lazy { blockState }

    companion object {
        private val pattern = "Block\\{minecraft:(?<name>.*)}".toPattern()

        private fun String.getName() =
            pattern.matchMatcher(this) {
                group("name")
            } ?: this
    }
}
