package at.martimavocado.awesome.events.entity

import net.minecraft.entity.DataWatcher
import net.minecraft.entity.Entity
import net.minecraftforge.fml.common.eventhandler.Event

class DataWatcherUpdatedEvent(
    val entity: Entity,
    val updatedEntries: List<DataWatcher.WatchableObject>,
) : Event()
