package at.martimavocado.awesome.utils.system

import at.martimavocado.awesome.utils.ChatUtils
import at.martimavocado.awesome.utils.system.ClipboardUtils.copyToClipboard
import net.minecraft.launchwrapper.Launch
import java.awt.Desktop
import java.io.IOException
import java.net.URI

object PlatformUtils {
    val isDevEnvironment: Boolean by lazy {
        Launch.blackboard?.get("fml.deobfuscatedEnvironment") as? Boolean != false
    }

    enum class OperatingSystem {
        LINUX,
        SOLARIS,
        WINDOWS,
        MACOS,
        UNKNOWN,
    }

    fun getOperatingSystemRaw(): String = System.getProperty("os.name")

    fun getOperatingSystem(): OperatingSystem {
        val osName = getOperatingSystemRaw().lowercase()
        return when {
            osName.contains("win") -> OperatingSystem.WINDOWS
            osName.contains("mac") -> OperatingSystem.MACOS
            osName.contains("solaris") || osName.contains("sunos") -> OperatingSystem.SOLARIS
            osName.contains("linux") || osName.contains("unix") -> OperatingSystem.LINUX

            else -> OperatingSystem.UNKNOWN
        }
    }

    @JvmStatic
    fun openBrowser(url: String) {
        val desktopSupported = Desktop.isDesktopSupported()
        val supportedActionBrowse = Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)
        if (desktopSupported && supportedActionBrowse) {
            try {
                Desktop.getDesktop().browse(URI(url))
            } catch (_: IOException) {
                ChatUtils.warning("Error while opening website. $url")
            }
        } else {
            copyToClipboard(url)
            ChatUtils.warning("Cannot open website! Copied url to clipboard instead. $url")
        }
    }
}
