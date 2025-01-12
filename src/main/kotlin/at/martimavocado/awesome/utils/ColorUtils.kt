package at.martimavocado.awesome.utils

import java.awt.Color

object ColorUtils {
    private const val MIN_CHROMA_SECS = 1
    private const val MAX_CHROMA_SECS = 60

    private val startTime = SimpleTimeMark.now()

    fun String.toColor() = Color(toColorInt(), true)

    fun String.toColorInt(): Int {
        val (chroma, tempAlpha, red, green, blue) = decompose(this)
        val alpha =
            when (tempAlpha) {
                0 -> 0
                255 -> 1
                else -> 255 - tempAlpha
            }
        val (hue, sat, bri) = Color.RGBtoHSB(red, green, blue, null)

        val adjustedHue =
            if (chroma > 0) {
                (hue + (startTime.passedSince().inWholeMilliseconds / 1000f / chromaSpeed(chroma) % 1)).let {
                    if (it < 0) it + 1f else it
                }
            } else {
                hue
            }

        return (alpha and 0xFF) shl 24 or (Color.HSBtoRGB(adjustedHue, sat, bri) and 0x00FFFFFF)
    }

    private fun decompose(csv: String) = csv.split(":").mapNotNull { it.toIntOrNull() }.toIntArray()

    private fun chromaSpeed(speed: Int) = (255 - speed) / 254f * (MAX_CHROMA_SECS - MIN_CHROMA_SECS) + MIN_CHROMA_SECS

    fun Color.withAlpha(alpha: Int) = Color(red, green, blue, alpha)
}
