package com.example.errors

class ConfigError(message: String, cause: Throwable) : Error(message, cause)