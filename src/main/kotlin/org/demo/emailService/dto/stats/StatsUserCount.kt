package org.demo.emailService.dto.stats

import java.util.UUID

interface StatsUserCount {
    fun getUserId(): UUID?
    fun getEmail(): String
    fun getCount(): Long
}

