package org.demo.emailService.dto.stats

import java.util.UUID

data class StatsEntryDTO(
    val userId: UUID,
    val email: String,
    val count: Long,
)

