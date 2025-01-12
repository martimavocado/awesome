package at.martimavocado.awesome.config.categories

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class DebugConfig {
    @Expose
    @ConfigOption(name = "Log Messages", desc = "Prints a copy of all chat messages to log.")
    @ConfigEditorBoolean
    var printMessages = false

    @Expose
    @ConfigOption(name = "Log ChatComponents", desc = "Prints a copy messages' chat components to log.")
    @ConfigEditorBoolean
    var printChatComponents = false

    @Expose
    @ConfigOption(name = "Log Expiry Time", desc = "Deletes logs after a certain amount of days.")
    @ConfigEditorSlider(minValue = 1f, maxValue = 30f, minStep = 1f)
    var logExpiryTime = 14

    @Expose
    @ConfigOption(name = "Debug Messages", desc = "Shows debug messages.")
    @ConfigEditorBoolean
    var debugMessages = false

    @Expose
    @ConfigOption(name = "replace color code thing with &", desc = "helps with debugging sometimes :shrug:")
    @ConfigEditorBoolean
    var colorCodes = false

    @Expose
    @ConfigOption(name = "hypixel modapi", desc = "shows when/what modapi packets are received")
    @ConfigEditorBoolean
    var modAPI = false
}
