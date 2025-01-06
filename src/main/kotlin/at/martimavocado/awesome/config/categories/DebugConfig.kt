package at.martimavocado.awesome.config.categories

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class DebugConfig {
    @Expose
    @ConfigOption(name = "raw messages", desc = "prints a copy of raw messages to stdout")
    @ConfigEditorBoolean
    var rawMessages = false

    @Expose
    @ConfigOption(name = "only chat", desc = "makes it so raw messages only works in chat")
    @ConfigEditorBoolean
    var onlyChat = true

    @Expose
    @ConfigOption(name = "command logger", desc = "logs commands sent")
    @ConfigEditorBoolean
    var commandLogs = false

    @Expose
    @ConfigOption(name = "replace color code thing with &", desc = "helps with debugging sometimes :shrug:")
    @ConfigEditorBoolean
    var colorCodes = false

    @Expose
    @ConfigOption(name = "substring color", desc = "i hate minecraft colro code")
    @ConfigEditorBoolean
    var debugColors = false

    @Expose
    @ConfigOption(name = "store titles", desc = "prints titles/subtitles to stdout")
    @ConfigEditorBoolean
    var logTitles = false

    @Expose
    @ConfigOption(name = "debug commands", desc = "shows messages that weren't commands")
    @ConfigEditorBoolean
    var figureOutCommands = false

    @Expose
    @ConfigOption(name = "hypixel modapi", desc = "shows when/what modapi packets are received")
    @ConfigEditorBoolean
    var modAPI = false
}
