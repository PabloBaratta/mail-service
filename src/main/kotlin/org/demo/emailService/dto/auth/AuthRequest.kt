package org.demo.emailService.dto.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class LoginRequest(
    @field:NotNull(message = "Email must not be null")
    val email: String?,
    @field:NotNull(message = "Password must not be null")
    val password: String?,
)

data class RegisterRequest(
    @field:NotNull(message = "Email must not be null")
    @field:Email(message = "Invalid email format")
    val email: String?,
    @field:NotNull(message = "Password must not be null")
    @field:Size(min = 8, message = "Password must be at least 8 characters")
    val password: String?,
)
