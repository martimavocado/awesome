package at.martimavocado.awesome.features

import at.martimavocado.awesome.utils.ChatUtils

object HelpCommands {
    private val HelpMessage = listOf(
        "§euse the following syntax in a party message",
        "§7?aw §6command §a'arg1' 'arg2'",
        "§bcommand list:",
        "§7-> §6warp §7- §fwarps the party",
        "§7-> §6transfer §7- §ftransfers the party to you",
        "§7-> §6say §a<message> §7- §fsend the message",
        "§7-> §6ban §a<reason> §a<id> §7- §fkicks the player with a hypixel ban message",
    )


    private val EmojiList = listOf(
        "§f§r§f§r§aFull Emoji List§r§r",
        "§6§r§6<3§r§f  -  §r§c§r§c❤§r§r§r",
        "§f§r§f§r§6:star:§r§f  -  §r§6§r§6✮§r§r§r",
        "§f§r§f§r§6:yes:§r§f  -  §r§a§r§a✔§r§r§r",
        "§f§r§f§r§6:no:§r§f  -  §r§c§r§c✖§r§r§r",
        "§f§r§f§r§6:java:§r§f  -  §r§b§r§b☕§r§r§r",
        "§f§r§f§r§6:arrow:§r§f  -  §r§e§r§e➜§r§r§r",
        "§f§r§f§r§6:shrug:§r§f  -  §r§e§r§e¯\\_(ツ)_/¯§r§r§r",
        "§f§r§f§r§6:tableflip:§r§f  -  §r§c(╯°□°）╯§r§f︵§r§7 ┻━┻§r§r",
        "§f§r§f§r§6o/§r§f  -  §r§d§r§d( ﾟ◡ﾟ)/§r§r§r",
        "§f§r§f§r§6:123§6:§r§f  -  §r§a1§r§e2§r§c3§r§r",
        "§f§r§f§r§6:totem:§r§f  -  §r§b☉§r§e_§r§b☉§r§r",
        "§f§r§f§r§6:typing:§r§f  -  §r§e✎§r§6...§r§r",
        "§f§r§f§r§6:maths:§r§f  -  §r§a√§r§e§l(§r§aπ§r§a§l+x§r§e§l)§r§a§l=§r§c§lL§r§r",
        "§f§r§f§r§6:snail:§r§f  -  §r§e@§r§a'§r§e-§r§a'§r§r",
        "§f§r§f§r§6:thinking:§r§f  -  §r§6(§r§a0§r§6.§r§ao§r§c?§r§6)§r§r",
        "§f§r§f§r§6:gimme:§r§f  -  §r§b§r§b༼つ◕_◕༽つ§r§r§r",
        "§f§r§f§r§6:wizard:§r§f  -  §r§e(§r§b'§r§e-§r§b'§r§e)⊃§r§c━§r§d☆ﾟ.*･｡ﾟ§r§r",
        "§f§r§f§r§6:pvp:§r§f  -  §r§e§r§e⚔§r§r§r",
        "§f§r§f§r§6:peace:§r§f  -  §r§a§r§a✌§r§r§r",
        "§f§r§f§r§6:oof:§r§f  -  §r§c§l§r§c§lOOF§r§r§r",
        "§f§r§f§r§6:puffer:§r§f  -  §r§e§l§r§e§l<('O')>§r§r§r",
        "§6§r§6:dog:§r§f  -  §r§6§r§6(ᵔᴥᵔ)§r§r§r",
        "§6§r§6:dj:§r§f  -  §r§9ヽ§r§5(§r§d⌐§r§c■§r§6_§r§e■§r§b)§r§3ノ§r§9♬§r§r",
        "§6§r§6h/§r§f  -  §r§e§r§eヽ(^◇^*)/§r§r§r",
        "§6§r§6:sloth:§r§f  -  §r§6(§r§8・§r§6⊝§r§8・§r§6)§r§r",
        "§6§r§6:cat:§r§f  -  §r§e= §r§b＾● ⋏ ●＾§r§e =§r§r",
        "§6§r§6:snow:§r§f  -  §r§b§r§b☃§r§r§r",
        "§6§r§6§r^_^§r§r§f  -  §r§a§r§a^_^§r§r§r",
        "§6§r§6:cute:§r§f  -  §r§e(§r§a✿§r§e◠‿◠)§r§r",
        "§6§r§6§r^-^§r§r§f  -  §r§a§r§a^-^§r§r§r",
        "§6§r§6:dab:§r§f  -  §r§d<§r§eo§r§d/§r§r",
        "§6§r§6:yey:§r§f  -  §r§a§r§aヽ (◕◡◕) ﾉ§r§r§r"
    )

    fun printMessage(s: String) {
        when (s) {
            "emoji" -> {
                EmojiList.forEach { line ->
                    ChatUtils.sendChatClient(line)
                }
            }

            "help" -> {
                HelpMessage.forEach { line ->
                    ChatUtils.sendChatClient(line)
                }
            }
        }
    }
}