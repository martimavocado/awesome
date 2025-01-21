package at.martimavocado.awesome.config.guieditor.data

import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ConfigLink(
    val owner: KClass<*>,
    val field: String,
)
