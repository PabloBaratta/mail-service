package org.demo.emailService.security

import org.springframework.security.core.GrantedAuthority
import java.util.UUID

data class AuthPrincipal(
    val id: UUID?,
    val username: String,
    val authorities: Collection<GrantedAuthority>,
)

