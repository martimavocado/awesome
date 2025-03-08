package at.martimavocado.awesome.mixins.hooks

import at.martimavocado.awesome.events.render.ContainerBackgroundDrawEvent
import at.martimavocado.awesome.events.render.ContainerForegroundDrawEvent
import at.martimavocado.awesome.utils.EventUtils.post
import net.minecraft.client.gui.inventory.GuiContainer

class GuiContainerHook(
    guiAny: Any,
) {
    val gui: GuiContainer = guiAny as GuiContainer

    fun foregroundDrawn(
        mouseX: Int,
        mouseY: Int,
        partialTicks: Float,
    ) {
        ContainerForegroundDrawEvent(gui, gui.inventorySlots, mouseX, mouseY, partialTicks).post()
    }

    fun backgroundDrawn(
        mouseX: Int,
        mouseY: Int,
        partialTicks: Float,
    ) {
        ContainerBackgroundDrawEvent(gui, gui.inventorySlots, mouseX, mouseY, partialTicks).post()
    }
}
