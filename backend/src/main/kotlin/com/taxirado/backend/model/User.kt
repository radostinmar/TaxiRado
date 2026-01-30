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

    @Column(nullable = false)
    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole
)

enum class UserRole {
    DRIVER,
    PASSENGER,
    BOTH
}
