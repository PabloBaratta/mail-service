package org.demo.emailService.exception.auth

import jakarta.servlet.http.HttpServletRequest
import org.demo.emailService.dto.ApiErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthExceptionHandler {
    @ExceptionHandler(AuthException::class)
    fun handleAuthException(
        ex: AuthException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val response =
            ApiErrorResponse(
                title = "Authentication error",
                status = HttpStatus.UNAUTHORIZED.value(),
                detail = ex.message ?: "Authentication failed.",
                instance = request.requestURI,
                errors = null,
            )
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(
        ex: UserAlreadyExistsException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val response =
            ApiErrorResponse(
                title = "User already exists",
                status = HttpStatus.CONFLICT.value(),
                detail = ex.message ?: "A user with this email already exists.",
                instance = request.requestURI,
                errors = null,
            )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response)
    }
}
