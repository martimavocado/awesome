package at.martimavocado.awesome.config.categories.debug

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorText
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class DebugTitleConfig {
    @Expose
    @ConfigOption(name = "Title", desc = "Sets the title to be used with /awtesttitle.")
    @ConfigEditorText
    var title = ""

    @Expose
    @ConfigOption(name = "Subtitle", desc = "Sets the subtitle to be used with /awtesttitle.")
    @ConfigEditorText
    var subtitle = ""
}
