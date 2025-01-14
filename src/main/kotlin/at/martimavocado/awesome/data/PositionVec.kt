package at.martimavocado.awesome.data

import at.martimavocado.awesome.utils.BlockUtils.toPositionVec
import at.martimavocado.awesome.utils.PlayerUtils
import net.minecraft.client.Minecraft
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import kotlin.math.acos
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

data class PositionVec(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0,
) {
    constructor() : this(0.0, 0.0, 0.0)
    constructor(x: Int = 0, y: Int = 0, z: Int = 0) : this(x.toDouble(), y.toDouble(), z.toDouble())
    constructor(x: Float = 0f, y: Float = 0f, z: Float = 0f) : this(x.toDouble(), y.toDouble(), z.toDouble())

    override fun toString(): String = "Pos($x, $y, $z)"

    private val mc get() = Minecraft.getMinecraft()

    fun toBlockPos() = BlockPos(x, y, z)

    fun toVec3() = Vec3(x, y, z)

    operator fun plus(other: PositionVec) = PositionVec(x + other.x, y + other.y, z + other.z)

    operator fun minus(other: PositionVec) = PositionVec(x - other.x, y - other.y, z - other.z)

    operator fun times(other: PositionVec) = PositionVec(x * other.x, y * other.y, z * other.z)

    operator fun times(other: Double) = PositionVec(x * other, y * other, z * other)

    operator fun times(other: Int) = PositionVec(x * other, y * other, z * other)

    operator fun div(other: PositionVec) = PositionVec(x / other.x, y / other.y, z / other.z)

    operator fun div(other: Double) = PositionVec(x / other, y / other, z / other)

    fun add(
        x: Int = 0,
        y: Int = 0,
        z: Int = 0,
    ) = PositionVec(this.x + x, this.y + y, this.z + z)

    fun add(
        x: Double = 0.0,
        y: Double = 0.0,
        z: Double = 0.0,
    ) = PositionVec(this.x + x, this.y + y, this.z + z)

    fun add(
        x: Float = 0f,
        y: Float = 0f,
        z: Float = 0f,
    ) = PositionVec(this.x + x, this.y + y, this.z + z)

    fun inLineOfSight(from: PositionVec? = PlayerUtils.playerEyesLocation()): Boolean {
        if (from == null) return false

        val raytrace =
            mc.theWorld.rayTraceBlocks(
                from.toVec3(),
                this.toVec3(),
                false,
                true,
                false,
            ) ?: return true

        return raytrace.blockPos.toPositionVec() == this
    }

    fun isBlockVisible(): Boolean {
        val player = mc.thePlayer ?: return false
        val fov = mc.gameSettings.fovSetting * player.fovModifier

        val aspectRatio = mc.displayWidth.toDouble() / mc.displayHeight.toDouble()
        val horizontalFov = fov * aspectRatio

        val playerEyes = PlayerUtils.playerEyesLocation() ?: return false
        val playerLookVec = player.lookVec.normalize()
        val blockVec = this.toVec3().subtract(playerEyes.toVec3()).normalize()

        val dotProduct = playerLookVec.dotProduct(blockVec)
        val angle = Math.toDegrees(acos(dotProduct))

        val inHorizontal = angle <= horizontalFov / 2.6
        val inVertical = angle <= fov / 2

        if (!inHorizontal && !inVertical) return false

        return inLineOfSight()
    }

    fun lengthSquared(): Double = x * x + y * y + z * z

    fun length(): Double = sqrt(lengthSquared())

    fun normalize() = length().let { PositionVec(x / it, y / it, z / it) }

    fun roundToInt() = PositionVec(this.x.roundToInt(), this.y.roundToInt(), this.z.roundToInt())

    fun distance(other: PositionVec): Double = distanceSq(other).pow(0.5)

    fun distanceSq(
        x: Double,
        y: Double,
        z: Double,
    ): Double = distanceSq(PositionVec(x, y, z))

    fun distance(
        x: Double,
        y: Double,
        z: Double,
    ): Double = distance(PositionVec(x, y, z))

    fun distanceSq(other: PositionVec): Double {
        val dx = other.x - x
        val dy = other.y - y
        val dz = other.z - z
        return (dx * dx + dy * dy + dz * dz)
    }

    companion object {
        val expandVector = PositionVec(0.0020000000949949026, 0.0020000000949949026, 0.0020000000949949026)

        fun AxisAlignedBB.expand(vec: PositionVec): AxisAlignedBB = expand(vec.x, vec.y, vec.z)

        fun AxisAlignedBB.expand(amount: Double): AxisAlignedBB = expand(amount, amount, amount)

        fun AxisAlignedBB.contains(location: PositionVec): Boolean = this.isVecInside(location.toVec3())
    }
}
