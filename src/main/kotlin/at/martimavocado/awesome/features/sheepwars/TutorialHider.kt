package at.martimavocado.awesome.features.sheepwars

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.events.chat.ChatReceiveEvent
import at.martimavocado.awesome.events.render.TitleReceivedEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.EventUtils.cancel
import at.martimavocado.awesome.utils.RegexUtils.matches
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.regex.Pattern

@LoadModule
object TutorialHider {
    private val config get() = Awesome.config.sheepWars

    private val shootChatTutorial =
        "§6Left Click §r§esheep heads to shoot sheep in a §r§astraight line §r§eand cause some types to §r§astick to blocks§r§e!"
            .toPattern()
    private val gravityChatTutorial =
        "§6Right Click §r§esheep heads to shoot sheep with §r§amore shot power and damage§r§e, but be affected by §r§agravity§r§e!"
            .toPattern()

    private val countdown = "§.[\\d❶❷❸]+".toPattern()
    private val gameStart = "§.§lGAME START(ING)?".toPattern()
    private val getReady = "§.(Get Ready|Good Luck)!".toPattern()

    private val objective = "§e§lLaunch Sheep".toPattern()
    private val shootTutorial = "§aLeft Click = Straight Shot §7\\|\\| §bRight Click = Gravity Shot".toPattern()

    private val effectSheep = "§cYou're in an? (Black Hole|Earthquake)!".toPattern()
    private val magicWoolSpawn = "§a§lMagic Wool".toPattern()
    private val magicWoolSubtitleSpawn = "§e§lShoot flaming wool for extra sheep/effects!".toPattern()

    private val death = "§cYOU DIED!".toPattern()

    private val winTitle = "§a§lVICTORY".toPattern()
    private val winSubtitle = "§6Your team was victorious!".toPattern()

    private val loseTitle = "§c§lDEFEAT!".toPattern()
    private val loseSubtitle = "§cYour team was defeated!".toPattern()

    private val queueCancel = "§cCANCELLED".toPattern()

    val tutorialChatSet =
        setOf(
            shootChatTutorial,
            gravityChatTutorial,
        )

    val tutorialSet =
        setOf(
            objective,
            shootTutorial,
            magicWoolSubtitleSpawn,
        )

    val gameStatusSet =
        setOf(
            countdown,
            gameStart,
            getReady,
            winTitle,
            winSubtitle,
            loseTitle,
            loseSubtitle,
            queueCancel,
            death,
        )

    val dynamicMessages =
        setOf(
            effectSheep,
            magicWoolSpawn,
        )

    @SubscribeEvent
    fun onChat(event: ChatReceiveEvent) {
        if (config.hiddenMessages.isEmpty()) return
        if (!HypixelGame.SHEEP_WARS.isPlaying()) return

        for (type in config.hiddenMessages) {
            type.chatPatterns?.forEach {
                if (it.matches(event.message)) {
                    event.cancel()
                    return
                }
            } ?: continue
        }
    }

    @SubscribeEvent
    fun onTitle(event: TitleReceivedEvent) {
        if (config.hiddenMessages.isEmpty()) return
        if (!HypixelGame.SHEEP_WARS.isPlaying()) return

        for (type in config.hiddenMessages) {
            type.titlePatterns.forEach {
                if (it.matches(event.formattedText)) {
                    event.cancel()
                    return
                }
            }
        }
    }

    enum class SheepWarsTutorialPattern(
        val prettyName: String,
        val titlePatterns: Set<Pattern>,
        val chatPatterns: Set<Pattern>? = null,
    ) {
        TUTORIAL("Tutorials", tutorialSet, tutorialChatSet),
        GAME_STATUS("Game Statuses", gameStatusSet),
        DYNAMIC("AoE Sheep + Wool Spawns", dynamicMessages),
        ;

        override fun toString(): String = prettyName
    }
}
