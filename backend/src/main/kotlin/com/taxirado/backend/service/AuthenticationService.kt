package com.taxirado.backend.service

import com.taxirado.backend.dto.*
import com.taxirado.backend.model.User
import com.taxirado.backend.repository.UserRepository
import com.taxirado.backend.security.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import kotlin.random.Random

/**
 * Authentication Service handling user registration, login, and verification
 */
@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun register(request: UserRegistrationRequest): UserResponse {
        // Check if username already exists
        if (userRepository.findByUsername(request.username) != null) {
            throw IllegalArgumentException("Username already exists")
        }

        // Check if email already exists
        if (userRepository.findByEmail(request.email) != null) {
            throw IllegalArgumentException("Email already exists")
        }

        // Check if phone number already exists
        if (userRepository.findByPhoneNumber(request.phoneNumber) != null) {
            throw IllegalArgumentException("Phone number already exists")
        }

        // Generate verification codes
        val emailCode = generateVerificationCode()
        val phoneCode = generateVerificationCode()

        // Create user with hashed password
        val user = User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            email = request.email,
            phoneNumber = request.phoneNumber,
            role = request.role,
            emailVerified = false,
            phoneVerified = false,
            emailVerificationCode = emailCode,
            phoneVerificationCode = phoneCode,
            enabled = false
        )

        val savedUser = userRepository.save(user)

        // TODO: Send email verification code
        println("Email verification code for ${request.email}: $emailCode")
        
        // TODO: Send SMS verification code
        println("SMS verification code for ${request.phoneNumber}: $phoneCode")

        return toUserResponse(savedUser)
    }

    fun login(request: LoginRequest): LoginResponse {
        val user = userRepository.findByUsername(request.username)
            ?: throw IllegalArgumentException("Invalid username or password")

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw IllegalArgumentException("Invalid username or password")
        }

        if (!user.emailVerified || !user.phoneVerified) {
            throw IllegalArgumentException("Please verify your email and phone number first")
        }

        val token = jwtTokenProvider.generateToken(user.username, user.id!!)
        return LoginResponse(token, toUserResponse(user))
    }

    fun verifyEmail(request: VerifyEmailRequest): VerificationResponse {
        val user = userRepository.findById(request.userId).orElseThrow {
            IllegalArgumentException("User not found")
        }

        if (user.emailVerified) {
            return VerificationResponse(true, "Email already verified")
        }

        if (user.emailVerificationCode != request.verificationCode) {
            return VerificationResponse(false, "Invalid verification code")
        }

        val updatedUser = user.copy(
            emailVerified = true,
            emailVerificationCode = null,
            enabled = user.phoneVerified // Enable if both are verified
        )
        userRepository.save(updatedUser)

        return VerificationResponse(true, "Email verified successfully")
    }

    fun verifyPhone(request: VerifyPhoneRequest): VerificationResponse {
        val user = userRepository.findById(request.userId).orElseThrow {
            IllegalArgumentException("User not found")
        }

        if (user.phoneVerified) {
            return VerificationResponse(true, "Phone already verified")
        }

        if (user.phoneVerificationCode != request.verificationCode) {
            return VerificationResponse(false, "Invalid verification code")
        }

        val updatedUser = user.copy(
            phoneVerified = true,
            phoneVerificationCode = null,
            enabled = user.emailVerified // Enable if both are verified
        )
        userRepository.save(updatedUser)

        return VerificationResponse(true, "Phone verified successfully")
    }

    private fun generateVerificationCode(): String {
        return Random.nextInt(100000, 999999).toString()
    }

    private fun toUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id!!,
            username = user.username,
            email = user.email,
            phoneNumber = user.phoneNumber,
            role = user.role,
            emailVerified = user.emailVerified,
            phoneVerified = user.phoneVerified
        )
    }
}
