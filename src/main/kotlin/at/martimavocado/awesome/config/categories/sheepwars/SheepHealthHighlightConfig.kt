package at.martimavocado.awesome.config.categories.sheepwars

import at.martimavocado.awesome.config.elements.ConfigColor
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class SheepHealthHighlightConfig {
    @Expose
    @ConfigOption(
        name = "Enabled",
        desc =
            "Highlights players at low health." +
                "\n§cRed players are between 0 HP and 5 HP." +
                "\n§eYellow Players are between 5 HP and 10 HP.",
    )
    @ConfigEditorBoolean
    var enabled = true

    @Expose
    @ConfigOption(
        name = "Dynamic Color",
        desc = "Blends the two colors below depending on the player's health.",
    )
    @ConfigEditorBoolean
    var dynamicColor = false

    @Expose
    @ConfigOption(
        name = "Low HP Color",
        desc = "Color used for when players are near 0 HP.",
    )
    @ConfigEditorColour
    var lowHPColor = ConfigColor(red = 1.0, alpha = 0.3).toString()

    @Expose
    @ConfigOption(
        name = "High HP Color",
        desc = "Color used for when players are near 20 HP.",
    )
    @ConfigEditorColour
    var highHPColor = ConfigColor(blue = 1.0, alpha = 0.3).toString()
}
