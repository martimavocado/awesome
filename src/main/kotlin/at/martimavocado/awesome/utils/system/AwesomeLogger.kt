package at.martimavocado.awesome.utils.system

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.utils.SimpleTimeMark
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import java.util.logging.FileHandler
import java.util.logging.Formatter
import java.util.logging.LogRecord
import java.util.logging.Logger
import kotlin.time.Duration.Companion.days

class AwesomeLogger(
    private val fileName: String,
) {
    private val format = SimpleDateFormat("HH:mm:ss")
    private val filePath = "$prefixPath$fileName.log"

    companion object {
        private val logDirectory = File("config/awesome/logs")
        private var prefixPath: String

        var cleanupDone = false

        init {
            val format = SimpleDateFormat("yyyy_MM_dd/HH:mm:ss").formatCurrentTime()
            prefixPath = "config/awesome/logs/$format/"
        }

        private fun SimpleDateFormat.formatCurrentTime() = this.format(System.currentTimeMillis())
    }

    private lateinit var logger: Logger

    private fun getLogger(): Logger {
        if (::logger.isInitialized) return logger

        var initLogger = initLogger()
        this.logger = initLogger
        return initLogger
    }

    private fun initLogger(): Logger {
        val logger = Logger.getLogger("Awesome-Logger-${System.nanoTime()}")
        try {
            createParent(File(filePath))
            val handler = FileHandler(filePath)

            handler.encoding = "utf-8"
            logger.addHandler(handler)
            logger.useParentHandlers = false

            handler.formatter =
                object : Formatter() {
                    override fun format(logRecord: LogRecord): String {
                        val message = logRecord.message
                        return format.formatCurrentTime() + " $message\n"
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (!cleanupDone) {
            cleanupDone = true
            val directoryFiles =
                logDirectory.listFiles() ?: run {
                    println("no files in log dir")
                    return logger
                }

            Awesome.launchCoroutine {
                val timeToDelete = Awesome.config.debug.logExpiryTime.days

                directoryFiles.forEach { file ->
                    val path = file.toPath()
                    try {
                        val attributes = Files.readAttributes(path, BasicFileAttributes::class.java)
                        val creationTime = attributes.creationTime().toMillis()
                        val timeSinceCreation = SimpleTimeMark(creationTime).passedSince()
                        if (timeSinceCreation > timeToDelete) {
                            if (!file.deleteRecursively()) {
                                println("failed to delete dir: ${file.name}")
                            }
                        }
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        println("Error: Unable to get creation date.")
                    }
                }
            }
        }

        return logger
    }

    fun log(text: String?) {
        getLogger().info(text)
    }

    private fun createParent(file: File) {
        val parent = file.parentFile
        if (parent != null && !parent.isDirectory) {
            parent.mkdirs()
        }
    }
}
