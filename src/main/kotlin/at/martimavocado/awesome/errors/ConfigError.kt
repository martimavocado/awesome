package at.martimavocado.awesome.errors

class ConfigError(message: String, cause: Throwable) : Error(message, cause)