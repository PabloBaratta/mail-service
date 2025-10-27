package org.demo.emailService.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "jwt")
data class JwtConfig(
    var secret: String = "",
    var expirationMs: Long = 0,
)
