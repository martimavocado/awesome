package at.martimavocado.awesome.features.sheepwars.data

import at.martimavocado.awesome.utils.StringUtils.capitalize
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class SheepWarsPowerUp(
    private val prettyName: String? = null,
    val duration: Duration? = null,
) {
    RANDOM_SHEEP("+1 Sheep"),
    BIG_EXPLOSION("Bigger Explosions", 15.seconds),
    EXPLOSIVE_ARROW("Explosive Arrows", 15.seconds),
    SLOW_SHEEP(duration = 10.seconds),
    FAST_SHEEP(duration = 10.seconds),
    HEALTH_REGEN("Health Regeneration", 10.seconds),
    POISON(duration = 3.seconds),
    BLINDNESS(duration = 5.seconds),
    PUNCH_BOW(duration = 20.seconds),
    DISABLE_ENEMY_BOW("Disable Enemy Bows", 10.seconds),
    ;

    companion object {
        @JvmField
        val defaultGoodPerks =
            listOf(
                BIG_EXPLOSION,
                SLOW_SHEEP,
                FAST_SHEEP,
                POISON,
            )
    }

    override fun toString(): String = this.prettyName ?: this.name.replace('_', ' ').capitalize()
}
