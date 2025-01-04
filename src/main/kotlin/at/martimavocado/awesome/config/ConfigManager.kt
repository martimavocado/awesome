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

    private fun tryReadConfig() {
        try {
            val inputStreamReader = InputStreamReader(FileInputStream(configFile), StandardCharsets.UTF_8)
            val bufferedReader = BufferedReader(inputStreamReader)

            val builder = StringBuilder()
            for (line in bufferedReader.lines()) {
                builder.append(line)
                builder.append("\n")
            }
            config = gson.fromJson(builder.toString(), AwesomeConfig::class.java)
        } catch (e: Exception) {
            throw ConfigError("Could not load config", e)
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
            if (oldConfig.isFile) Files.move(
                oldConfig.toPath(),
                configDirectory.resolve("config-old.json").toPath(),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            )
            Files.move(
                tempFile.toPath(),
                configFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE
            )
        } catch (e: IOException) {
            throw ConfigError("Could not save config", e)
        }
    }
}