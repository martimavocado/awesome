package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.data.GameStatus
import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.data.PlayerStatus
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
import at.martimavocado.awesome.utils.StringUtils.matchMatcher
import at.martimavocado.awesome.utils.StringUtils.matches
import net.minecraft.block.BlockColored
import net.minecraft.init.Blocks
import net.minecraft.item.EnumDyeColor
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object SheepWarsAPI {
    var magicWool: SheepWarsMagicWool? = null
        private set
    var playerStatus: PlayerStatus? = null
        private set
    var gameStatus: GameStatus? = null
        private set

    private val magicWoolHitPattern = "^§5§lMAGIC WOOL!.*\$".toPattern()
    private val gameStartPattern = "§f {23}§r§e§lWelcome to Sheep Wars!".toPattern()
    private val gameEndPattern = "§f {23}§r§e§lWelcome to Sheep Wars!".toPattern()
    private val playerKillPattern =
        "^§(?<playerTeam>9)(?<player>\\w+)[\\w' §]+§(?<killerTeam>c)(?<killer>\\w+)[\\w' §]+\\.$".toPattern()
    private val playerWalkOffPattern = "§.(?<player>\\w+) §r§7fell into the void\\.".toPattern()

    fun isPlaying() = HypixelGame.SHEEP_WARS.isPlaying() && gameStatus == GameStatus.IN_GAME

    fun isAlive() = isPlaying() && playerStatus == PlayerStatus.ALIVE

    @SubscribeEvent
    fun onBlockChange(event: BlockChangeEvent) {
        if (!HypixelGame.SHEEP_WARS.isPlaying()) return
        if (!(event.old == Blocks.wool || event.old == Blocks.air)) return
        if (event.new != Blocks.wool) return

        if (!checkSurroundingBlocks(event.location)) return

        val color = event.newState.getValue(BlockColored.COLOR)

        spawnWool(color, event.location)
    }

    private fun checkSurroundingBlocks(blockPosition: PositionVec): Boolean =
        blockPosition.add(x = 1).getBlockAt() == Blocks.air &&
            blockPosition.add(x = -1).getBlockAt() == Blocks.air &&
            (
                blockPosition.add(y = 1).getBlockAt() == Blocks.air ||
                    blockPosition
                        .add(y = 1)
                        .getBlockAt() == Blocks.fire
            ) &&
            blockPosition.add(y = -1).getBlockAt() == Blocks.air &&
            blockPosition.add(z = 1).getBlockAt() == Blocks.air &&
            blockPosition.add(z = -1).getBlockAt() == Blocks.air

    @SubscribeEvent
    fun onChat(event: ChatReceiveEvent) {
        if (!HypixelGame.SHEEP_WARS.isPlaying()) return
        if (magicWoolHitPattern.matches(event.message)) {
            magicWool = null
            return
        }
        if (handleGameStatus(event.message)) return
        handleKill(event.message)
    }

    private fun handleKill(message: String) {
        playerKillPattern.matchMatcher(message) {
//            val killerTeam = group("killerTeam")
//            val killer = group("killer")
//
//            val playerTeam = group("playerTeam")
            val player = group("player")

            if (player == PlayerUtils.playerIGN) {
                ChatUtils.debug("you died")
                playerStatus == PlayerStatus.DEAD
            }
            return
        }
        playerWalkOffPattern.matchMatcher(message) {
            val player = group("player")

            if (player == PlayerUtils.playerIGN) playerStatus = PlayerStatus.DEAD
        }
    }

    private fun handleGameStatus(message: String): Boolean {
        when {
            gameStartPattern.matches(message) -> {
                ChatUtils.debug("in-game")
                gameStatus = GameStatus.IN_GAME
                playerStatus = PlayerStatus.ALIVE
                return true
            }

            gameEndPattern.matches(message) -> {
                ChatUtils.debug("post game")
                gameStatus = GameStatus.POST_GAME
                playerStatus = null
                magicWool = null
                return true
            }

            else -> return false
        }
        return false
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    fun onTick(event: AwesomeTickEvent) {
        if (!HypixelGame.SHEEP_WARS.isPlaying()) return
        val wool = magicWool ?: return

        magicWool = wool.copy(age = wool.age + 1)
    }

    @SubscribeEvent
    fun onGameSwitch(event: HypixelServerChangeEvent) {
        magicWool = null
        playerStatus = null
        gameStatus =
            if (HypixelGame.SHEEP_WARS.isPlaying()) {
                GameStatus.PRE_GAME
            } else {
                null
            }
    }

    private fun spawnWool(
        color: EnumDyeColor,
        location: PositionVec,
    ) {
        val magicWoolType = SheepWarsMagicWoolType.getFromDye(color)

        magicWool =
            SheepWarsMagicWool(
                magicWoolType,
                location,
                0,
            )
    }
}
