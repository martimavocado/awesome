package at.martimavocado.awesome.features.chat

import at.martimavocado.awesome.Awesome
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ChatFeatures {
    private val emojis = arrayOf(
            "❤" to "§r§c❤§r",
            "✮" to "§r§6✮§r",
            "✔" to "§r§a✔§r",
            "✖" to "§r§c✖§r",
            "☕" to "§r§b☕§r",
            "➜" to "§r§e➜§r",
            "¯\\_(ツ)_/¯" to "§r§e¯\\_(ツ)_/¯§r",
            "(╯°□°）╯︵┻━┻" to "§r§c(╯°□°）╯§f︵§7 ┻━┻§r",
            "( ﾟ◡ﾟ)/" to "§r§d( ﾟ◡ﾟ)/§r",
            "123" to "§r§a1§e2§c3§r",
            "☉_☉" to "§r§b☉§e_§b☉§r",
            "✎..." to "§r§e✎§6...§r",
            "√(π+x)=L" to "§r§a√§e§l(§aπ§a§l+x§e§l)§a§l=§c§lL§r",
            "@\'-\'" to "§r§e@§a\'§e-§a\'§r",
            "(0.o?)" to "§r§6(§a0§6.§ao§c?§6)§r",
            "༼つ◕_◕༽つ" to "§r§b༼つ◕_◕༽つ§r",
            "('-')⊃━☆ﾟ.*･｡ﾟ" to "§r§e(§b'§e-§b'§e)⊃§c━§d☆ﾟ.*･｡ﾟ§r",
            "⚔" to "§r§e⚔§r",
            "✌" to "§r§a✌§r",
            "OOF" to "§r§c§lOOF§r",
            "<('O')>" to "§r§e§l<('O')>§r",
            "(ᵔᴥᵔ)" to "§r§6(ᵔᴥᵔ)§r",
            "ヽ (◕◡◕) ﾉ" to "§r§aヽ (◕◡◕) ﾉ§r",
            "= ＾● ⋏ ●＾ =" to "§r§e= §b＾● ⋏ ●＾§e =§r",
            "☃" to "§r§b☃§r",
            "(✿◠‿◠)" to "§r§e(§a✿§e◠‿◠)§7§r",
            "ヽ(^◇^*)/" to "§r§eヽ(^◇^*)/§r",
            "ヽ(⌐■_■)ノ♬" to "§r§9ヽ§5(§d⌐§c■§6_§e■§b)§3ノ§9♬§r",
            "(・⊝・)" to "§r§6(§8・§6⊝§8・§6)§r",
            "^-^" to "§r§a^-^§r",
            "<o/" to "§r§d<§eo§d/§r",
            "^_^" to "§r§a^_^§r"
    )

    private val channels = arrayOf(
            "§9Party §8>" to "§r§9P §8>",
            "§2Guild >" to "§2G >",
            "§3Officer >" to "§3 O >"
    )

    @SubscribeEvent
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (Awesome.config.debug.rawChat) {
            println("\nOriginal Message: ${event.message}\nType: ${event.type}")
        }
        if (Awesome.config.chatter.colorEmoji) event.message = replace(event ,emojis)
        if (Awesome.config.chatter.shortChannels) event.message = replace(event ,channels)
    }

    private fun replace (event:ClientChatReceivedEvent, array: Array<Pair<String, String>>): IChatComponent {
        var oldMessage = event.message.formattedText
        array.forEach { (search, replace) ->
            oldMessage = oldMessage.replace(search, replace)
        }
        val newMessage = ChatComponentText(oldMessage)
        if (event.message.siblings.isEmpty()) {
            newMessage.chatStyle = event.message.chatStyle
        } else newMessage.chatStyle = event.message.siblings[0].chatStyle
        return newMessage
    }
}
