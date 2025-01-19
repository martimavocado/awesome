package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.data.GameStatus
import at.martimavocado.awesome.events.games.sheepwars.SheepWarsStatusEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.TitleManager
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.seconds

@LoadModule
object KitReminder {
    private val config get() = Awesome.config.sheepWars
    private var wasRaider = false

    @SubscribeEvent
    fun onGameSwitch(event: SheepWarsStatusEvent) {
        if (!config.aztlanRaider) return
        if (event.gameStatus != GameStatus.PRE_GAME) return

        val isRaider = SheepWarsAPI.map == "Aztlan"

        val title =
            when {
                isRaider && !wasRaider -> "§aSwitch to §6Raider"
                isRaider && wasRaider -> "§aKeep Raider"
                !isRaider && wasRaider -> "§cSwitch Kit"
                else -> return
            }
        wasRaider = isRaider

        TitleManager.setTitle(title, null, 5.seconds, 0.5.seconds, 0.5.seconds)
    }
}
