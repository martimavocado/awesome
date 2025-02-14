package at.martimavocado.awesome.events.keyboard

import at.martimavocado.awesome.utils.KeyboardManager
import net.minecraftforge.fml.common.eventhandler.Event

class MousePressEvent(
    val clickType: KeyboardManager.MouseClickType,
) : Event()
