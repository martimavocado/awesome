package at.martimavocado.awesome.config.categories

import at.martimavocado.awesome.utils.system.PlatformUtils
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Accordion
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorButton
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class AboutConfig {
    @Expose
    @ConfigOption(name = "Check for Updates", desc = "Automatically check for updates on each startup")
    @ConfigEditorBoolean
    var autoUpdates = true

    @Expose
    @ConfigOption(name = "Auto Updates", desc = "Automatically download new version on each startup")
    @ConfigEditorBoolean
    var fullAutoUpdates = false

    @Expose
    @ConfigOption(name = "Used Software", desc = "Information about used software and licenses")
    @Accordion
    var licenses = Licenses()

    class Licenses {
        @ConfigOption(name = "SkyHanni", desc = "SkyHanni is available under the LGPL 2.1 license")
        @ConfigEditorButton(buttonText = "Source")
        var skyhanni = Runnable { PlatformUtils.openBrowser("https://github.com/hannibal002/SkyHanni") }

        @ConfigOption(name = "MoulConfig", desc = "MoulConfig is available under the LGPL 3.0 License or later version")
        @ConfigEditorButton(buttonText = "Source")
        var moulConfig = Runnable { PlatformUtils.openBrowser("https://github.com/NotEnoughUpdates/MoulConfig") }

        @ConfigOption(name = "Forge", desc = "Forge is available under the LGPL 3.0 license")
        @ConfigEditorButton(buttonText = "Source")
        var forge = Runnable { PlatformUtils.openBrowser("https://github.com/MinecraftForge/MinecraftForge") }

        @ConfigOption(name = "LibAutoUpdate", desc = "LibAutoUpdate is available under the BSD 2 Clause License")
        @ConfigEditorButton(buttonText = "Source")
        var libAutoUpdate = Runnable { PlatformUtils.openBrowser("https://git.nea.moe/nea/libautoupdate/") }

        @ConfigOption(name = "Mixin", desc = "Mixin is available under the MIT License")
        @ConfigEditorButton(buttonText = "Source")
        var mixin = Runnable { PlatformUtils.openBrowser("https://github.com/SpongePowered/Mixin/") }
    }
}
