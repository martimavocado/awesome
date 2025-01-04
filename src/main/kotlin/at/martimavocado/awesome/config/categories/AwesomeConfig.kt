package at.martimavocado.awesome.config.categories

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.config.categories.sheepwars.SheepWarsConfig
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.Config
import io.github.notenoughupdates.moulconfig.annotations.Category

class AwesomeConfig: Config() {

    override fun getTitle(): String {
        return "awesome " + Awesome.version + " by §cmartimavocado§r, config by §5Moulberry §rand §5nea89"
    }

    override fun saveNow() {
        Awesome.configManager.save()
    }

    @Expose
    @Category(name = "sheep wars", desc = "i love sheep wars")
    var sheepWars = SheepWarsConfig()

    @Expose
    @Category(name = "chat", desc = "this is where we chat.")
    var chatter = ChatConfig()

    @Expose
    @Category(name = "chat commands", desc = "toggles for chat commands")
    var commands = ChatCommandsConfig()

    @Expose
    @Category(name = "debug stuff", desc = "you probably don't care about this")
    var debug = DebugConfig()
}
