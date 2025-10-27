package org.demo.emailService.exception.auth

class UserAlreadyExistsException(
    message: String,
) : RuntimeException(message)
