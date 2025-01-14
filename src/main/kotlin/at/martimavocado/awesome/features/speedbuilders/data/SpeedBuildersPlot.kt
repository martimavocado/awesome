package at.martimavocado.awesome.features.speedbuilders.data

import at.martimavocado.awesome.data.PositionVec
import net.minecraft.util.AxisAlignedBB

data class SpeedBuildersPlot(
    val centerPoint: PositionVec,
    val boundingBox: AxisAlignedBB,
) {
    constructor(centerPoint: PositionVec) : this(
        centerPoint,
        AxisAlignedBB(
            centerPoint.add(x = -4, y = 0, z = -4).toBlockPos(),
            centerPoint.add(x = 5, z = 5).copy(y = 90.0).toBlockPos(),
        ),
    )
}
