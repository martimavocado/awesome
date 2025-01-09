package at.martimavocado.awesome.features.chat

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.chat.ChatSendEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ChatUtils
import net.minecraft.network.play.client.C01PacketChatMessage
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object EmojiReplacer {
    private val config get() = Awesome.config.chatter.emojiReplace

    private val mvpPlusPlus =
        setOf(
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
            ":puffer:" to "<('O')>",
        )
    private val gifted5 =
        setOf(
            ":cute:" to "(✿◠‿◠)",
        )
    private val gifted20 =
        setOf(
            ":dab:" to "<o/",
            ":yey:" to "ヽ (◕◡◕) ﾉ",
        )
    private val gifted50 =
        setOf(
            ":dj:" to "ヽ(⌐■_■)ノ♬",
            ":dog:" to "(ᵔᴥᵔ)",
        )
    private val gifted100 =
        setOf(
            ":cat:" to "= ＾● ⋏ ●＾ =",
            "h/" to "ヽ(^◇^*)/",
        )
    private val gifted200 =
        setOf(
            ":sloth:" to "(・⊝・)",
            ":snow:" to "☃",
        )

    @SubscribeEvent
    fun onChatSend(event: ChatSendEvent) {
        if (config.enabled) {
            val finalSet = mutableSetOf<Pair<String, String>>()
            if (config.mvp) finalSet += mvpPlusPlus
            val giftedRanks = config.giftedRanks.gifts

            if (giftedRanks < 5) finalSet += gifted5
            if (giftedRanks < 20) finalSet += gifted20
            if (giftedRanks < 50) finalSet += gifted50
            if (giftedRanks < 100) finalSet += gifted100
            if (giftedRanks < 200) finalSet += gifted200

            event.isCanceled = chatEdit(event.message, finalSet.toSet())
        }
    }

    private fun chatEdit(
        message: String,
        emojis: Set<Pair<String, String>>,
    ): Boolean {
        if (!emojis.any { message.contains(it.first) }) return false

        var message = message
        emojis.forEach { (search, replace) ->
            val regex = """(?<=^|\s)$search(?=\s|$)""".toRegex()
            message = message.replace(regex, replace)
        }

        ChatUtils.sendChatPacket(C01PacketChatMessage(message))
        return true
    }
}
