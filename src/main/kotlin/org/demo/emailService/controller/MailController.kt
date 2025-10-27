package org.demo.emailService.controller

import jakarta.validation.Valid
import org.demo.emailService.dto.mail.MailRequestDTO
import org.demo.emailService.security.AuthPrincipal
import org.demo.emailService.service.email.EmailService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/mail")
class MailController(
    private val emailService: EmailService,
) {
    @PostMapping
    fun sendMail(
        @Valid @RequestBody request: MailRequestDTO,
    ): ResponseEntity<Any> {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication?.principal as? AuthPrincipal
        val userId = principal?.id ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()

        val sent = emailService.sendMail(request, userId)
        return if (sent) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(mapOf("message" to "All providers failed to send the email"))
        }
    }
}
