package at.martimavocado.awesome

import at.martimavocado.awesome.commands.CommandManager
import at.martimavocado.awesome.config.ConfigManager
import at.martimavocado.awesome.config.categories.AwesomeConfig
import at.martimavocado.awesome.events.ShowTitleEvent
import at.martimavocado.awesome.features.bedwars.ShowStats
import at.martimavocado.awesome.features.chat.EmojiColorer
import at.martimavocado.awesome.features.chat.EmojiReplacer
import at.martimavocado.awesome.features.chat.MessageLogger
import at.martimavocado.awesome.features.commands.ChatCommands
import at.martimavocado.awesome.hooks.ShowTitleHook
import at.martimavocado.awesome.utils.CommandsSentToServerLogger
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = Awesome.MOD_ID, useMetadata = true, name = "Awesome", version = "1.3.1", clientSideOnly = true)
class Awesome {
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        configManager = ConfigManager()
        MinecraftForge.EVENT_BUS.register(configManager)
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        CommandManager()

        MinecraftForge.EVENT_BUS.register(EmojiColorer())
        MinecraftForge.EVENT_BUS.register(EmojiReplacer())
        MinecraftForge.EVENT_BUS.register(CommandsSentToServerLogger())
        MinecraftForge.EVENT_BUS.register(ChatCommands())
        MinecraftForge.EVENT_BUS.register(ShowStats())
        MinecraftForge.EVENT_BUS.register(ShowTitleHook())
        MinecraftForge.EVENT_BUS.register(MessageLogger())

    }

    companion object {
        lateinit var configManager: ConfigManager
        const val MOD_ID = "awesome"

        @JvmStatic
        val version: String
            get() = Loader.instance().indexedModList[MOD_ID]!!.version

        val config: AwesomeConfig
            get() = configManager.config ?: error("config is null")
    }
}
