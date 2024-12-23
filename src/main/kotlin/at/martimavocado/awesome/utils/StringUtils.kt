package at.martimavocado.awesome.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtils {
    inline fun <T> Pattern.matchMatcher(text: String, consumer: Matcher.() -> T) =
        matcher(text).let { if (it.matches()) consumer(it) else null }

    fun Pattern.matches(text: String) =
        this.toRegex().matches(text)
}