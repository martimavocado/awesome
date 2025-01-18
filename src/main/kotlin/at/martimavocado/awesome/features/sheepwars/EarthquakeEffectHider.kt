package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.ParticleEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.BlockUtils
import at.martimavocado.awesome.utils.BlockUtils.toPositionVec
import at.martimavocado.awesome.utils.EventUtils.cancel
import net.minecraft.util.BlockPos
import net.minecraft.util.EnumParticleTypes.BLOCK_CRACK
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object EarthquakeEffectHider {
    private val config get() = Awesome.config.sheepWars

    fun shouldCancelOverlay(position: BlockPos): Boolean {
        if (!config.hideEarthquake) return false

        val position = position.toPositionVec()
        val lookingAt = BlockUtils.getBlockLookingAt()

        return position != lookingAt
    }

    @SubscribeEvent
    fun onParticle(event: ParticleEvent) {
        if (!config.hideEarthquake) return

        if (event.type != BLOCK_CRACK) return
        if (event.count != 4) return
        if (event.speed != 0.1f) return
        if (event.longDistance != false) return

        event.cancel()
    }
}
