package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.events.BlockChangeEvent
import at.martimavocado.awesome.events.chat.ChatReceiveEvent
import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.features.sheepwars.data.SheepWarsMagicWool
import at.martimavocado.awesome.features.sheepwars.data.SheepWarsMagicWoolType
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.BlockUtils.getBlockAt
import at.martimavocado.awesome.utils.ChatUtils
import at.martimavocado.awesome.utils.PlayerUtils
import at.martimavocado.awesome.utils.StringUtils.matches
import net.minecraft.block.BlockColored
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

@LoadModule
object SheepWarsAPI {
    var magicWool: SheepWarsMagicWool? = null
        private set
    var isAlive: Boolean = false
        private set

    private val magicWoolHitPattern = "^§5§lMAGIC WOOL!.*\$".toPattern()

    fun isPlaying() = HypixelGame.SHEEP_WARS.isPlaying()

    @SubscribeEvent
    fun onBlockChange(event: BlockChangeEvent) {
        if (!HypixelGame.SHEEP_WARS.isPlaying()) return
        if (event.old != Blocks.air || event.old != Blocks.wool) return
        if (event.new != Blocks.wool) return

        if (!checkSurroundingBlocks(event.location)) return

        val color = event.newState.getValue(BlockColored.COLOR)

        if (event.old == Blocks.wool && magicWool != null) {
            magicWool = magicWool?.copy(type = SheepWarsMagicWoolType.getFromDye(color))
            ChatUtils.chat("updated to color $color")
            return
        }

        spawnWool(color, event.location)
    }

    private fun checkSurroundingBlocks(blockPosition: PositionVec): Boolean {
        return blockPosition.add(x = 1).getBlockAt() == Blocks.air
                && blockPosition.add(x = -1).getBlockAt() == Blocks.air
                && (blockPosition.add(y = 1).getBlockAt() == Blocks.air || blockPosition.add(y = 1).getBlockAt() == Blocks.fire)
                && blockPosition.add(y = -1).getBlockAt() == Blocks.air
                && blockPosition.add(z = 1).getBlockAt() == Blocks.air
                && blockPosition.add(z = -1).getBlockAt() == Blocks.air
    }

    @SubscribeEvent
    fun onChat(event: ChatReceiveEvent) {
        if (!HypixelGame.SHEEP_WARS.isPlaying()) return
        if (!magicWoolHitPattern.matches(event.message)) return

        resetWool()
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    fun onTick(event: ClientTickEvent) {
        if (!HypixelGame.SHEEP_WARS.isPlaying()) return
        val wool = magicWool ?: return

        magicWool = wool.copy(age = wool.age+1)
    }

    @SubscribeEvent
    fun onGameSwitch(event: HypixelServerChangeEvent) {
        resetWool()
    }

    private fun spawnWool(color: EnumDyeColor, location: PositionVec) {
        val magicWoolType = SheepWarsMagicWoolType.getFromDye(color)

        magicWool = SheepWarsMagicWool(
            magicWoolType,
            location,
            magicWool?.age ?: 0
        )

        ChatUtils.chat("spawned with new color $color")
    }

    private fun resetWool() {
        magicWool = null
    }

    @SubscribeEvent
    fun onTick(event: AwesomeTickEvent) {
        if (!HypixelGame.SHEEP_WARS.isPlaying()) return

        isAlive = PlayerUtils.getPlayer()?.capabilities?.allowFlying == true
    }
}