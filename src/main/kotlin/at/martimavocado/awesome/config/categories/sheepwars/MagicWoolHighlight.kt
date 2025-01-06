package at.martimavocado.awesome.config.categories.sheepwars

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class MagicWoolHighlight {
    @Expose
    @ConfigOption(name = "Enabled", desc = "Highlights Magic Wool spawns")
    @ConfigEditorBoolean
    var enabled = true

    @Expose
    @ConfigOption(name = "Beacon", desc = "Adds a beacon effect")
    @ConfigEditorBoolean
    var beacon = true

    @Expose
    @ConfigOption(name = "Color Match", desc = "Makes the highlight be the same color as the current wool")
    @ConfigEditorBoolean
    var colorMatch = true

    @Expose
    @ConfigOption(name = "Show in Spectator", desc = "Highlights the Magic Wool even in spectator mode")
    @ConfigEditorBoolean
    var spectator = true

    @Expose
    @ConfigOption(
        name = "Color",
        desc = "Makes the highlight be a specific color\n" + "Requires Color Match to be disabled.",
    )
    @ConfigEditorColour
    var color = "0:0:0:0:0"
}
