package at.martimavocado.awesome.config.categories

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class ChatCommandsConfig {
    @Expose
    @ConfigOption(name = "main toggle", desc = "this is the main toggle")
    @ConfigEditorBoolean
    var enabled = true

    @Expose
    @ConfigOption(name = "party warping", desc = "enables ?aw warp")
    @ConfigEditorBoolean
    var warping = true

    @Expose
    @ConfigOption(name = "party transfering", desc = "enables ?aw transfer")
    @ConfigEditorBoolean
    var transfer = true

    @Expose
    @ConfigOption(name = "chat", desc = "enables ?aw say something")
    @ConfigEditorBoolean
    var say = true

    @Expose
    @ConfigOption(name = "say hi", desc = "enables ?aw hi")
    @ConfigEditorBoolean
    var hi = true

    @Expose
    @ConfigOption(name = "ban", desc = "enables being banned")
    @ConfigEditorBoolean
    var ban = true
}
