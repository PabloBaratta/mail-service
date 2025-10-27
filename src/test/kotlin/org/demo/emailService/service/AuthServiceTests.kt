package org.demo.emailService.service

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
    }
