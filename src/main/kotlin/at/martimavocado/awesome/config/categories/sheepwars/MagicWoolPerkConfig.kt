package at.martimavocado.awesome.config.categories.sheepwars

import at.martimavocado.awesome.data.HypixelGame
import at.martimavocado.awesome.features.sheepwars.data.SheepWarsPowerUp
import at.martimavocado.awesome.utils.render.GuiPosition
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDraggableList
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class MagicWoolPerkConfig {
    @Expose
    @ConfigOption(name = "Show GUI", desc = "Shows a GUI with the current wool's perk")
    @ConfigEditorBoolean
    var perkGUI = true

    @Expose
    var perkPosition = GuiPosition(10, 10, HypixelGame.SHEEP_WARS)

    @Expose
    @ConfigOption(name = "Ping on shoot", desc = "Pings when the wool has a good perk" +
            "\nDisabled when not in line of sight or dead")
    @ConfigEditorBoolean
    var shootPing = false

    @Expose
    @ConfigOption(name = "Ping Delay", desc = "Defines how often you should get pinged in ticks" + "\n§8(Magic Wool lifespan is ~40 ticks)")
    @ConfigEditorSlider(minValue = 1f, maxValue = 40f, minStep = 1f)
    var pingDelay = 5

    @Expose
    @ConfigOption(name = "Good Perks", desc = "Makes the highlight be the same color as the current wool §c[unused]")
    @ConfigEditorDraggableList
    var goodPerks: MutableList<SheepWarsPowerUp> = SheepWarsPowerUp.defaultGoodPerks.toMutableList<SheepWarsPowerUp>()

    @Expose
    @ConfigOption(
        name = "Color",
        desc = "Makes the highlight be a specific color\n" + "Requires Color Match to be disabled.",
    )
    @ConfigEditorColour
    var color = "0:0:0:0:0"
}
