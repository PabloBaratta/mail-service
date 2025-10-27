package org.demo.emailService.model.stats

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.demo.emailService.model.user.User
import java.time.Instant
import java.util.UUID

@Entity
class EmailLog(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,
    @Column(nullable = false, length = 100)
    var provider: String,
    @Column(length = 200)
    var subject: String? = null,
    @Column(name = "to_address", nullable = false)
    var toAddress: String,
    @Column(nullable = false)
    var timestamp: Instant = Instant.now(),
)
