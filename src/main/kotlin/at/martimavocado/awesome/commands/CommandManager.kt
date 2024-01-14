package at.martimavocado.awesome.commands

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.commands.SimpleCommand.ProcessCommandRunnable
import net.minecraft.command.ICommandSender
import net.minecraftforge.client.ClientCommandHandler

class CommandManager {



    init {
        registerCommand("awesome") {
            Awesome.configManager.openConfigGui()
        }
        registerCommand("aw") {
            Awesome.configManager.openConfigGui()
        }
        registerCommand("emojilist") {
            at.martimavocado.awesome.features.HelpCommands.printMessage("emoji")
        }
        registerCommand("awcommands") {
            at.martimavocado.awesome.features.HelpCommands.printMessage("help")
        }
        registerCommand("showtitle") {
            at.martimavocado.awesome.utils.OtherUtils.tryShowTitle(it)
        }
        registerCommand("testmessage") {
            at.martimavocado.awesome.utils.ChatUtils.sendChatCommand(it)
        }
        registerCommand("fakeban") {
            at.martimavocado.awesome.features.FakeBan.showBanScreenArguments(it[0], it[1])
        }
    }

    private fun registerCommand(name: String, function: (Array<String>) -> Unit) {
        ClientCommandHandler.instance.registerCommand(SimpleCommand(name, createCommand(function)))
    }

    private fun createCommand(function: (Array<String>) -> Unit) = object : ProcessCommandRunnable() {
        override fun processCommand(sender: ICommandSender?, args: Array<String>?) {
            if (args != null) function(args.asList().toTypedArray())
        }
    }
}