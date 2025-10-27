package org.demo.emailService.service.email

import org.demo.emailService.dto.mail.MailRequest
import org.demo.emailService.dto.mail.MailRequestDTO
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val providers: Map<String, EmailProvider>,
    @Value("\${email.providers.order}") private val providersOrder: String,
    @Value("\${email.providers.retry-count}") private val retryCount: Int,
    @Value("\${email.from}") private val defaultFrom: String,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun sendMail(request: MailRequestDTO): Boolean {
        val mailRequest = MailRequest(
            from = defaultFrom,
            to = request.to!!,
            subject = request.subject!!,
            text = request.text!!
        )
        val orderedNames = providersOrder.split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        for (name in orderedNames) {
            val provider = providers[name]
            if (provider == null) {
                logger.warn("Email provider bean not found: {}", name)
                continue
            }

            for (attempt in 0..retryCount) {
                try {
                    logger.debug("Trying provider '{}' attempt {}/{}", name, attempt + 1, retryCount + 1)
                    val ok = provider.sendMail(mailRequest)
                    if (ok) {
                        logger.info("Email sent successfully using provider '{}'", name)
                        return true
                    } else {
                        logger.warn("Provider '{}' returned false on attempt {}/{}", name, attempt + 1, retryCount + 1)
                    }
                } catch (ex: Exception) {
                    logger.error("Exception sending with provider '{}' on attempt {}/{}: {}", name, attempt + 1, retryCount + 1, ex.message)
                }
            }

            logger.info("Provider '{}' exhausted ({} attempts) - moving to next", name, retryCount + 1)
        }

        logger.error("All providers failed to send the email")
        return false
    }
}