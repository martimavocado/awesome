package at.martimavocado.awesome.utils

import at.martimavocado.awesome.Awesome
class CommandsSentToServerLogger {
    companion object {
        fun logCommandsToServer(command: String) {
            if (Awesome.config.debug.commandLogs) {
                println("command send to server: '$command'")
            }
        }
    }
}