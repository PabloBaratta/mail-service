package org.demo.emailService.config.admin

import org.demo.emailService.model.user.RoleType
import org.demo.emailService.service.AuthService
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdminSeeder {
    @Bean
    fun addAdmins(authService: AuthService) = CommandLineRunner {
        authService.register("p@g.co", "12345678", RoleType.ADMIN)
    }
}