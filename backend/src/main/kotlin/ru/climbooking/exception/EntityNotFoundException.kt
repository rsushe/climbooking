package ru.climbooking.exception

import java.lang.RuntimeException

class EntityNotFoundException(message: String, cause: Throwable): RuntimeException(message, cause)
