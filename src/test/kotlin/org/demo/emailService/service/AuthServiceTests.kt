package org.demo.emailService.service

import org.demo.emailService.exception.auth.AuthException
import org.demo.emailService.exception.auth.UserAlreadyExistsException
import org.demo.emailService.model.user.RoleType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@SpringBootTest
@TestPropertySource(locations = ["classpath:application-test.properties"])
class AuthServiceTests
    @Autowired
    constructor(
        private val authService: AuthService,
    ) {
        @Test
        fun `register should create a new user`() {
            val email = "test1@example.com"
            val password = "password123"
            val user = authService.register(email, password, RoleType.USER)
            assertNotNull(user.id)
            assertEquals(email, user.email)
        }

        @Test
        fun `register should throw if email already exists`() {
            val email = "test2@example.com"
            val password = "password123"
            authService.register(email, password, RoleType.USER)
            assertFailsWith<UserAlreadyExistsException> {
                authService.register(email, password, RoleType.USER)
            }
        }

        @Test
        fun `authenticate should return user with valid credentials`() {
            val email = "auth-success@example.com"
            val password = "validPass123"
            authService.register(email, password, RoleType.USER)

            val user = authService.authenticate(email, password)
            assertNotNull(user.id)
            assertEquals(email, user.email)
        }

        @Test
        fun `authenticate should throw when password is invalid`() {
            val email = "auth-wrongpass@example.com"
            val password = "rightPass123"
            authService.register(email, password, RoleType.USER)

            assertFailsWith<AuthException> {
                authService.authenticate(email, "wrongPassword")
            }
        }

        @Test
        fun `authenticate should throw when user does not exist`() {
            assertFailsWith<AuthException> {
                authService.authenticate("nonexistent@example.com", "any")
            }
        }
    }
