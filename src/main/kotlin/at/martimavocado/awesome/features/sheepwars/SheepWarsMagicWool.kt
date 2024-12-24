package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.features.sheepwars.SheepWarsPowerUp.*
import at.martimavocado.awesome.utils.AwesomeColor
import at.martimavocado.awesome.utils.StringUtils.capitalize
import net.minecraft.item.EnumDyeColor

enum class SheepWarsMagicWool(val perk: SheepWarsPowerUp, val color: AwesomeColor) {
    WHITE(RANDOM_SHEEP, AwesomeColor.WHITE),
    ORANGE(BIG_EXPLOSION, AwesomeColor.ORANGE),
    MAGENTA(RANDOM_SHEEP, AwesomeColor.MAGENTA),
    LIGHT_BLUE(SLOW_SHEEP, AwesomeColor.LIGHT_BLUE),
    YELLOW(RANDOM_SHEEP, AwesomeColor.YELLOW),
    LIME(RANDOM_SHEEP, AwesomeColor.LIME),
    PINK(HEALTH_REGEN, AwesomeColor.PINK),
    GRAY(BLINDNESS, AwesomeColor.GRAY),
    LIGHT_GRAY(PUNCH_BOW, AwesomeColor.LIGHT_GRAY),
    CYAN(FAST_SHEEP, AwesomeColor.CYAN),
    PURPLE(RANDOM_SHEEP, AwesomeColor.PURPLE),
    BLUE(DISABLE_ENEMY_BOW, AwesomeColor.BLUE),
    BROWN(RANDOM_SHEEP, AwesomeColor.BROWN),
    GREEN(POISON, AwesomeColor.GREEN),
    RED(EXPLOSIVE_ARROW, AwesomeColor.RED),
    BLACK(RANDOM_SHEEP, AwesomeColor.BLACK),
    ;

    override fun toString(): String {
        return super.toString().replace('_', ' ').capitalize()
    }

    companion object {
        fun getFromDye(dyeColor: EnumDyeColor) = entries.first { it.color.dyeColor == dyeColor }
    }
}