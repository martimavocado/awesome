package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.events.render.GuiOverlayRenderEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.SoundUtils
import at.martimavocado.awesome.utils.render.RenderUtils.renderString
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object MagicWoolPerk {
    private val config get() = Awesome.config.sheepWars.magicWoolPerk

    @SubscribeEvent
    fun onOverlay(event: GuiOverlayRenderEvent) {
        if (!config.perkGUI) return
        if (!SheepWarsAPI.isAlive()) return
        val wool = SheepWarsAPI.magicWool ?: return

        val string = "§${wool.type.color.colorCode}${wool.type}§7: §7${wool.type.perk}"

        config.perkPosition.renderString(string, "Magic Wool Perk")
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onTickEvent(event: AwesomeTickEvent) {
        if (!config.shootPing) return
        if (!SheepWarsAPI.isAlive()) return

        val wool = SheepWarsAPI.magicWool ?: return

        if (wool.type.perk !in config.goodPerks) return
        if (!wool.location.isBlockVisible()) return
        if (wool.age % config.pingDelay != 0) return

        SoundUtils.playDing()
    }
}
