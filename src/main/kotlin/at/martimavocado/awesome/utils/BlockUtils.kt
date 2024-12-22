package at.martimavocado.awesome.utils

import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.events.BlockChangeEvent
import at.martimavocado.awesome.events.PacketReceivedEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.OtherUtils.post
import net.minecraft.block.Block
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.network.play.server.S22PacketMultiBlockChange
import net.minecraft.network.play.server.S23PacketBlockChange
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object BlockUtils {
    private val world get() = Minecraft.getMinecraft().theWorld

    fun BlockPos.toPositionVec() = PositionVec(this.x, this.y, this.z)

    fun PositionVec.getBlockAt(): Block = getBlockStateAt().block
    fun PositionVec.getBlockStateAt(): IBlockState = world.getBlockState(toBlockPos())

    @SubscribeEvent
    fun onBlockReceivePacket(event: PacketReceivedEvent) {
        when (event.packet) {
            is S23PacketBlockChange -> {
                val blockPos = event.packet.blockPosition ?: return
                val blockState = event.packet.blockState ?: return

                BlockChangeEvent(blockPos, blockState).post()
            }

            is S22PacketMultiBlockChange -> {
                event.packet.changedBlocks.forEach {
                    BlockChangeEvent(it.pos, it.blockState).post()
                }
            }
        }

        if (event.packet is S23PacketBlockChange) {
            val blockPos = event.packet.blockPosition ?: return
            val blockState = event.packet.blockState ?: return
            BlockChangeEvent(blockPos, blockState).post()
        } else if (event.packet is S22PacketMultiBlockChange) {
            for (block in event.packet.changedBlocks) {
                BlockChangeEvent(block.pos, block.blockState).post()
            }
        }
    }
}