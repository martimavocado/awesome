package at.martimavocado.awesome.utils

import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.events.WorldChangeEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.minecraft.client.Minecraft
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

@LoadModule
object EventUtils {
    fun Event.post() {
        MinecraftForge.EVENT_BUS.post(this)
    }

    fun Event.cancel() {
        if (!this.isCancelable) return
        this.isCanceled = true
    }

    private var totalTicks = 0

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.END) return
        if (Minecraft.getMinecraft().thePlayer == null) return

        totalTicks++
        AwesomeTickEvent(totalTicks).post()
    }

    @SubscribeEvent
    fun onWorldSwap(event: WorldEvent.Load) {
        WorldChangeEvent().post()
    }
}
