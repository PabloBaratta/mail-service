package org.demo.emailService.model.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue
    val id: UUID? = null,
    @Column(nullable = false)
    var passwordHash: String,
    @Column(unique = true, nullable = false)
    var email: String,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var role: RoleType = RoleType.USER,
)
