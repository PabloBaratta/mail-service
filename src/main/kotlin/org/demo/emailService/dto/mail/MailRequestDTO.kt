package org.demo.emailService.dto.mail

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class MailRequestDTO(
    @field:NotBlank(message = "Recipient must not be empty")
    @field:Email(message = "Recipient must be a valid email")
    val to: String?,

    @field:NotBlank(message = "Subject must not be empty")
    val subject: String?,

    @field:NotBlank(message = "Text must not be empty")
    val text: String?,
)

data class MailRequest(
    val from: String,
    val to: String,
    val subject: String,
    val text: String,
)


