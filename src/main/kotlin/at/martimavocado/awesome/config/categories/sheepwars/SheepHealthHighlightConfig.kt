package at.martimavocado.awesome.config.categories.sheepwars

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
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
}
