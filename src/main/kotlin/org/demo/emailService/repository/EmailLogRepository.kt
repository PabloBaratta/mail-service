package org.demo.emailService.repository

import org.demo.emailService.model.stats.EmailLog
import org.demo.emailService.model.user.User
import org.demo.emailService.dto.stats.StatsUserCount
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.UUID

interface EmailLogRepository : JpaRepository<EmailLog, UUID> {
    @Query("SELECT COUNT(e) FROM EmailLog e WHERE e.user = :user AND e.timestamp BETWEEN :start AND :end")
    fun countByUserAndPeriod(
        @Param("user") user: User,
        @Param("start") start: Instant,
        @Param("end") end: Instant,
    ): Long

    @Query(
        "SELECT e.user.id as userId, e.user.email as email, COUNT(e) as count " +
            "FROM EmailLog e " +
            "WHERE e.timestamp BETWEEN :start AND :end " +
            "GROUP BY e.user.id, e.user.email"
    )
    fun countEmailsGroupedByUserBetween(
        @Param("start") start: Instant,
        @Param("end") end: Instant,
    ): List<StatsUserCount>
}
