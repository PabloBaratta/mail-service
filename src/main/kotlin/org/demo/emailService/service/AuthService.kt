package org.demo.emailService.service

import org.demo.emailService.exception.auth.AuthException
import org.demo.emailService.exception.auth.UserAlreadyExistsException
import org.demo.emailService.model.user.RoleType
import org.demo.emailService.model.user.User
import org.demo.emailService.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService
    @Autowired
    constructor(
        private val userRepository: UserRepository,
    ) {
        private val passwordEncoder = BCryptPasswordEncoder()

        fun register(
            email: String,
            password: String,
            role: RoleType = RoleType.USER,
        ): User {
            if (userRepository.findByEmail(email) != null) {
                throw UserAlreadyExistsException("A user with this email already exists.")
            }
            val passwordHash = passwordEncoder.encode(password)
            val user = User(email = email, passwordHash = passwordHash, role = role)
            return userRepository.save(user)
        }

        fun authenticate(
            email: String,
            password: String,
        ): User {
            val user =
                userRepository.findByEmail(email)
                    ?: throw AuthException("Authentication failed: invalid credentials.")
            if (!passwordEncoder.matches(password, user.passwordHash)) {
                throw AuthException("Authentication failed: invalid credentials.")
            }
            return user
        }
    }
