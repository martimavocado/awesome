package at.martimavocado.awesome.config.categories.speedbuilders

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class SpeedBuildersPreviewConfig {
    @Expose
    @ConfigOption(name = "Enabled", desc = "Highlights Magic Wool spawns")
    @ConfigEditorBoolean
    var enabled = true

    @Expose
    @ConfigOption(name = "Block Brightness", desc = "Highlights Magic Wool spawns")
    @ConfigEditorSlider(minValue = 0f, maxValue = 1f, minStep = 0.01f)
    var blockBrightness = 0.8f
}
