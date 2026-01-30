package com.taxirado.backend.controller

import com.taxirado.backend.dto.*
import com.taxirado.backend.service.AuthenticationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Authentication Controller
 * Handles user registration, login, and verification
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["*"])
@Tag(name = "Authentication", description = "Authentication API - register, login, and verify identity")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register with username, password, email, phone number and role")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "User registered successfully, verification codes sent"),
        ApiResponse(responseCode = "400", description = "Username, email, or phone number already exists")
    ])
    fun register(@RequestBody request: UserRegistrationRequest): ResponseEntity<UserResponse> {
        return try {
            val user = authenticationService.register(request)
            ResponseEntity.status(HttpStatus.CREATED).body(user)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login with username and password to receive JWT token")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Login successful"),
        ApiResponse(responseCode = "401", description = "Invalid credentials or unverified account")
    ])
    fun login(@RequestBody request: LoginRequest): ResponseEntity<LoginResponse> {
        return try {
            val response = authenticationService.login(request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PostMapping("/verify-email")
    @Operation(summary = "Verify email", description = "Verify email address with the code sent during registration")
    @ApiResponse(responseCode = "200", description = "Verification attempted")
    fun verifyEmail(@RequestBody request: VerifyEmailRequest): ResponseEntity<VerificationResponse> {
        return try {
            val response = authenticationService.verifyEmail(request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(VerificationResponse(false, e.message ?: "Verification failed"))
        }
    }

    @PostMapping("/verify-phone")
    @Operation(summary = "Verify phone number", description = "Verify phone number with the SMS code sent during registration")
    @ApiResponse(responseCode = "200", description = "Verification attempted")
    fun verifyPhone(@RequestBody request: VerifyPhoneRequest): ResponseEntity<VerificationResponse> {
        return try {
            val response = authenticationService.verifyPhone(request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(VerificationResponse(false, e.message ?: "Verification failed"))
        }
    }
}
