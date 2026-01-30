package com.taxirado.backend.controller

import com.taxirado.backend.dto.*
import com.taxirado.backend.model.User
import com.taxirado.backend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = ["*"])
class UserController(private val userRepository: UserRepository) {

    @PostMapping("/register")
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
