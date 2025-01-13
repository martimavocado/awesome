package at.martimavocado.awesome.events.games.sheepwars

import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.features.sheepwars.data.SheepWarsMagicWoolType
import at.martimavocado.awesome.features.sheepwars.data.SheepWarsPowerUp
import net.minecraftforge.fml.common.eventhandler.Event

open class SheepWarsMagicWoolEvent : Event() {
    open class Spawn(
        val location: PositionVec,
        val type: SheepWarsMagicWoolType,
    ) : SheepWarsMagicWoolEvent()

    open class TypeChange(
        val oldType: SheepWarsMagicWoolType,
        val newType: SheepWarsMagicWoolType,
    ) : SheepWarsMagicWoolEvent()

    open class Shoot(
        val player: String?,
        val perk: SheepWarsPowerUp,
    ) : SheepWarsMagicWoolEvent()
}
