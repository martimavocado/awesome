package at.martimavocado.awesome.features.misc.update

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.CommandRegistrationEvent
import at.martimavocado.awesome.events.hypixel.HypixelJoinEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ChatUtils
import moe.nea.libautoupdate.CurrentVersion
import moe.nea.libautoupdate.PotentialUpdate
import moe.nea.libautoupdate.UpdateContext
import moe.nea.libautoupdate.UpdateSource
import moe.nea.libautoupdate.UpdateTarget
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.concurrent.CompletableFuture

@LoadModule
object UpdateManager {
    private val config get() = Awesome.config.about

    val updateContext =
        UpdateContext(
            UpdateSource.githubUpdateSource("martimavocado", "awesome"),
            UpdateTarget.deleteAndSaveInTheSameFolder(UpdateManager::class.java),
            CurrentVersion.of(modVersion),
            "pre",
        )

    @Suppress("ktlint:standard:backing-property-naming")
    private var _activePromise: CompletableFuture<*>? = null
    private var activePromise: CompletableFuture<*>?
        get() = _activePromise
        set(value) {
            _activePromise?.cancel(true)
            _activePromise = value
        }

    private var potentialUpdate: PotentialUpdate? = null
    var updateState: UpdateState = UpdateState.NONE
        private set

    fun updateCommand() {
        when (updateState) {
            UpdateState.NONE -> checkUpdate()
            UpdateState.AVAILABLE -> queueUpdate()
            else -> return
        }
    }

    private fun checkUpdate() {
        updateContext.checkUpdate("pre").thenAcceptAsync {
            if (updateState != UpdateState.NONE) return@thenAcceptAsync

            potentialUpdate = it
            if (!it.isUpdateAvailable) {
                ChatUtils.debug("did not find an update")
                return@thenAcceptAsync
            }
            if (modVersionNumber(it.update.versionName) <= modVersion) {
                ChatUtils.debug("already up-to-date")
                return@thenAcceptAsync
            }
            ChatUtils.chat("§aFound update ${it.update.versionName}! Use §b/awupdate §ato complete it.")
            ChatUtils.debug("${it.update.versionNumber.asNumber}")
            updateState = UpdateState.AVAILABLE
            if (config.fullAutoUpdates) queueUpdate()
        }
    }

    private fun queueUpdate() {
        updateState = UpdateState.QUEUED
        activePromise =
            CompletableFuture
                .supplyAsync {
                    val update = potentialUpdate!!
                    update.update.versionName
                    ChatUtils.chat("Downloading ${update.update.versionName}.")
                    update.prepareUpdate()
                }.thenAcceptAsync {
                    ChatUtils.chat("Finished downloading. The update will be installed after the next restart.")
                    updateState == UpdateState.DOWNLOADED
                    potentialUpdate!!.executePreparedUpdate()
                }
    }

    val modVersion get() = modVersionNumber(Awesome.version)

    private fun modVersionNumber(version: String) = version.replace(".", "").toInt()

    @SubscribeEvent
    fun onHypixelJoin(event: HypixelJoinEvent) {
        if (config.autoUpdates) checkUpdate()
    }

    init {
        updateContext.cleanup()
    }

    enum class UpdateState {
        AVAILABLE,
        QUEUED,
        DOWNLOADED,
        NONE,
    }

    @SubscribeEvent
    fun onCommandRegistration(event: CommandRegistrationEvent) {
        event.register("awupdate") {
            description = "Updates the mod."
            callback { updateCommand() }
        }
    }
}
