package at.martimavocado.awesome.utils

import at.martimavocado.awesome.utils.AwesomeColor.entries
import at.martimavocado.awesome.utils.StringUtils.capitalize
import net.minecraft.item.EnumDyeColor
import java.awt.Color

enum class AwesomeColor(
    val dyeColor: EnumDyeColor,
    val color: Color,
    val colorCode: Char,
) {
    WHITE(EnumDyeColor.WHITE, Color(255, 255, 255), 'f'),
    ORANGE(EnumDyeColor.ORANGE, Color(255, 140, 0), '6'),
    MAGENTA(EnumDyeColor.MAGENTA, Color(255, 0, 255), 'd'),
    LIGHT_BLUE(EnumDyeColor.LIGHT_BLUE, Color(69, 140, 230), 'b'),
    YELLOW(EnumDyeColor.YELLOW, Color(255, 255, 0), 'e'),
    LIME(EnumDyeColor.LIME, Color(50, 205, 50), 'a'),
    PINK(EnumDyeColor.PINK, Color(255, 192, 203), 'd'),
    GRAY(EnumDyeColor.GRAY, Color(105, 105, 105), '8'),
    LIGHT_GRAY(EnumDyeColor.SILVER, Color(211, 211, 211), '7'),
    CYAN(EnumDyeColor.CYAN, Color(0, 139, 139), '3'),
    PURPLE(EnumDyeColor.PURPLE, Color(128, 0, 128), '5'),
    BLUE(EnumDyeColor.BLUE, Color(0, 0, 255), '1'),
    BROWN(EnumDyeColor.BROWN, Color(139, 69, 19), '6'),
    GREEN(EnumDyeColor.GREEN, Color(0, 128, 0), '2'),
    RED(EnumDyeColor.RED, Color(255, 0, 0), 'c'),
    BLACK(EnumDyeColor.BLACK, Color(0, 0, 0), '0'),
    ;

    override fun toString(): String = super.toString().replace('_', ' ').capitalize()

    companion object {
        fun getFromDye(dyeColor: EnumDyeColor) = entries.first { it.dyeColor == dyeColor }
    }
}
