package com.example.template.application.presentation

import mu.KLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
@Suppress("unused")
class ControllerExceptionHandler {
    companion object : KLogging()

    @ExceptionHandler(IllegalArgumentException::class)
    fun handle(e: IllegalArgumentException): ResponseEntity<RestErrorMessage> =
        ResponseEntity.badRequest().toResponseEntity(e)

    @ExceptionHandler(NoSuchElementException::class)
    fun handle(e: NoSuchElementException): ResponseEntity<RestErrorMessage> = ResponseEntity.notFound().build()

    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ResponseEntity<RestErrorMessage> =
        ResponseEntity.internalServerError().toResponseEntity(e)

    private fun ResponseEntity.BodyBuilder.toResponseEntity(e: Throwable): ResponseEntity<RestErrorMessage> {
        logger.error("handle IllegalArgumentException", e)

        return this.body(RestErrorMessage(e.message.orEmpty()))
    }
}

data class RestErrorMessage(
    val message: String,
)
