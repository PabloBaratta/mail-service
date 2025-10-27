package org.demo.emailService.exception.auth

import jakarta.servlet.http.HttpServletRequest
import org.demo.emailService.dto.ApiErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val response =
            ApiErrorResponse(
                title = "Malformed JSON request",
                status = HttpStatus.BAD_REQUEST.value(),
                detail = ex.localizedMessage ?: "Request body is not readable or is malformed JSON.",
                instance = request.requestURI,
                errors = null,
            )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val errors = ex.bindingResult.fieldErrors.associate { it.field to (it.defaultMessage ?: "Invalid value") }
        val response =
            ApiErrorResponse(
                title = "Validation failed",
                status = HttpStatus.BAD_REQUEST.value(),
                detail = "Request validation failed. See errors for details.",
                instance = request.requestURI,
                errors = errors,
            )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }
}
