package at.martimavocado.awesome.mixins.hooks

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.WorldChangeEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

@LoadModule
object RenderLivingEntityHelper {
    private val entityColorMap = mutableMapOf<EntityLivingBase, Int>()
    private val entityColorCondition = mutableMapOf<EntityLivingBase, () -> Boolean>()

    private val entityNoHurtTimeCondition = mutableMapOf<EntityLivingBase, () -> Boolean>()

    private val config get() = Awesome.config.debug

    @SubscribeEvent
    fun onWorldChange(event: WorldChangeEvent) {
        entityColorMap.clear()
        entityColorCondition.clear()

        entityNoHurtTimeCondition.clear()
    }

    fun <T : EntityLivingBase> removeEntityColor(entity: T) {
        entityColorMap.remove(entity)
        entityColorCondition.remove(entity)
    }

    fun <T : EntityLivingBase> setEntityColor(
        entity: T,
        color: Int,
        condition: () -> Boolean,
    ) {
        entityColorMap[entity] = color
        entityColorCondition[entity] = condition
    }

    fun <T : EntityLivingBase> setEntityColor(
        entity: T,
        color: Color,
        condition: () -> Boolean,
    ) {
        setEntityColor(entity, color.rgb, condition)
    }

    fun <T : EntityLivingBase> setNoHurtTime(
        entity: T,
        condition: () -> Boolean,
    ) {
        entityNoHurtTimeCondition[entity] = condition
    }

    fun <T : EntityLivingBase> setEntityColorWithNoHurtTime(
        entity: T,
        color: Int,
        condition: () -> Boolean,
    ) {
        setEntityColor(entity, color, condition)
        setNoHurtTime(entity, condition)
    }

    fun <T : EntityLivingBase> setEntityColorWithNoHurtTime(
        entity: T,
        color: Color,
        condition: () -> Boolean,
    ) {
        setEntityColorWithNoHurtTime(entity, color.rgb, condition)
    }

    fun <T : EntityLivingBase> removeNoHurtTime(entity: T) {
        entityNoHurtTimeCondition.remove(entity)
    }

    fun <T : EntityLivingBase> removeCustomRender(entity: T) {
        removeEntityColor(entity)
        removeNoHurtTime(entity)
    }

    @JvmStatic
    fun <T : EntityLivingBase> internalSetColorMultiplier(entity: T): Int {
        if (!config.renderToggle) return 0
        if (entityColorMap.containsKey(entity)) {
            val condition = entityColorCondition[entity]!!
            if (condition.invoke()) {
                return entityColorMap[entity]!!
            }
        }
        return 0
    }

    @JvmStatic
    fun <T : EntityLivingBase> internalChangeHurtTime(entity: T): Int {
        if (!config.renderToggle) return entity.hurtTime
        run {
            val condition = entityNoHurtTimeCondition[entity] ?: return@run
            if (condition.invoke()) {
                return 0
            }
        }
        return entity.hurtTime
    }
}
