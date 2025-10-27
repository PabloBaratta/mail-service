package org.demo.emailService.exception.stats

import jakarta.servlet.http.HttpServletRequest
import org.demo.emailService.dto.ApiErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class StatsExceptionHandler {
    @ExceptionHandler(MailQuotaExceededException::class)
    fun handleMailQuotaExceeded(ex: MailQuotaExceededException, request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val error =
            ApiErrorResponse(
                title = "Mail quota exceeded",
                status = HttpStatus.TOO_MANY_REQUESTS.value(),
                detail = ex.message ?: "User has reached the daily mail limit. Please wait until your quota is reset the next day.",
                instance = request.requestURI,
            )
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(error)
    }
}
