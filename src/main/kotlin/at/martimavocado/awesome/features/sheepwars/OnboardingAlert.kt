package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.chat.ChatReceiveEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.RegexUtils.matches
import at.martimavocado.awesome.utils.TitleManager
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration.Companion.seconds

@LoadModule
object OnboardingAlert {
    private val spawnPattern = "§eAn §r§6§lOnboarding Sheep §r§ehas been spawned for each team!".toPattern()
    private val config get() = Awesome.config.sheepWars.onboardingAlert

    @SubscribeEvent
    fun onChat(event: ChatReceiveEvent) {
        if (!config.enableTitle) return
        if (!SheepWarsAPI.isPlaying()) return
        if (!spawnPattern.matches(event.message)) return

        TitleManager.setTitle("§6§lOnboarding Sheep", null, 2.seconds, 0.2.seconds, 0.2.seconds)
    }
}
