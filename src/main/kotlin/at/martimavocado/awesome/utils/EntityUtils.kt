package at.martimavocado.awesome.utils

import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.events.CommandRegistrationEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.BlockUtils.toPositionVec
import at.martimavocado.awesome.utils.system.ClipboardUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object EntityUtils {
    private val mc get() = Minecraft.getMinecraft()

    fun Entity.getLocation() = PositionVec(posX, posY, posZ)

    fun getPlayers() = mc.theWorld.playerEntities.toList()

    fun getEntityByID(id: Int): Entity? = mc.theWorld.getEntityByID(id)

    fun Entity.isRealPlayer() = this is EntityPlayer && this.isRealPlayer()

    fun EntityPlayer.isRealPlayer() = this.uniqueID?.let { it.version() == 4 } == true

    @SubscribeEvent
    fun onCommandRegistration(event: CommandRegistrationEvent) {
        event.register("awcopyentities") {
            description = "Copy nearby entities to your clipboard!"
            callback { copyEntitiesCommand(it) }
        }
    }

    private fun copyEntitiesCommand(args: Array<String>) {
        val distance = args.getOrElse(0) { "15" }.toIntOrNull() ?: return
        var stringToCopy = "-------------\n"
        val entities =
            mc.theWorld.loadedEntityList.filter {
                it.positionVector.toPositionVec().distanceToPlayer() <= distance &&
                    it != null &&
                    it !is EntityPlayerSP
            }

        for (entity in entities) {
            stringToCopy += "\n"
            stringToCopy += "type: ${entity.javaClass.simpleName}\n"
            stringToCopy += "name: ${entity.name}\n"
            stringToCopy += "displayName: ${entity.displayName}\n"
            stringToCopy += "customNameTag: ${entity.customNameTag}\n"
            stringToCopy += "entityID: ${entity.entityId}\n"
            stringToCopy += "position: ${entity.positionVector.toPositionVec()}\n"
            if (entity is EntityPlayer) stringToCopy += "isRealPlayer: ${entity.isRealPlayer()}\n"
            stringToCopy += "\n"
        }

        ClipboardUtils.copyToClipboard(stringToCopy)
        ChatUtils.chat("Copied ${entities.size} entities to the clipboard!")
    }
}
