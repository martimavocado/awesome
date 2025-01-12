package at.martimavocado.awesome.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtils {
    inline fun <T> Pattern.matchMatcher(
        text: String,
        consumer: Matcher.() -> T,
    ) = matcher(text).let { if (it.matches()) consumer(it) else null }

    inline fun <T> Pattern.findMatcher(
        text: String,
        consumer: Matcher.() -> T,
    ) = matcher(text).let { if (it.find()) consumer(it) else null }

    fun Pattern.matches(text: String) = this.toRegex().matches(text)

    fun Pattern.find(text: String) = this.matcher(text).find()

    fun String.capitalize(): String =
        split(" ").joinToString(" ") { word ->
            word.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase() else it.toString()
            }
        }

    fun String.cleanupColors(): String {
        var message = this
        while (message.startsWith("§r")) {
            message = message.substring(2)
        }
        while (message.endsWith("§r")) {
            message = message.substring(0, message.length - 2)
        }
        return message
    }
}
