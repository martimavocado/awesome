package at.martimavocado.awesome.events.render

import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.inventory.Container
import net.minecraftforge.fml.common.eventhandler.Event

class ContainerForegroundDrawEvent(
    val gui: GuiContainer,
    val inventorySlots: Container,
    val mouseX: Int,
    val mouseY: Int,
    val partialTicks: Float,
) : Event()
