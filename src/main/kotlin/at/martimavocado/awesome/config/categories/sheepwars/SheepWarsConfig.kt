package at.martimavocado.awesome.config.categories.sheepwars

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Category

class SheepWarsConfig {
    @Expose
    @Category(name = "Magic Wool Highlighter", desc = "")
    var magicWoolHighlight = MagicWoolHighlightConfig()

    @Expose
    @Category(name = "Magic Wool Perks", desc = "")
    var magicWoolPerk = MagicWoolPerkConfig()
}
