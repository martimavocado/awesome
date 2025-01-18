package at.martimavocado.awesome.features.debug

import at.martimavocado.awesome.events.CommandRegistrationEvent
import at.martimavocado.awesome.events.ParticleEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ChatUtils
import at.martimavocado.awesome.utils.SimpleTimeMark
import at.martimavocado.awesome.utils.system.ClipboardUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import kotlin.time.Duration

@LoadModule
object ParticleTrackerCommand {
    private var isTracking = false

    private val particleList = mutableSetOf<Pair<ParticleEvent, SimpleTimeMark>>()
    private var trackingStartTime = SimpleTimeMark.farPast()

    @SubscribeEvent
    fun onParticle(event: ParticleEvent) {
        if (!isTracking) return

        particleList.add(event to SimpleTimeMark.now())
    }

    @SubscribeEvent
    fun onCommandRegistration(event: CommandRegistrationEvent) {
        event.register("awtrackparticles") {
            description = "Tracks Particles in the world"
            callback { command() }
        }
    }

    private fun command() {
        if (!isTracking) {
            ChatUtils.chat("Started tracking particles!")
            particleList.clear()
            trackingStartTime = SimpleTimeMark.now()
            isTracking = true

            return
        }

        val newMap: Map<ParticleEvent, Duration> =
            particleList.associate { it.first to (it.second - trackingStartTime) }

        val string = newMap.map { (particle, duration) -> "$duration -> $particle" }.joinToString("\n")

        ClipboardUtils.copyToClipboard(string)
        ChatUtils.chat("Copied tracked particles to the clipboard!")

        isTracking = false
        particleList.clear()
        trackingStartTime = SimpleTimeMark.farPast()
    }
}
