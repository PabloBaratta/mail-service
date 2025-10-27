package org.demo.emailService.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.demo.emailService.config.JwtConfig
import org.demo.emailService.model.user.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.spec.SecretKeySpec
import org.springframework.security.core.userdetails.User as SpringUser

@Service
class TokenService(
    private val jwtConfig: JwtConfig,
) {
    fun generateToken(user: User): String {
        val key = SecretKeySpec(jwtConfig.secret.toByteArray(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.jcaName)
        return Jwts
            .builder()
            .setSubject(user.email)
            .claim("role", user.role.name)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtConfig.expirationMs))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun validateToken(token: String): SpringUser? {
        return try {
            val key = Keys.hmacShaKeyFor(jwtConfig.secret.toByteArray(StandardCharsets.UTF_8))
            val claims: Claims =
                Jwts
                    .parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .body
            val email = claims.subject
            val role = claims["role"] as? String ?: "USER"
            val authorities = listOf(SimpleGrantedAuthority("ROLE_$role"))
            return SpringUser(email, "", authorities)
        } catch (ex: Exception) {
            println(ex)
            null
        }
    }
}
