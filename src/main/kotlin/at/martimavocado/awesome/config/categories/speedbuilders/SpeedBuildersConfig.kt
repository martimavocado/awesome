package at.martimavocado.awesome.config.categories.speedbuilders

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Category

class SpeedBuildersConfig {
    @Expose
    @Category(name = "Block Preview", desc = "is this cheating?")
    var blockPreview = SpeedBuildersPreviewConfig()
}
