package org.demo.emailService.service.email

import org.demo.emailService.dto.mail.MailRequest
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class GmailProvider(
    private val mailSender: JavaMailSender,
) : EmailProvider {
    override fun sendMail(request: MailRequest): Boolean =
        try {
            val message =
                SimpleMailMessage().apply {
                    from = request.from
                    setTo(request.to)
                    subject = request.subject
                    text = request.text
                }
            mailSender.send(message)
            true
        } catch (ex: Exception) {
            false
        }
}
