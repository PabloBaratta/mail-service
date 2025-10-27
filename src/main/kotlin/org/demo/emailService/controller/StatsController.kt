package org.demo.emailService.controller

import org.demo.emailService.dto.stats.StatsEntryDTO
import org.demo.emailService.security.AuthPrincipal
import org.demo.emailService.service.stats.StatsService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/stats")
class StatsController(
    private val statsService: StatsService,
) {
    @GetMapping
    fun getStats(): ResponseEntity<List<StatsEntryDTO>> {
        val authentication = SecurityContextHolder.getContext().authentication
        val principal = authentication?.principal as? AuthPrincipal
        val isAdmin = principal?.authorities?.any { it.authority == "ROLE_ADMIN" } ?: false
        if (!isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        }

        val stats = statsService.getDailyStats()
        return ResponseEntity.ok(stats)
    }
}

