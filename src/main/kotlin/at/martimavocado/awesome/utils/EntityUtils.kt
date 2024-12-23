package at.martimavocado.awesome.utils

import at.martimavocado.awesome.data.PositionVec
import net.minecraft.entity.Entity

object EntityUtils {
    fun Entity.getLocation() = PositionVec(posX, posY, posZ)
}