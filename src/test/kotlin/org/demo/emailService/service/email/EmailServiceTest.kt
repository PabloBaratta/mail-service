package org.demo.emailService.service.email

import org.demo.emailService.dto.mail.MailRequestDTO
import org.demo.emailService.exception.stats.MailQuotaExceededException
import org.demo.emailService.service.AuthService
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test

@SpringBootTest
@TestPropertySource(
    properties = [
        "email.max-per-day=1",
    ]
)
class EmailServiceTest(
    @Autowired
    private val authService: AuthService,
    @Autowired
    private val emailService: EmailService,
) {

    @Test
    fun cannotExceedQuota() {
        val user = authService.register(
            "pb@g.co",
            "12345678"
        )

        emailService.sendMail(
            MailRequestDTO(
                "g@g.co",
                "Test",
                "Test"
            ),
            user.id!!
        )

        assertThrows<MailQuotaExceededException> {
            emailService.sendMail(
                MailRequestDTO(
                    "g@g.co",
                    "Test",
                    "Test"
                ),
                user.id!!
            )
        }
    }
}