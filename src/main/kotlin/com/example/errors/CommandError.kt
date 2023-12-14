package com.example.errors

class CommandError(message: String, cause: Throwable) : Error(message, cause)