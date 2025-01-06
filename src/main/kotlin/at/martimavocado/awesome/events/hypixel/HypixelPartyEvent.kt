package at.martimavocado.awesome.events.hypixel

import net.minecraftforge.fml.common.eventhandler.Event
import java.util.UUID

class HypixelPartyEvent(
    val inParty: Boolean,
    val leader: UUID?,
    val members: Set<UUID>?,
) : Event()
