package at.martimavocado.awesome.features.chat

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.hooks.sendChatMessage
import at.martimavocado.awesome.utils.ChatUtils
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class ChatFeatures {
    private val emojis = arrayOf(
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
            "^_^" to "§r§a^_^"
    )

    private val channels = arrayOf(
            "§9Party §8>" to "§9P §8>",
            "§2Guild >" to "§2G >",
            "§3Officer >" to "§3O >",
            "§aFriend >" to "§aF >"
    )

    private val color = arrayOf(
            "§" to "&"
    )

    private val resettingCodes = arrayOf(
        "§0",
        "§1",
        "§2",
        "§3",
        "§4",
        "§5",
        "§6",
        "§7",
        "§8",
        "§9",
        "§a",
        "§b",
        "§c",
        "§d",
        "§e",
        "§f"
    )

    private val formattingCodes = arrayOf(
        "§k",
        "§l",
        "§m",
        "§n",
        "§o",
        "§r"
    )

    @SubscribeEvent
    fun onChatReceive(event: ClientChatReceivedEvent) {
        if (Awesome.config.debug.rawChat) {
            println("\nOriginal Message: ${event.message.formattedText}\nType: ${event.type}")
        }
        if (Awesome.config.chatter.colorEmoji) event.message = replace(event ,emojis)
        if (Awesome.config.chatter.shortChannels) event.message = replace(event ,channels)
        if (Awesome.config.debug.colorCodes) event.message = replace(event, color)
    }

    private fun replace (event:ClientChatReceivedEvent, array: Array<Pair<String, String>>): IChatComponent {
        if (ChatUtils.inArray(event.message.unformattedText, array)) {
            var oldMessage = event.message.formattedText
            array.forEach { (search, replace) ->
                if (oldMessage.contains(search)) {
                    val oldColor = findColor(oldMessage, search)
                   oldMessage = if (oldColor == "shrug") oldMessage.replace(search, replace)
                   else oldMessage.replace(search, "$replace$oldColor")
                }
            }
            val newMessage = ChatComponentText(oldMessage)
            if (event.message.siblings.isEmpty()) {
                newMessage.chatStyle = event.message.chatStyle
            } else newMessage.chatStyle = event.message.siblings[0].chatStyle
            return newMessage
        }
        return event.message
    }

    private fun findColor(message: String, search: String): String {
        val lastIndex = message.indexOf(search)
        val firstIndex = findFirstIndex(message, lastIndex)
        val subString = message.substring(firstIndex,lastIndex)
        if (subString.isNotEmpty() && subString.last() == '§') {
            sendChatMessage("§c[Awesome] §fSomething went wrong, report the error in logs")
            println("[Debug] Last character was §, how does this happen '$message'")
            return "shrug"
        }
        if (Awesome.config.debug.debugColors) {
            println("[Debug] message: $message")
            println("[Debug] substring: $subString")
            println("[Debug] index: ${subString.lastIndexOf("§")+1}")
            println("[Debug] code: ${subString[subString.lastIndexOf("§")+1]}")
        }
        return handleColor(subString)
    }

    private fun handleColor(subString: String): String {
        var finalColors = "§${subString[1]}"
        for (code in formattingCodes) {
            if (subString.contains(code)) {
                finalColors += code
            }
        }
        return finalColors
    }

    private fun findFirstIndex(message: String, lastIndex: Int): Int {
        val subString = message.substring(0, lastIndex)
        var maxIndex = 0

        resettingCodes.forEach { code ->
            val index = subString.lastIndexOf(code)
            if (index > maxIndex) maxIndex = index
        }
        return maxIndex
    }
}
