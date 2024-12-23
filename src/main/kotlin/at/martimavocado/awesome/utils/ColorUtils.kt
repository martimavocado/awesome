package at.martimavocado.awesome.utils

import net.minecraft.item.EnumDyeColor
import java.awt.Color

object ColorUtils {
    private const val MIN_CHROMA_SECS = 1
    private const val MAX_CHROMA_SECS = 60

    private val startTime = SimpleTimeMark.now()

    fun EnumDyeColor.toColor(): Color {
        return when (this) {
            EnumDyeColor.WHITE -> Color(255, 255, 255)
            EnumDyeColor.ORANGE -> Color(255, 140, 0)
            EnumDyeColor.MAGENTA -> Color(255, 0, 255)
            EnumDyeColor.LIGHT_BLUE -> Color(69, 140, 230)
            EnumDyeColor.YELLOW -> Color(255, 255, 0)
            EnumDyeColor.LIME -> Color(50, 205, 50)
            EnumDyeColor.PINK -> Color(255, 192, 203)
            EnumDyeColor.GRAY -> Color(105, 105, 105)
            EnumDyeColor.SILVER -> Color(211, 211, 211)
            EnumDyeColor.CYAN -> Color(0, 139, 139)
            EnumDyeColor.PURPLE -> Color(128, 0, 128)
            EnumDyeColor.BLUE -> Color(0, 0, 255)
            EnumDyeColor.BROWN -> Color(139, 69, 19)
            EnumDyeColor.GREEN -> Color(0, 128, 0)
            EnumDyeColor.RED -> Color(255, 0, 0)
            EnumDyeColor.BLACK -> Color(0, 0, 0)
        }
    }

    fun String.toColor() = Color(toColorInt(), true)

    fun String.toColorInt(): Int {
        val (chroma, tempAlpha, red, green, blue) = decompose(this)
        val alpha = when (tempAlpha) {
            0 -> 0
            255 -> 1
            else -> 255 - tempAlpha
        }
        val (hue, sat, bri) = Color.RGBtoHSB(red, green, blue, null)

        val adjustedHue = if (chroma > 0) (hue + (startTime.passedSince().inWholeMilliseconds / 1000f / chromaSpeed(chroma) % 1)).let {
            if (it < 0) it + 1f else it
        } else hue

        return (alpha and 0xFF) shl 24 or (Color.HSBtoRGB(adjustedHue, sat, bri) and 0x00FFFFFF)
    }

    private fun decompose(csv: String) = csv.split(":").mapNotNull { it.toIntOrNull() }.toIntArray()
    private fun chromaSpeed(speed: Int) = (255 - speed) / 254f * (MAX_CHROMA_SECS - MIN_CHROMA_SECS) + MIN_CHROMA_SECS
}