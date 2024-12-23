package at.martimavocado.awesome.data

import net.minecraft.util.AxisAlignedBB
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

    override fun toString(): String {
        return "Pos($x, $y, $z)"
    }

    fun toBlockPos() = BlockPos(x, y, z)
    fun toVec3() = Vec3(x, y, z)

    operator fun plus(other: PositionVec) = PositionVec(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: PositionVec) = PositionVec(x - other.x, y - other.y, z - other.z)

    operator fun times(other: PositionVec) = PositionVec(x * other.x, y * other.y, z * other.z)
    operator fun times(other: Double) = PositionVec(x * other, y * other, z * other)
    operator fun times(other: Int) = PositionVec(x * other, y * other, z * other)

    operator fun div(other: PositionVec) = PositionVec(x / other.x, y / other.y, z / other.z)
    operator fun div(other: Double) = PositionVec(x / other, y / other, z / other)

    fun add(x: Int = 0, y: Int = 0, z: Int = 0) =
        PositionVec(this.x + x, this.y + y, this.z + z)

    fun AxisAlignedBB.expand(vec: PositionVec): AxisAlignedBB = expand(vec.x, vec.y, vec.z)
    fun AxisAlignedBB.expand(amount: Double): AxisAlignedBB = expand(amount, amount, amount)

    companion object {
        val expandVector = PositionVec(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026)
    }
}
