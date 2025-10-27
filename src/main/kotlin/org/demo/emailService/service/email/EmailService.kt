package org.demo.emailService.service.email

import org.demo.emailService.dto.mail.MailRequest
import org.demo.emailService.dto.mail.MailRequestDTO
import org.demo.emailService.exception.stats.MailQuotaExceededException
import org.demo.emailService.service.stats.StatsService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID

@Service
class EmailService(
    private val providers: Map<String, EmailProvider>,
    @Value("\${email.providers.order}") private val providersOrder: String,
    @Value("\${email.providers.retry-count}") private val retryCount: Int,
    @Value("\${email.from}") private val defaultFrom: String,
    @Value("\${email.max-per-day:1000}") private val maxPerDay: Int,
    private val statsService: StatsService,
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun sendMail(
        request: MailRequestDTO,
        userId: UUID,
    ): Boolean {
        val today = LocalDate.now(ZoneOffset.UTC)
        val startOfDay = today.atStartOfDay().toInstant(ZoneOffset.UTC)
        val endOfDay = today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        val sentToday = statsService.countEmailsSentByUserInPeriod(userId, startOfDay, endOfDay)
        if (sentToday >= maxPerDay) {
            throw MailQuotaExceededException(
                "User has reached the daily mail limit ($maxPerDay). Please wait until your quota is reset the next day.",
            )
        }
        val mailRequest =
            MailRequest(
                from = defaultFrom,
                to = request.to!!,
                subject = request.subject!!,
                text = request.text!!,
            )
        val orderedNames =
            providersOrder
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

        for (name in orderedNames) {
            val provider = providers[name]
            if (provider == null) {
                logger.warn("Email provider bean not found: {}", name)
                continue
            }

            if (sendMailGivenProvider(name, provider, mailRequest)) {
                statsService.logEmailSent(userId, name, mailRequest.subject, mailRequest.to)
                return true
            }
        }

        return false
    }

    private fun sendMailGivenProvider(
        name: String,
        provider: EmailProvider,
        mailRequest: MailRequest,
    ): Boolean {
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
                logger.error(
                    "Exception sending with provider '{}' on attempt {}/{}: {}",
                    name,
                    attempt + 1,
                    retryCount + 1,
                    ex.message,
                )
            }
        }

        logger.info("Provider '{}' exhausted ({} attempts) - moving to next", name, retryCount + 1)
        return false
    }
}
