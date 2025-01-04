package at.martimavocado.awesome

import at.martimavocado.awesome.commands.CommandManager
import at.martimavocado.awesome.config.ConfigManager
import at.martimavocado.awesome.config.categories.AwesomeConfig
import at.martimavocado.awesome.events.AwesomeTickEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.loadmodule.LoadedModules
import io.github.notenoughupdates.moulconfig.gui.GuiScreenElementWrapper
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Mod(
    modid = Awesome.MOD_ID,
    useMetadata = true,
    name = "Awesome",
    version = Awesome.MOD_VERSION,
    guiFactory = "at.martimavocado.awesome.config.ConfigGuiForgeInterop",
    clientSideOnly = true
)
class Awesome {
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        configManager = ConfigManager()
        MinecraftForge.EVENT_BUS.register(configManager)
        loadedClasses.clear()
    }
    private val loadedClasses = mutableSetOf<Any>()

    private fun loadModule(obj: Any) {
        if (!loadedClasses.add(obj.javaClass.name)) throw IllegalStateException("module ${obj.javaClass.name} already loaded")
        MinecraftForge.EVENT_BUS.register(obj)
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        CommandManager()

        LoadedModules.modules.forEach { loadModule(it) }
    }

    @LoadModule
    companion object {
        lateinit var configManager: ConfigManager
        const val MOD_ID = "awesome"
        const val MOD_VERSION = "1.4"

        private var screenToOpen: GuiScreen? = null

        @JvmStatic
        val version: String
            get() = Loader.instance().indexedModList[MOD_ID]!!.version

        val config: AwesomeConfig
            get() = configManager.config ?: error("config is null")

        fun openScreen(screen: GuiScreen) {
            screenToOpen = screen
        }

        @SubscribeEvent
        fun onTick(event: AwesomeTickEvent) {
            if (screenToOpen != null) {
                Minecraft.getMinecraft().displayGuiScreen(screenToOpen)

                if (screenToOpen is GuiScreenElementWrapper) {
                    configManager.save()
                }

                screenToOpen = null
            }
        }
    }
}
