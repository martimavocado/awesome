package at.martimavocado.awesome.config.categories

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.config.categories.sheepwars.SheepWarsConfig
import at.martimavocado.awesome.config.categories.speedbuilders.SpeedBuildersConfig
import at.martimavocado.awesome.config.guieditor.data.GuiPosition
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.Config
import io.github.notenoughupdates.moulconfig.annotations.Category

class AwesomeConfig : Config() {
    override fun getTitle(): String = "awesome " + Awesome.version + " by §cmartimavocado§r, config by §5Moulberry §rand §5nea89"

    override fun saveNow() {
        Awesome.configManager.save()
    }

    @Expose
    @Category(name = "About", desc = "updater settings + credits")
    var about = AboutConfig()

    @Expose
    @Category(name = "Sheep Wars", desc = "i love sheep wars")
    var sheepWars = SheepWarsConfig()

    @Expose
    @Category(name = "Speed Builders", desc = "this game is kinda cool")
    var speedBuilders = SpeedBuildersConfig()

    @Expose
    @Category(name = "Chat", desc = "this is where we chat.")
    var chatter = ChatConfig()

    @Expose
    @Category(name = "Debug", desc = "you probably don't care about this")
    var debug = DebugConfig()

    @Expose
    var pos = GuiPosition(100, 100, 1.1)
}
