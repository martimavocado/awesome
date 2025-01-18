package at.martimavocado.awesome.events

import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.utils.NumberUtils.roundTo
import net.minecraft.util.EnumParticleTypes
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class ParticleEvent(
    val type: EnumParticleTypes,
    val location: PositionVec,
    val count: Int,
    val speed: Float,
    val offset: PositionVec,
    val longDistance: Boolean,
    val particleArgs: IntArray,
) : Event() {
    val distanceToPlayer by lazy { location.distanceToPlayer() }

    override fun toString(): String =
        "ReceiveParticleEvent(type='$type', location=${location.roundTo(1)}, count=$count, speed=$speed, offset=${
            offset.roundTo(1)
        }, longDistance=$longDistance, particleArgs=${particleArgs.contentToString()}, distanceToPlayer=${
            distanceToPlayer.roundTo(1)
        })"
}
