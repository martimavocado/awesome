package at.martimavocado.awesome.config.categories.sheepwars

import at.martimavocado.awesome.config.guieditor.data.GuiPosition
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigLink
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class OnboardingAlertConfig {
    @Expose
    @ConfigOption(
        name = "Show Title",
        desc = "Shows a Title when the §6§lOnboarding Sheep§7 spawns.",
    )
    @ConfigEditorBoolean
    var enableTitle = true

    @Expose
    @ConfigOption(
        name = "Show Timer",
        desc = "Shows the remaining time until a §6§lOnboarding Sheep§7 spawns.",
    )
    @ConfigEditorBoolean
    var enableTimer = true

    @Expose
    @ConfigLink(owner = OnboardingAlertConfig::class, field = "enableTimer")
    var timerPosition = GuiPosition(100, 100)
}
