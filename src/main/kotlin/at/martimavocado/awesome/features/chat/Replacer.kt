package at.martimavocado.awesome.features.chat

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.ClientMessageEvent
import at.martimavocado.awesome.utils.ChatUtils
import net.minecraft.network.play.client.C01PacketChatMessage
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class Replacer {
    private val mvpPlusPlus = arrayOf(
        "<3" to "❤",
        ":star" to "✮",
        ":yes:" to "✔",
        ":no:" to "✖",
        ":java:" to "☕",
        ":arrow:" to "➜",
        ":shrug:" to "¯\\_(ツ)_/¯",
        ":tableflip:" to "(╯°□°）╯︵┻━┻",
        "o/" to "( ﾟ◡ﾟ)/",
//        ":123:" to "",
        ":totem:" to "☉_☉",
        ":typing:" to "✎...",
        ":maths:" to "√(π+x)=L",
        ":snail:" to "@\'-\'",
        ":thinking:" to "(0.o?)",
        ":gimme:" to "༼つ◕_◕༽つ",
        ":wizard:" to "('-')⊃━☆ﾟ.*･｡ﾟ",
        ":pvp:" to "⚔",
        ":peace:" to "✌",
        ":oof:" to "OOF",
        ":puffer:" to "<('O')>"
    )

    private val gifted5 = arrayOf(
        "^-^" to "^-^",
        ":cute:" to "(✿◠‿◠)"
    )

    private val gifted20 = arrayOf(
        ":dab:" to "<o/",
        ":yey:" to "ヽ (◕◡◕) ﾉ"
    )

    private val gifted50 = arrayOf(
        ":dj:" to "ヽ(⌐■_■)ノ♬",
        ":dog:" to "(ᵔᴥᵔ)"
    )

    private val gifted100 = arrayOf(
        ":cat:" to "= ＾● ⋏ ●＾ =",
        "h/" to "ヽ(^◇^*)/"
    )
    private val gifted200 = arrayOf(
        ":sloth:" to "(・⊝・)",
        ":snow:" to "☃"
    )

    @SubscribeEvent
    fun onChatSend(event: ClientMessageEvent) {
        if (Awesome.config.chatter.emojiReplace.mvp) chatEdit(event, mvpPlusPlus)
        if (Awesome.config.chatter.emojiReplace.five) chatEdit(event, gifted5)
        if (Awesome.config.chatter.emojiReplace.twenty) chatEdit(event, gifted20)
        if (Awesome.config.chatter.emojiReplace.fifty) chatEdit(event, gifted50)
        if (Awesome.config.chatter.emojiReplace.hundred) chatEdit(event, gifted100)
        if (Awesome.config.chatter.emojiReplace.twoHundred) chatEdit(event, gifted200)
        return
    }

    private fun chatEdit(event: ClientMessageEvent, array: Array<Pair<String, String>>) {
        if (ChatUtils.inArray(event.message, array)) {
            event.isCanceled = true
            var newMessage = event.message
            array.forEach { (search, replace) ->
                newMessage = newMessage.replace(search, replace)
            }
            ChatUtils.sendChatPacket(C01PacketChatMessage(newMessage))
        } else return
    }
}