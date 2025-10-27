package org.demo.emailService.service.stats

import org.demo.emailService.model.stats.EmailLog
import org.demo.emailService.repository.EmailLogRepository
import org.demo.emailService.repository.UserRepository
import org.demo.emailService.dto.stats.StatsEntryDTO
import org.demo.emailService.dto.stats.StatsUserCount
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.UUID

@Service
class StatsService(
    private val emailLogRepository: EmailLogRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun logEmailSent(
        userId: UUID,
        provider: String,
        subject: String?,
        toAddress: String,
    ) {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        val log =
            EmailLog(
                user = user,
                provider = provider,
                subject = subject,
                toAddress = toAddress,
                timestamp = Instant.now(),
            )
        emailLogRepository.save(log)
    }

    fun countEmailsSentByUserInPeriod(
        userId: UUID,
        start: Instant,
        end: Instant,
    ): Long {
        val user = userRepository.findById(userId).orElseThrow { IllegalArgumentException("User not found") }
        return emailLogRepository.countByUserAndPeriod(user, start, end)
    }

    fun getDailyStats(): List<StatsEntryDTO> {
        val today = LocalDate.now(ZoneOffset.UTC)
        val startOfDay = today.atStartOfDay().toInstant(ZoneOffset.UTC)
        val endOfDay = today.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)

        val grouped: List<StatsUserCount> = emailLogRepository.countEmailsGroupedByUserBetween(startOfDay, endOfDay)
        return grouped.mapNotNull { row ->
            val uid = row.getUserId() ?: return@mapNotNull null
            StatsEntryDTO(uid, row.getEmail(), row.getCount())
        }
    }
}
