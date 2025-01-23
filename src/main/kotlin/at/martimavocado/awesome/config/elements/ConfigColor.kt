package at.martimavocado.awesome.config.elements

import at.martimavocado.awesome.utils.ColorUtils.decompose
import java.awt.Color

data class ConfigColor(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0,
    val alpha: Int = 255,
    val chroma: Int = 0,
) {
    constructor(string: String) : this(
        decompose(string)[2],
        decompose(string)[3],
        decompose(string)[4],
        decompose(string)[1],
        decompose(string)[0],
    )

    constructor(
        red: Double = 0.0,
        green: Double = 0.0,
        blue: Double = 0.0,
        alpha: Double = 1.0,
        chroma: Double = 0.0,
    ) : this(
        (red * 255).coerceIn(0.0..255.0).toInt(),
        (green * 255).coerceIn(0.0..255.0).toInt(),
        (blue * 255).coerceIn(0.0..255.0).toInt(),
        (alpha * 255).coerceIn(0.0..255.0).toInt(),
        (chroma * 255).coerceIn(0.0..255.0).toInt(),
    )

    constructor(color: Color) : this(color.red, color.green, color.blue, color.alpha, 0)

    override fun toString() = "$chroma:$alpha:$red:$green:$blue"

    fun toColor() = Color(red, green, blue, alpha)
}
