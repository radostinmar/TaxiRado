package com.taxirado.backend.controller

import com.taxirado.backend.dto.*
import com.taxirado.backend.model.User
import com.taxirado.backend.repository.UserRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["*"])
@Tag(name = "Users", description = "User management API - registration and user information")
class UserController(private val userRepository: UserRepository) {

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Register a new user with a role (DRIVER, PASSENGER, or BOTH)")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "User successfully registered"),
        ApiResponse(responseCode = "409", description = "Username already exists")
    ])
    fun register(@RequestBody request: UserRegistrationRequest): ResponseEntity<UserResponse> {
        val existingUser = userRepository.findByUsername(request.username)
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build()
        }

        val user = User(
            username = request.username,
            password = request.password,
            email = request.email,
            role = request.role
        )
        val savedUser = userRepository.save(user)
        val response = UserResponse(
            id = savedUser.id!!,
            username = savedUser.username,
            email = savedUser.email,
            role = savedUser.role
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

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
            role = user.role
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
                role = user.role
            )
        }
        return ResponseEntity.ok(responses)
    }
}
