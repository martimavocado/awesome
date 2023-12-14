package com.example

import com.example.commands.CommandManager
import com.example.config.ConfigManager
import com.example.config.categories.ExampleModConfig
import com.example.features.ChatFeatures
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = ExampleMod.MOD_ID, useMetadata = true)
class ExampleMod {

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        configManager = ConfigManager()
        MinecraftForge.EVENT_BUS.register(configManager)
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        CommandManager()

        MinecraftForge.EVENT_BUS.register(ChatFeatures())
    }

    companion object {
        lateinit var configManager: ConfigManager
        const val MOD_ID = "examplemod"

        @JvmStatic
        val version: String
            get() = Loader.instance().indexedModList[MOD_ID]!!.version

        val config: ExampleModConfig
            get() = configManager.config ?: error("config is null")
    }
}
