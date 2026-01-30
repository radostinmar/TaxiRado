package com.taxirado.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false, unique = true)
    val phoneNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole,

    @Column(nullable = false)
    val emailVerified: Boolean = false,

    @Column(nullable = false)
    val phoneVerified: Boolean = false,

    @Column
    val emailVerificationCode: String? = null,

    @Column
    val phoneVerificationCode: String? = null,

    @Column(nullable = false)
    val enabled: Boolean = false
)

enum class UserRole {
    DRIVER,
    PASSENGER,
    BOTH
}
