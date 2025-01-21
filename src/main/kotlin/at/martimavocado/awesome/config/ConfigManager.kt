package at.martimavocado.awesome.config

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.config.categories.AwesomeConfig
import at.martimavocado.awesome.config.guieditor.GuiPositionEditorUtils.makeAccessible
import at.martimavocado.awesome.config.guieditor.data.GuiPosition
import at.martimavocado.awesome.config.guieditor.data.IdentityCharacteristics
import at.martimavocado.awesome.errors.ConfigError
import at.martimavocado.awesome.features.misc.update.UpdateManager
import at.martimavocado.awesome.utils.system.AwesomeLogger
import at.martimavocado.awesome.utils.system.PlatformUtils
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.notenoughupdates.moulconfig.annotations.ConfigLink
import io.github.notenoughupdates.moulconfig.observer.PropertyTypeAdapterFactory
import io.github.notenoughupdates.moulconfig.processor.BuiltinMoulConfigGuis
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor
import net.minecraftforge.fml.common.FMLCommonHandler
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.UUID

class ConfigManager {
    private val logger = AwesomeLogger("config-manager")

    companion object {
        val gson =
            GsonBuilder()
                .setPrettyPrinting()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeSpecialFloatingPointValues()
                .registerTypeAdapterFactory(PropertyTypeAdapterFactory())
                .registerTypeAdapter(
                    UUID::class.java,
                    object : TypeAdapter<UUID>() {
                        override fun write(
                            out: JsonWriter,
                            value: UUID,
                        ) {
                            out.value(value.toString())
                        }

                        override fun read(reader: JsonReader): UUID = UUID.fromString(reader.nextString())
                    }.nullSafe(),
                ).enableComplexMapKeySerialization()
                .create()

        var wasCorrupted = false
            private set
        var loadedOld = false
            private set
    }

    private var configDirectory = File("config/awesome")
    private var configFile: File
    var config: AwesomeConfig? = null
    private var lastSaveTime = 0L

    var processor: MoulConfigProcessor<AwesomeConfig>

    init {
        configDirectory.mkdirs()
        configFile = File(configDirectory, "config.json")

        if (configFile.isFile) tryReadConfig()

        if (config == null) {
            println("Creating a clean config.")
            config = AwesomeConfig()
        }

        val config = config!!
        processor = MoulConfigProcessor(config)

        BuiltinMoulConfigGuis.addProcessors(processor)
        UpdateManager.injectConfigProcessor(processor)

        val driver = ConfigProcessorDriver(processor)
        driver.warnForPrivateFields = false
        driver.checkExpose = false
        driver.processConfig(config)

        try {
            handlePositionLinks(config, mutableSetOf())
        } catch (e: Exception) {
            throw e
        }

        Runtime.getRuntime().addShutdownHook(
            Thread {
                save()
            },
        )
    }

    private fun handlePositionLinks(
        obj: Any?,
        set: MutableSet<IdentityCharacteristics<Any>>,
    ) {
        if (obj == null) return
        if (!obj.javaClass.name.startsWith("at.martimavocado.awesome")) return
        val ic = IdentityCharacteristics(obj)
        if (ic in set) return
        set.add(ic)

        var missingConfigLink = false

        for (field in obj.javaClass.declaredFields.map { it.makeAccessible() }) {
            if (field.type != GuiPosition::class.java) {
                handlePositionLinks(field.get(obj), set)
                continue
            }

            val configLink = field.getAnnotation(ConfigLink::class.java)
            if (configLink == null) {
                if (PlatformUtils.isDevEnvironment) {
                    var name = "${field.declaringClass.name}.${field.name}"
                    name = name.replace("at.martimavocado.awesome.config.", "")
                    println("missing config link pls fix!! $name")
                    missingConfigLink = true
                }
                continue
            }

            val position = field.get(obj) as GuiPosition
            position.setLink(configLink)
        }

        if (missingConfigLink) {
            println("")
            println(
                "This crash is here to remind you to fix the missing " +
                    "@ConfigLink annotation over your new config position config element.",
            )

            System.err.println("Awesome ${Awesome.MOD_VERSION} forced the game to shutdown. Missing Config Link.")
            FMLCommonHandler.instance().handleExit(-1)
        }
    }

    private fun tryReadConfig(file: File = configFile) {
        try {
            val inputStreamReader = InputStreamReader(FileInputStream(file), StandardCharsets.UTF_8)
            val bufferedReader = BufferedReader(inputStreamReader)

            val builder = StringBuilder()
            for (line in bufferedReader.lines()) {
                builder.append(line)
                builder.append("\n")
            }
            config = gson.fromJson(builder.toString(), AwesomeConfig::class.java)

            if (file.name.contains("old")) {
                file.move(configDirectory.resolve("config.json"))
                loadedOld = true
            }
        } catch (e: Exception) {
            println("Could not load config")
            println(e)
            markCorruptedConfig()

            val oldConfig = configDirectory.resolve("config-old.json")
            if (!file.name.contains("old") && oldConfig.isFile) {
                tryReadConfig(oldConfig)
            }
        }
    }

    fun save() {
        if (System.currentTimeMillis() <= lastSaveTime + 30_000) return

        lastSaveTime = System.currentTimeMillis()
        val config = config ?: error("Can not save null config.")

        logger.log("saving config")

        try {
            configDirectory.mkdirs()
            val tempFile = configDirectory.resolve("config.json.write")
            tempFile.createNewFile()
            BufferedWriter(OutputStreamWriter(FileOutputStream(tempFile), StandardCharsets.UTF_8)).use { writer ->
                writer.write(gson.toJson(config))
            }

            val oldConfig = configDirectory.resolve("config.json")
            if (oldConfig.isFile) {
                oldConfig.move(configDirectory.resolve("config-old.json"))
            }

            tempFile.move(configFile)
        } catch (e: IOException) {
            throw ConfigError("Could not save config", e)
        }
    }

    private fun markCorruptedConfig() {
        if (configFile.isFile) {
            val corruptedFolder = File("config/awesome/corrupted")
            val corruptedConfig = corruptedFolder.resolve("config-${System.currentTimeMillis()}.json")

            corruptedFolder.mkdirs()
            configFile.move(corruptedConfig)
            wasCorrupted = true
        }
    }

    private fun File.move(path: Path) {
        Files.move(
            this.toPath(),
            path,
            StandardCopyOption.REPLACE_EXISTING,
            StandardCopyOption.ATOMIC_MOVE,
        )
    }

    private fun File.move(file: File) {
        this.move(file.toPath())
    }
}
