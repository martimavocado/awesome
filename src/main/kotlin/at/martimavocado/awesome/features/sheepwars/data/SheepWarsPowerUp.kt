package at.martimavocado.awesome.features.sheepwars.data

import at.martimavocado.awesome.utils.RegexUtils.matches
import at.martimavocado.awesome.utils.StringUtils.capitalize
import java.util.regex.Pattern
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

enum class SheepWarsPowerUp(
    private val woolPattern: Pattern,
    private val prettyName: String? = null,
    val duration: Duration? = null,
) {
    RANDOM_SHEEP("found \\+1 [\\w ]+ Sheep for the (RED|BLUE) team".toPattern(), "+1 Sheep"),
    BIG_EXPLOSION("activated bigger explosions for 15s".toPattern(), "Bigger Explosions", 15.seconds),
    EXPLOSIVE_ARROW("activated explosive arrows for 15s".toPattern(), "Explosive Arrows", 15.seconds),
    SLOW_SHEEP("decreased (RED|BLUE) team's sheep speed for 10s".toPattern(), duration = 10.seconds),
    FAST_SHEEP("increased (RED|BLUE) team's sheep speed for 10s".toPattern(), duration = 10.seconds),
    HEALTH_REGEN("activated health regeneration for 10s".toPattern(), "Health Regeneration", 10.seconds),
    POISON("gave (RED|BLUE) team poison for 3s".toPattern(), duration = 3.seconds),
    BLINDNESS("gave (RED|BLUE) team blindness for 5s".toPattern(), duration = 5.seconds),
    PUNCH_BOW("activated punch bow for 20s".toPattern(), duration = 20.seconds),
    DISABLE_ENEMY_BOW("disabled (RED|BLUE) team's bows for 10s".toPattern(), "Disable Enemy Bows", 10.seconds),
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

        fun getPerkFromWool(message: String) = entries.firstOrNull { it.woolPattern.matches(message) }
    }

    override fun toString(): String = this.prettyName ?: this.name.replace('_', ' ').capitalize()
}
