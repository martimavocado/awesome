package at.martimavocado.awesome.utils

import at.martimavocado.awesome.data.PositionVec
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer

object EntityUtils {
    private val mc get() = Minecraft.getMinecraft()

    fun Entity.getLocation() = PositionVec(posX, posY, posZ)

    fun getPlayers() = mc.theWorld.playerEntities.toList()

    fun getEntityByID(id: Int): Entity? = mc.theWorld.getEntityByID(id)

    fun Entity.isRealPlayer() = this is EntityPlayer && this.isRealPlayer()

    fun EntityPlayer.isRealPlayer() = this.uniqueID?.let { it.version() == 4 } == true
}
