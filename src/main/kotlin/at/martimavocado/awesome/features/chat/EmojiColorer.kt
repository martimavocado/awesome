package at.martimavocado.awesome.features.chat

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.chat.PlayerChatEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.minecraft.util.ChatComponentText
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object EmojiColorer {
    private val emojis =
        setOf(
            "❤" to "§r§c❤",
            "✮" to "§r§6✮",
            "✔" to "§r§a✔",
            "✖" to "§r§c✖",
            "☕" to "§r§b☕",
            "➜" to "§r§e➜",
            "¯\\_(ツ)_/¯" to "§r§e¯\\_(ツ)_/¯",
            "(╯°□°）╯︵┻━┻" to "§r§c(╯°□°）╯§f︵§7 ┻━┻",
            "( ﾟ◡ﾟ)/" to "§r§d( ﾟ◡ﾟ)/",
            "123" to "§r§a1§e2§c3",
            "☉_☉" to "§r§b☉§e_§b☉",
            "✎..." to "§r§e✎§6...",
            "√(π+x)=L" to "§r§a√§e§l(§aπ§a§l+x§e§l)§a§l=§c§lL",
            "@\'-\'" to "§r§e@§a\'§e-§a\'",
            "(0.o?)" to "§r§6(§a0§6.§ao§c?§6)",
            "༼つ◕_◕༽つ" to "§r§b༼つ◕_◕༽つ",
            "('-')⊃━☆ﾟ.*･｡ﾟ" to "§r§e(§b'§e-§b'§e)⊃§c━§d☆ﾟ.*･｡ﾟ",
            "⚔" to "§r§e⚔",
            "✌" to "§r§a✌",
            "OOF" to "§r§c§lOOF",
            "<('O')>" to "§r§e§l<('O')>",
            "(ᵔᴥᵔ)" to "§r§6(ᵔᴥᵔ)",
            "ヽ (◕◡◕) ﾉ" to "§r§aヽ (◕◡◕) ﾉ",
            "= ＾● ⋏ ●＾ =" to "§r§e= §b＾● ⋏ ●＾§e =",
            "☃" to "§r§b☃",
            "(✿◠‿◠)" to "§r§e(§a✿§e◠‿◠)§7",
            "ヽ(^◇^*)/" to "§r§eヽ(^◇^*)/",
            "ヽ(⌐■_■)ノ♬" to "§r§9ヽ§5(§d⌐§c■§6_§e■§b)§3ノ§9♬",
            "(・⊝・)" to "§r§6(§8・§6⊝§8・§6)",
            "^-^" to "§r§a^-^",
            "<o/" to "§r§d<§eo§d/",
            "^_^" to "§r§a^_^",
        )

    val config get() = Awesome.config

    @SubscribeEvent
    fun onChat(event: PlayerChatEvent) {
        if (config.chatter.colorEmoji) replaceEmoji(event)
        if (config.debug.colorCodes) replaceColorCodes(event)
    }

    private fun replaceEmoji(event: PlayerChatEvent) {
        if (!emojis.any { event.message.contains(it.first) }) return

        var message = event.message
        emojis.forEach { (find, replace) ->
            message = message.replace(find, "$replace§r")
        }

        event.chatComponent = listOf(ChatComponentText(message))
    }

    private fun replaceColorCodes(event: PlayerChatEvent) {
        var message = event.message.replace('§', '&')

        event.chatComponent = listOf(ChatComponentText(message))
    }
}
