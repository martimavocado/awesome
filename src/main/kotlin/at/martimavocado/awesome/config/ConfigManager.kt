package at.martimavocado.awesome.config

import at.martimavocado.awesome.config.categories.AwesomeConfig
import at.martimavocado.awesome.errors.ConfigError
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.notenoughupdates.moulconfig.observer.PropertyTypeAdapterFactory
import io.github.notenoughupdates.moulconfig.processor.BuiltinMoulConfigGuis
import io.github.notenoughupdates.moulconfig.processor.ConfigProcessorDriver
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor
import java.io.*
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*

class ConfigManager {
    companion object {
        val gson = GsonBuilder().setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .serializeSpecialFloatingPointValues()
            .registerTypeAdapterFactory(PropertyTypeAdapterFactory())
            .registerTypeAdapter(UUID::class.java, object : TypeAdapter<UUID>() {
                override fun write(out: JsonWriter, value: UUID) {
                    out.value(value.toString())
                }

                override fun read(reader: JsonReader): UUID {
                    return UUID.fromString(reader.nextString())
                }
            }.nullSafe())
            .enableComplexMapKeySerialization()
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

        val driver = ConfigProcessorDriver(processor)
        driver.warnForPrivateFields = false
        driver.processConfig(config)

        Runtime.getRuntime().addShutdownHook(Thread {
            save()
        })
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
        if (System.currentTimeMillis() <= lastSaveTime + 60_000) {
            error("aborting saving config, last save is too new. ${System.currentTimeMillis() - lastSaveTime} ago")
            return
        }

        lastSaveTime = System.currentTimeMillis()
        val config = config ?: error("Can not save null config.")

        try {
            configDirectory.mkdirs()
            val tempFile = configDirectory.resolve("config.json.write")
            tempFile.createNewFile()
            BufferedWriter(OutputStreamWriter(FileOutputStream(tempFile), StandardCharsets.UTF_8)).use { writer ->
                writer.write(gson.toJson(config))
            }

            val oldConfig = configDirectory.resolve("config.json")
            if (oldConfig.isFile)
                oldConfig.move(configDirectory.resolve("config-old.json"))

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
            StandardCopyOption.ATOMIC_MOVE
        )
    }

    private fun File.move(file: File) {
        this.move(file.toPath())
    }
}