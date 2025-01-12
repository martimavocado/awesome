package at.martimavocado.awesome.events.entity

import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.fml.common.eventhandler.Event

class EntityHealthUpdateEvent(
    val entity: EntityLivingBase,
    val health: Float,
) : Event()
