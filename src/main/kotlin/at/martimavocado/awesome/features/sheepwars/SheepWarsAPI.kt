package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.data.GameStatus
import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.data.PlayerStatus
import at.martimavocado.awesome.data.PositionVec
import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.events.BlockChangeEvent
import at.martimavocado.awesome.events.DebugDataCollectionEvent
import at.martimavocado.awesome.events.chat.ChatReceiveEvent
import at.martimavocado.awesome.events.games.sheepwars.SheepWarsMagicWoolEvent
import at.martimavocado.awesome.events.games.sheepwars.SheepWarsStatusEvent
import at.martimavocado.awesome.events.hypixel.HypixelServerChangeEvent
import at.martimavocado.awesome.features.sheepwars.data.SheepWarsMagicWool
import at.martimavocado.awesome.features.sheepwars.data.SheepWarsMagicWoolType
import at.martimavocado.awesome.features.sheepwars.data.SheepWarsPowerUp
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.BlockUtils.getBlockAt
import at.martimavocado.awesome.utils.ChatUtils
import at.martimavocado.awesome.utils.EventUtils.post
import at.martimavocado.awesome.utils.PlayerUtils
import at.martimavocado.awesome.utils.RegexUtils.matchMatcher
import at.martimavocado.awesome.utils.RegexUtils.matches
import at.martimavocado.awesome.utils.StringUtils.removeColors
import at.martimavocado.awesome.utils.system.AwesomeLogger
import net.minecraft.block.BlockColored
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.EnumDyeColor
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object SheepWarsAPI {
    private val logger = AwesomeLogger("sheepwarsapi")

    var magicWool: SheepWarsMagicWool? = null
        private set
    var playerStatus: PlayerStatus? = null
        private set
    var gameStatus: GameStatus? = null
        private set
    var map: String? = null
        private set

    private val magicWoolHitPattern =
        "^§5§lMAGIC WOOL! (?:§.)+(?<shooter>\\w+) (?<perk>.*)!".toPattern()
    private val gameStartPattern = "§f +§r§e§lWelcome to Sheep Wars!".toPattern()
    private val gameEndPattern = "§f +§r§.§lGAME WIN - \\w+".toPattern()
    private val playerKillPattern =
        "^§(?<playerTeam>.)(?<player>\\w+)[\\w' §]+§(?<killerTeam>.)(?<killer>\\w+)[\\w' §]+\\.$".toPattern()
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
        magicWoolHitPattern.matchMatcher(event.message) {
            val shooter = group("shooter")
            val perkMessage = group("perk").removeColors()

            val perk = SheepWarsPowerUp.getPerkFromWool(perkMessage)

            if (perk == null) {
                ChatUtils.warning("Unknown perk message! '$perkMessage' pls report")
            } else {
                SheepWarsMagicWoolEvent.Shoot(shooter, perk)
            }
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
                SheepWarsStatusEvent(GameStatus.IN_GAME).post()
                playerStatus = PlayerStatus.ALIVE
                return true
            }

            gameEndPattern.matches(message) -> {
                ChatUtils.debug("post game")
                gameStatus = GameStatus.POST_GAME
                SheepWarsStatusEvent(GameStatus.POST_GAME).post()
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
        val wool = magicWool
        if (wool != null) {
            SheepWarsMagicWoolEvent.Shoot(null, wool.type.perk)
        }

        magicWool = null

        playerStatus = null
        gameStatus =
            if (HypixelGame.SHEEP_WARS.isPlaying()) {
                SheepWarsStatusEvent(GameStatus.PRE_GAME).post()
                map = event.map
                GameStatus.PRE_GAME
            } else {
                map = null
                null
            }
    }

    private fun spawnWool(
        color: EnumDyeColor,
        location: PositionVec,
    ) {
        val magicWoolType = SheepWarsMagicWoolType.getFromDye(color)

        val oldWool = magicWool
        if (oldWool?.location != location) {
            SheepWarsMagicWoolEvent.Spawn(location, magicWoolType).post()
        } else {
            SheepWarsMagicWoolEvent.TypeChange(oldWool.type, magicWoolType)
        }
        magicWool =
            SheepWarsMagicWool(
                magicWoolType,
                location,
                0,
            )
    }

    fun EntityPlayer.isFriendly(): Boolean? {
        val team = this.getTeamColor()
        val myTeam = PlayerUtils.getPlayer()?.getTeamColor() ?: return null

        return team == myTeam
    }

    fun EntityPlayer.getTeamColor(): TeamColor? {
        val armor = this.getCurrentArmor(2) ?: return null
        if (armor.item != Items.leather_chestplate) return null

        val color =
            armor.tagCompound
                .getCompoundTag("display")
                .getTag("color")
                .toString()
                .toInt()

        return TeamColor.getTeamFromColor(color)
    }

    @SubscribeEvent
    fun onDebug(event: DebugDataCollectionEvent) {
        event.title("Sheep Wars API")

        if (!HypixelGame.SHEEP_WARS.isPlaying()) {
            event.addIrrelevant("not playing sheepwars")
        } else {
            event.addData {
                add("gameStatus: $gameStatus")
                add("playerStatus: $playerStatus")
                add("")
                add("magicWool: $magicWool")
                add("map: $map")
            }
        }
    }

    enum class TeamColor(
        val color: Int,
    ) {
        BLUE(29680),
        RED(16711680),
        ;

        companion object {
            fun getTeamFromColor(input: Int) =
                TeamColor.entries.firstOrNull { input == it.color } ?: run {
                    logger.log("weird color $input")
                    null
                }
        }
    }
}
