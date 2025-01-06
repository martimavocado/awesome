package at.martimavocado.awesome.utils

import at.martimavocado.awesome.events.hypixel.HypixelPartyEvent
import at.martimavocado.awesome.utils.BlockUtils.toPositionVec
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.UUID

object PlayerUtils {
    val playerIGN: String get() = Minecraft.getMinecraft().thePlayer.name
    val cachedUUID = mutableMapOf<UUID, String>()

    fun getPlayer(): EntityPlayerSP? = Minecraft.getMinecraft().thePlayer

    fun getPlayerLocation() = getPlayer()?.playerLocation?.toPositionVec()

    fun getPlayerEyesLocation() = getPlayerLocation()?.add(y = getPlayer()?.eyeHeight?.toDouble() ?: 0.0)

    fun getArmor() = getPlayer()?.inventory?.armorInventory

    @SubscribeEvent
    fun onHypixelParty(event: HypixelPartyEvent) {
        val party = event.members ?: return

        resolveUUIDs(party)
    }

    private fun resolveUUIDs(party: Set<UUID>) {
        val queuedUUID = mutableSetOf<UUID>()

        for (member in party) {
            if (member in cachedUUID) continue

            queuedUUID.add(member)

            if (queuedUUID.size == 10) {
                getAPINames(queuedUUID)
                queuedUUID.clear()
            }
        }

        if (queuedUUID.isNotEmpty()) {
            getAPINames(queuedUUID)
            queuedUUID.clear()
        }
    }

    private fun getAPINames(uuidSet: Set<UUID>) {
        for (uuid in uuidSet) {
            val username =
                Minecraft
                    .getMinecraft()
                    .netHandler
                    .getPlayerInfo(uuid)
                    .gameProfile.name ?: continue

            cachedUUID[uuid] = username
        }
    }

    fun UUID.getPlayerName(): String =
        Minecraft
            .getMinecraft()
            .netHandler
            .getPlayerInfo(this)
            .gameProfile.name
}
