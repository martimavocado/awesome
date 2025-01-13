package at.martimavocado.awesome.data

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.CommandRegistrationEvent
import at.martimavocado.awesome.events.DebugDataCollectionEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ChatUtils
import at.martimavocado.awesome.utils.EventUtils.post
import at.martimavocado.awesome.utils.StringUtils.equalsIgnoreColor
import at.martimavocado.awesome.utils.system.ClipboardUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object DebugCommand {
    fun command(args: Array<String>) {
        val list = mutableListOf<String>()

        list.add("```")
        list.add("= Debug Information for Awesome ${Awesome.MOD_VERSION} =")
        list.add("")

        val search = args.joinToString(" ")
        list.add(
            if (search.isNotEmpty()) {
                if (search.equalsIgnoreColor("all")) {
                    "search for everything:"
                } else {
                    "search '$search':"
                }
            } else {
                "no search specified, only showing interesting stuff:"
            },
        )

        val event = DebugDataCollectionEvent(list, search)

        event.post()
        if (event.empty) {
            list.add("")
            list.add("Nothing interesting to show right now!")
            list.add("Looking for something specific? /awdebug <search>")
            list.add("Wanna see everything? /awdebug all")
        }

        list.add("```")
        ClipboardUtils.copyToClipboard(list.joinToString("\n"))
        ChatUtils.chat("Copied Awesome debug data to the clipboard.")
    }

    @SubscribeEvent
    fun onCommandRegistration(event: CommandRegistrationEvent) {
        event.register("awdebug") {
            description = "Copies important(?) debug data to the scoreboard."
            callback { command(it) }
        }
    }
}
