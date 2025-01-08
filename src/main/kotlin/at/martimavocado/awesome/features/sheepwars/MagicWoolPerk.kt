package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.events.render.GuiOverlayRenderEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.SoundUtils
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object MagicWoolPerk {
    private val config get() = Awesome.config.sheepWars.magicWoolPerk

    @SubscribeEvent
    fun onOverlay(event: GuiOverlayRenderEvent) {
        if (!(isEnabled() && config.perkGUI)) return
        val wool = SheepWarsAPI.magicWool ?: return

        val string = "§${wool.type.color.colorCode}$wool§7: §f${wool.type}"

        config.perkPosition.renderString(string)
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onTickEvent(event: AwesomeTickEvent) {
        if (!(isEnabled() && config.shootPing)) return

        val wool = SheepWarsAPI.magicWool ?: return

        if (wool.type.perk !in config.goodPerks) return
        if (!wool.location.canSee()) return
        if (wool.age % config.pingDelay != 0) return

        SoundUtils.playDing()
    }

    private fun isEnabled() = SheepWarsAPI.isAlive()
}
