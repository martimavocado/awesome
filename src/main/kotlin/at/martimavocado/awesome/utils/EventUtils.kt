package at.martimavocado.awesome.utils

import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.events.WorldChangeEvent
import at.martimavocado.awesome.events.entity.DataWatcherUpdatedEvent
import at.martimavocado.awesome.events.entity.EntityHealthUpdateEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.boss.EntityWither
import net.minecraft.entity.item.EntityArmorStand
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.item.EntityItemFrame
import net.minecraft.entity.item.EntityXPOrb
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

    private val ignoredEntities =
        setOf(
            EntityArmorStand::class.java,
            EntityXPOrb::class.java,
            EntityItem::class.java,
            EntityItemFrame::class.java,
            EntityPlayerSP::class.java,
        )

    @SubscribeEvent
    fun onDataWatcherUpdate(event: DataWatcherUpdatedEvent) {
        for (entry in event.updatedEntries) {
            if (entry.dataValueId == 6) {
                val health = (entry.`object` as? Float) ?: continue

                val entity = EntityUtils.getEntityById(event.entity.entityId) ?: continue
                if (entity.javaClass in ignoredEntities) continue

                if (event.entity is EntityWither && health == 300f && event.entity.entityId < 0) continue
                if (event.entity is EntityLivingBase) {
                    EntityHealthUpdateEvent(event.entity, health)
                }
            }
        }
    }
}
