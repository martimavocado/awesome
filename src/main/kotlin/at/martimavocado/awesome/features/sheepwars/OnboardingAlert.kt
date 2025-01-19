package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.data.GameStatus
import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.events.chat.ChatReceiveEvent
import at.martimavocado.awesome.events.games.sheepwars.SheepWarsKillEvent
import at.martimavocado.awesome.events.games.sheepwars.SheepWarsStatusEvent
import at.martimavocado.awesome.events.render.GuiOverlayRenderEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ChatUtils
import at.martimavocado.awesome.utils.RegexUtils.matches
import at.martimavocado.awesome.utils.SimpleTimeMark
import at.martimavocado.awesome.utils.TitleManager
import at.martimavocado.awesome.utils.render.GuiPosition
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@LoadModule
object OnboardingAlert {
    private val spawnPattern = "§eAn §r§6§lOnboarding Sheep §r§ehas been spawned for each team!".toPattern()
    private val config get() = Awesome.config.sheepWars.onboardingAlert

    private var lastKillTime = SimpleTimeMark.farPast()

    @SubscribeEvent
    fun onChat(event: ChatReceiveEvent) {
        if (!config.enableTitle) return
        if (!SheepWarsAPI.isPlaying()) return
        if (!spawnPattern.matches(event.message)) return

        ChatUtils.debug("Onboarding spawned after ${lastKillTime.passedSince()}")
        TitleManager.setTitle("§6§lOnboarding Sheep", null, 2.seconds, 0.2.seconds, 0.2.seconds)
    }

    @SubscribeEvent
    fun onKill(event: SheepWarsKillEvent) {
        lastKillTime = SimpleTimeMark.now()
    }

    @SubscribeEvent
    fun onRender(event: GuiOverlayRenderEvent) {
        if (!config.enableTimer) return
        if (!SheepWarsAPI.isPlaying()) return

        val timeUntil = (lastKillTime + 60.seconds).timeUntil()
        if (timeUntil > 1.minutes) return

        val string = "§6§lOnboarding Sheep: §a$timeUntil"

        GuiPosition(100, 100, HypixelGame.SHEEP_WARS).renderString(string, true)
    }

    @SubscribeEvent
    fun onStatus(event: SheepWarsStatusEvent) {
        if (event.gameStatus != GameStatus.IN_GAME) return

        lastKillTime = SimpleTimeMark.now() + 10.seconds
    }
}
