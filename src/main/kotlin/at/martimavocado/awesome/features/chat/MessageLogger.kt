package at.martimavocado.awesome.features.chat

import at.martimavocado.awesome.Awesome
import at.martimavocado.awesome.events.chat.ChatSendEvent
import at.martimavocado.awesome.loadmodule.LoadModule
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.BufferedWriter
import java.io.FileWriter

@LoadModule
object MessageLogger {
    private val config get() = Awesome.config.chatter

    @SubscribeEvent
    fun onSendChat(event: ChatSendEvent) {
        if (!config.chatLogger) return

        val message = event.message
        if (message.startsWith("?aw") || message.startsWith("/")) return

        val filePath = "config/awesome/messages.txt"
        val writer = BufferedWriter(FileWriter(filePath, true))
        writer.write("$message\n")
        writer.close()
    }
}