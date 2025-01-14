package at.martimavocado.awesome.features.speedbuilders.data

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.data.PositionVec.Companion.contains
import at.martimavocado.awesome.events.BlockChangeEvent
import at.martimavocado.awesome.events.DebugDataCollectionEvent
import at.martimavocado.awesome.events.WorldChangeEvent
import at.martimavocado.awesome.events.games.speedbuilders.SpeedBuildersPhaseChange
import at.martimavocado.awesome.events.render.WorldRenderEvent
import at.martimavocado.awesome.features.speedbuilders.SpeedBuildersAPI
import at.martimavocado.awesome.features.speedbuilders.SpeedBuildersAPI.GameState
import at.martimavocado.awesome.features.speedbuilders.SpeedBuildersAPI.gameState
import at.martimavocado.awesome.features.speedbuilders.SpeedBuildersAPI.isPlaying
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.BlockUtils.getBlockStateAt
import at.martimavocado.awesome.utils.render.RenderUtils.renderBlock
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Blocks
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object SpeedBuildersPreview {
    private val config get() = Awesome.config.speedBuilders.blockPreview
    private val currentBuild: MutableMap<PositionVec, IBlockState> =
        mutableMapOf<PositionVec, IBlockState>()

    @SubscribeEvent
    fun onWorldRender(event: WorldRenderEvent) {
        if (!config.enabled) return
        if (!isPlaying()) return
        if (gameState !in setOf(GameState.LOOKING_AT_BUILD, GameState.BUILDING)) return

        val build = currentBuild
        if (build.isEmpty()) return

        try {
            for ((location, blockState) in build) {
                val blockAtLocation = location.getBlockStateAt()
                if (blockAtLocation == blockState) continue

                event.renderBlock(blockState, location, config.blockBrightness) {
                    GlStateManager.scale(0.5, 0.5, 0.5)
                    GlStateManager.translate(0.5, 0.5, -0.5)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SubscribeEvent
    fun onBlockChange(event: BlockChangeEvent) {
        if (gameState != GameState.LOOKING_AT_BUILD) return
        val location = event.location
        if (SpeedBuildersAPI.plot?.boundingBox?.contains(location) != true) return

        if (event.old != Blocks.air) return

        currentBuild.put(location, event.newState)
    }

    @SubscribeEvent
    fun onServerChange(event: WorldChangeEvent) {
        currentBuild.clear()
    }

    @SubscribeEvent
    fun onPhaseChange(event: SpeedBuildersPhaseChange) {
        if (event.oldPhase == GameState.BUILDING || event.newPhase == GameState.LOOKING_AT_BUILD) {
            currentBuild.clear()
        }
    }

    @SubscribeEvent
    fun onDebug(event: DebugDataCollectionEvent) {
        event.title("Speed Builders Build Preview")

        if (!isPlaying()) {
            event.addIrrelevant("not playing")
        } else {
            event.addData {
                add("currentBuild.size: ${currentBuild.size}")
                add("currentBuild: $currentBuild")
            }
        }
    }
}
