package at.martimavocado.awesome.features.sheepwars.data

import at.martimavocado.awesome.features.sheepwars.data.SheepWarsMagicWoolType.entries
import at.martimavocado.awesome.utils.AwesomeColor
import at.martimavocado.awesome.utils.StringUtils.capitalize
import net.minecraft.item.EnumDyeColor

enum class SheepWarsMagicWoolType(
    val perk: SheepWarsPowerUp,
    val color: AwesomeColor,
) {
    WHITE(SheepWarsPowerUp.RANDOM_SHEEP, AwesomeColor.WHITE),
    ORANGE(SheepWarsPowerUp.BIG_EXPLOSION, AwesomeColor.ORANGE),
    MAGENTA(SheepWarsPowerUp.RANDOM_SHEEP, AwesomeColor.MAGENTA),
    LIGHT_BLUE(SheepWarsPowerUp.SLOW_SHEEP, AwesomeColor.LIGHT_BLUE),
    YELLOW(SheepWarsPowerUp.RANDOM_SHEEP, AwesomeColor.YELLOW),
    LIME(SheepWarsPowerUp.RANDOM_SHEEP, AwesomeColor.LIME),
    PINK(SheepWarsPowerUp.HEALTH_REGEN, AwesomeColor.PINK),
    GRAY(SheepWarsPowerUp.BLINDNESS, AwesomeColor.GRAY),
    LIGHT_GRAY(SheepWarsPowerUp.PUNCH_BOW, AwesomeColor.LIGHT_GRAY),
    CYAN(SheepWarsPowerUp.FAST_SHEEP, AwesomeColor.CYAN),
    PURPLE(SheepWarsPowerUp.RANDOM_SHEEP, AwesomeColor.PURPLE),
    BLUE(SheepWarsPowerUp.DISABLE_ENEMY_BOW, AwesomeColor.BLUE),
    BROWN(SheepWarsPowerUp.RANDOM_SHEEP, AwesomeColor.BROWN),
    GREEN(SheepWarsPowerUp.POISON, AwesomeColor.GREEN),
    RED(SheepWarsPowerUp.EXPLOSIVE_ARROW, AwesomeColor.RED),
    BLACK(SheepWarsPowerUp.RANDOM_SHEEP, AwesomeColor.BLACK),
    ;

    override fun toString(): String = super.toString().replace('_', ' ').capitalize()

    companion object {
        fun getFromDye(dyeColor: EnumDyeColor) = entries.first { it.color.dyeColor == dyeColor }
    }
}
