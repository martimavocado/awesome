package at.martimavocado.awesome.config.categories.sheepwars

import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class OnboardingAlertConfig {
    @Expose
    @ConfigOption(
        name = "Show Title",
        desc = "Shows a Title when the §6§lOnboarding Sheep§7 spawns.",
    )
    @ConfigEditorBoolean
    var enableTitle = true
}
