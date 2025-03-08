package at.martimavocado.awesome.features.bedwars

import at.martimavocado.awesome.events.render.ContainerBackgroundDrawEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.AwesomeColor
import at.martimavocado.awesome.utils.InventoryUtils.getContainerName
import at.martimavocado.awesome.utils.InventoryUtils.getUpperItems
import at.martimavocado.awesome.utils.PlayerUtils
import at.martimavocado.awesome.utils.render.GuiRenderUtils.highlight
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.inventory.ContainerChest
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object LobbyChallengeHighlighter {
    @SubscribeEvent
    fun onContainerBackground(event: ContainerBackgroundDrawEvent) {
        if (!BedWarsAPI.GameType.LOBBY.isPlaying()) return
        if (event.gui !is GuiChest) return

        if (event.gui.getContainerName() == "Bed Wars Challenges") {
            challengeHandler(event)
        }

        if (event.gui.getContainerName() == "Bed Wars Quests") {
            questHandler(event)
        }
    }

    private fun challengeHandler(event: ContainerBackgroundDrawEvent) {
        val inventory = event.gui.inventorySlots as ContainerChest

        for ((slot, stack) in inventory.getUpperItems()) {
            if (stack == null) continue

            val tooltip = stack.getTooltip(PlayerUtils.getPlayer(), false)
            if (!tooltip.any { it.contains("Click to Activate!") || it.contains("Challenge Locked") }) continue

            if (!tooltip.any { it.contains("Rewards Claimed") }) {
                slot highlight AwesomeColor.RED.color
            }
        }
    }

    private fun questHandler(event: ContainerBackgroundDrawEvent) {
        val inventory = event.gui.inventorySlots as ContainerChest

        for ((slot, stack) in inventory.getUpperItems()) {
            if (stack == null) continue

            val tooltip = stack.getTooltip(PlayerUtils.getPlayer(), false)
            if (tooltip.any {
                    it.contains("Click to Start this quest.") || it.contains("You've already started this quest!")
                }
            ) {
                slot highlight AwesomeColor.RED.color
            }
        }
    }
}
