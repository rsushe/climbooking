package ru.climbooking.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.climbooking.exception.EntityNotFoundException

@ControllerAdvice
class ExceptionAdvice {

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFoundException(entityNotFoundException: EntityNotFoundException): ResponseEntity<String> =
        ResponseEntity(entityNotFoundException.message, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(illegalArgumentException: IllegalArgumentException): ResponseEntity<String> =
        ResponseEntity(illegalArgumentException.message, HttpStatus.BAD_REQUEST)
}
