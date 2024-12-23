package at.martimavocado.awesome.events

import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event

@Cancelable
class WorldRenderEvent(val partialTicks: Float): Event()