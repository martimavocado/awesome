package at.martimavocado.awesome.commands

import at.martimavocado.awesome.events.CommandRegistrationEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import at.martimavocado.awesome.utils.ChatUtils
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@LoadModule
object CommandManager {
    val commandList = mutableListOf<CommandBuilder>()

    @SubscribeEvent
    fun onCommandRegistration(event: CommandRegistrationEvent) {
        event.register("awcommands") {
            description = "Prints all commands and their descriptions in chat"
            callback { commandListCommand() }
        }
    }

    private fun commandListCommand() {
        var message = ""
        message += "§7---------------------------------------------------"
        commandList.forEachIndexed { index, command ->

            var nameMessage = "\n§2${command.name}"
            command.aliases.forEach { nameMessage += "§8, §a$it" }
            message += nameMessage

            message += "\n§e" + command.description
            if (index != (commandList.size - 1)) message += "\n"
        }
        message += "\n§7---------------------------------------------------"
        ChatUtils.chat(message, false)
    }
}
