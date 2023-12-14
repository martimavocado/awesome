package com.github.martimavocado.awesome.errors

class CommandError(message: String, cause: Throwable) : Error(message, cause)