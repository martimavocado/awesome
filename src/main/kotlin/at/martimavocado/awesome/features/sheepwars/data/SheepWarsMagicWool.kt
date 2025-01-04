package at.martimavocado.awesome.features.sheepwars.data

import at.martimavocado.awesome.data.PositionVec

data class SheepWarsMagicWool(
    val type: SheepWarsMagicWoolType,
    val location: PositionVec,
    val age: Int,
)
