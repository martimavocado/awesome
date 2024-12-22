package at.martimavocado.awesome.data

import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3

data class PositionVec(
    val x: Double,
    val y: Double,
    val z: Double,
) {
    constructor() : this(0.0, 0.0, 0.0)
    constructor(x: Int, y: Int, z: Int) : this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(x: Float, y: Float, z: Float) : this(x.toDouble(), y.toDouble(), z.toDouble())

    fun toBlockPos() = BlockPos(x, y, z)
    fun toVec3() = Vec3(x, y, z)
}
