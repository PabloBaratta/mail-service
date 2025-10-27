package org.demo.emailService.service.email

import org.demo.emailService.dto.mail.MailRequest

interface EmailProvider {
    fun sendMail(request: MailRequest): Boolean
}
