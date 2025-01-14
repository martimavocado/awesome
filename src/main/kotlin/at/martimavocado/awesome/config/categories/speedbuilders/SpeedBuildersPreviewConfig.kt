package at.martimavocado.awesome.config.categories.speedbuilders

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class SpeedBuildersPreviewConfig {
    @Expose
    @ConfigOption(name = "Enabled", desc = "Shows the solution for the current build")
    @ConfigEditorBoolean
    var enabled = true

    @Expose
    @ConfigOption(name = "Block Brightness", desc = "Sets the block's brightness")
    @ConfigEditorSlider(minValue = 0f, maxValue = 1f, minStep = 0.01f)
    var blockBrightness = 0.8f
}
