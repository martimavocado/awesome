package at.martimavocado.awesome.config.categories

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Accordion
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class ChatConfig {
    @Expose
    @ConfigOption(
        name = "Emoji Replacer",
        desc =
            "Replaces Emojis to emulate Hypixel" +
                "Example: §r§f<3 §r§6➜§r §r§f❤",
    )
    @Accordion
    var emojiReplace = EmojiReplacerConfig()

    @Expose
    @ConfigOption(
        name = "Recolor Emojis",
        desc =
            "Recolors colorless emojis\n" +
                "Example: §r§f❤ §r§6➜§r §r§c❤§r",
    )
    @ConfigEditorBoolean
    var colorEmoji = true

    @Expose
    @ConfigOption(
        name = "Shorten Channel Names",
        desc =
            "Shortens channel names\n" +
                "Example: §9Party §8> §r§6➜§r §9P §8>",
    )
    @ConfigEditorBoolean
    var shortChannels = true

    @Expose
    @ConfigOption(
        name = "Chat Logger",
        desc =
            "Logs sent messages\n" +
                "Saved to config/awesome/messages.txt",
    )
    @ConfigEditorBoolean
    var chatLogger = true
}
