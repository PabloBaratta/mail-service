package org.demo.emailService.exception.stats

class MailQuotaExceededException(
    message: String,
) : RuntimeException(message)
