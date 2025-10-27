package org.demo.emailService.service.email

import org.demo.emailService.dto.mail.MailRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class GmailProviderTest {
    @Autowired
    lateinit var gmailProvider: GmailProvider

    @Test
    fun `should send mail using GmailProvider`() {
        val mailRequest =
            MailRequest(
                from = "from@example.com",
                to = "pbaratta2004@gmail.com",
                subject = "Test Subject",
                text = "Test Body",
            )
        gmailProvider.sendMail(mailRequest)
    }
}
