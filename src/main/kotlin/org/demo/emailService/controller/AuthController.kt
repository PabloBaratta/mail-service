package org.demo.emailService.controller

import jakarta.validation.Valid
import org.demo.emailService.dto.auth.AuthResponse
import org.demo.emailService.dto.auth.LoginRequest
import org.demo.emailService.dto.auth.RegisterRequest
import org.demo.emailService.model.user.RoleType
import org.demo.emailService.service.AuthService
import org.demo.emailService.service.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController
    @Autowired
    constructor(
        private val authService: AuthService,
        private val tokenService: TokenService,
    ) {
        @PostMapping("/register")
        fun register(
            @Valid @RequestBody request: RegisterRequest,
        ): ResponseEntity<Unit> {
            val user = authService.register(request.email!!, request.password!!, RoleType.USER)
            return ResponseEntity.ok().build()
        }

        @PostMapping("/login")
        fun login(
            @Valid @RequestBody request: LoginRequest,
        ): ResponseEntity<AuthResponse> {
            val user = authService.authenticate(request.email!!, request.password!!)
            val token = tokenService.generateToken(user)
            return ResponseEntity.ok(
                AuthResponse(
                    token = token,
                ),
            )
        }
    }
