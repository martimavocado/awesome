package at.martimavocado.awesome.features.bedwars

import at.martimavocado.awesome.events.ShowTitleEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object ShowStats {
    @SubscribeEvent
    fun onShowTitle(event: ShowTitleEvent) {
        return
    }
}