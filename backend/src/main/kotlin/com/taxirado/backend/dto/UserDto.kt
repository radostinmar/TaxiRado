package com.taxirado.backend.dto

import com.taxirado.backend.model.UserRole

data class UserRegistrationRequest(
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val role: UserRole
)

data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val role: UserRole,
    val emailVerified: Boolean,
    val phoneVerified: Boolean
)

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: UserResponse
)

data class VerifyEmailRequest(
    val userId: Long,
    val verificationCode: String
)

data class VerifyPhoneRequest(
    val userId: Long,
    val verificationCode: String
)

data class VerificationResponse(
    val success: Boolean,
    val message: String
)
