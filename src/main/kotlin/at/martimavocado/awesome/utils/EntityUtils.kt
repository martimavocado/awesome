package at.martimavocado.awesome.utils

import at.martimavocado.awesome.data.PositionVec
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity

object EntityUtils {
    private val mc get() = Minecraft.getMinecraft()

    fun Entity.getLocation() = PositionVec(posX, posY, posZ)

    fun getPlayers() = mc.theWorld.playerEntities.toList()
}
