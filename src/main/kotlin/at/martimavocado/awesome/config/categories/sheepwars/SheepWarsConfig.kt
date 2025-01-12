package at.martimavocado.awesome.config.categories.sheepwars

import at.martimavocado.awesome.features.sheepwars.TutorialHider
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Accordion
import io.github.notenoughupdates.moulconfig.annotations.Category
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDraggableList
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class SheepWarsConfig {
    @Expose
    @Category(name = "Magic Wool Highlighter", desc = "")
    var magicWoolHighlight = MagicWoolHighlightConfig()

    @Expose
    @Category(name = "Magic Wool Perks", desc = "")
    var magicWoolPerk = MagicWoolPerkConfig()

    @Expose
    @ConfigOption(name = "Health Highlight", desc = "")
    @Accordion
    var healthHighlight = SheepHealthHighlightConfig()

    @Expose
    @ConfigOption(name = "Hide Tutorials", desc = "Hides titles and messages")
    @ConfigEditorDraggableList
    var hiddenMessages = mutableListOf(TutorialHider.SheepWarsTutorialPattern.TUTORIAL)
}
