package at.martimavocado.awesome.utils

import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.events.keyboard.KeyPressEvent
import at.martimavocado.awesome.events.keyboard.MousePressEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.EventUtils.post
import io.github.notenoughupdates.moulconfig.gui.GuiScreenElementWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse

@LoadModule
object KeyboardManager {
    private val clickedMouseButtons = mutableMapOf<MouseClickType, SimpleTimeMark?>()

    fun Int.isKeyHeld(): Boolean =
        when {
            this == 0 -> false
            this < 0 -> Mouse.isButtonDown(this + 100)
            this >= Keyboard.KEYBOARD_SIZE -> {
                val pressedKey =
                    if (Keyboard.getEventKey() == 0) Keyboard.getEventCharacter().code + 256 else Keyboard.getEventKey()
                Keyboard.getEventKeyState() && this == pressedKey
            }

            else -> Keyboard.isKeyDown(this)
        }

    @SubscribeEvent
    fun onTick(event: AwesomeTickEvent) {
        val currentScreen = Minecraft.getMinecraft().currentScreen
        if (currentScreen is GuiScreenElementWrapper || currentScreen is GuiChat) return

        if (Keyboard.getEventKeyState() && Keyboard.getEventKey() != 0) {
            postEvent(Keyboard.getEventKey())
        }
    }

    private fun postEvent(keyCode: Int) {
        KeyPressEvent(keyCode).post()
    }

    enum class MouseClickType(
        private val keycode: Int,
    ) {
        LEFT_CLICK(0),
        RIGHT_CLICK(1),
        MIDDLE_CLICK(2),
        MOUSE_4(3),
        MOUSE_5(4),
        ;

        @LoadModule
        companion object {
            fun getTypeFromKey(key: Int) = entries.firstOrNull { it.keycode == key }

            fun MouseClickType.isPressed() = clickedMouseButtons[this] != null

            fun MouseClickType.isPressedSince() = clickedMouseButtons[this]?.passedSince()

            @SubscribeEvent
            fun onTick(event: AwesomeTickEvent) {
                entries.forEach {
                    if (Mouse.isButtonDown(it.keycode)) {
                        if (clickedMouseButtons[it] == null) {
                            clickedMouseButtons[it] = SimpleTimeMark.now()
                            MousePressEvent(it).post()
                        }
                    } else {
                        clickedMouseButtons[it] = null
                    }
                }
            }
        }
    }
}
