package com.taxirado.backend.dto

import com.taxirado.backend.model.UserRole

data class UserRegistrationRequest(
    val username: String,
    val password: String,
    val email: String,
    val role: UserRole
)

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val role: UserRole
)
