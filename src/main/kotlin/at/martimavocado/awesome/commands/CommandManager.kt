package at.martimavocado.awesome.commands

import at.martimavocado.awesome.commands.SimpleCommand.ProcessCommandRunnable
import at.martimavocado.awesome.config.ConfigGuiManager
import at.martimavocado.awesome.features.FakeBan
import at.martimavocado.awesome.features.HelpCommands
import at.martimavocado.awesome.features.misc.update.UpdateManager
import at.martimavocado.awesome.utils.ChatUtils
import at.martimavocado.awesome.utils.OtherUtils
import net.minecraft.command.ICommandSender
import net.minecraftforge.client.ClientCommandHandler

class CommandManager {
    init {
        registerCommand("awesome") {
            ConfigGuiManager.onCommand(it)
        }
        registerCommand("aw") {
            ConfigGuiManager.onCommand(it)
        }
        registerCommand("emojilist") {
            HelpCommands.printMessage("emoji")
        }
        registerCommand("awcommands") {
            HelpCommands.printMessage("help")
        }
        registerCommand("showtitle") {
            OtherUtils.tryShowTitle(it)
        }
        registerCommand("testmessage") {
            ChatUtils.testMessageCommand(it)
        }
        registerCommand("fakeban") {
            FakeBan.showBanScreen()
        }
        registerCommand("awupdate") {
            UpdateManager.updateCommand()
        }
    }

    private fun registerCommand(
        name: String,
        function: (Array<String>) -> Unit,
    ) {
        ClientCommandHandler.instance.registerCommand(SimpleCommand(name, createCommand(function)))
    }

    private fun createCommand(function: (Array<String>) -> Unit) =
        object : ProcessCommandRunnable() {
            override fun processCommand(
                sender: ICommandSender?,
                args: Array<String>?,
            ) {
                if (args != null) function(args.asList().toTypedArray())
            }
        }
}
