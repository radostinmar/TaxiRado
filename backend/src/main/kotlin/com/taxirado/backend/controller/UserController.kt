package com.taxirado.backend.controller

import com.taxirado.backend.dto.*
import com.taxirado.backend.repository.UserRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["*"])
@Tag(name = "Users", description = "User management API - user information (use /api/auth for registration)")
class UserController(private val userRepository: UserRepository) {

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve user information by user ID")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "User found"),
        ApiResponse(responseCode = "404", description = "User not found")
    ])
    fun getUser(@PathVariable id: Long): ResponseEntity<UserResponse> {
        val user = userRepository.findById(id).orElse(null)
            ?: return ResponseEntity.notFound().build()

        val response = UserResponse(
            id = user.id!!,
            username = user.username,
            email = user.email,
            phoneNumber = user.phoneNumber,
            role = user.role,
            emailVerified = user.emailVerified,
            phoneVerified = user.phoneVerified
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val users = userRepository.findAll()
        val responses = users.map { user ->
            UserResponse(
                id = user.id!!,
                username = user.username,
                email = user.email,
                phoneNumber = user.phoneNumber,
                role = user.role,
                emailVerified = user.emailVerified,
                phoneVerified = user.phoneVerified
            )
        }
        return ResponseEntity.ok(responses)
    }
}
