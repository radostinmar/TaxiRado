package com.taxirado

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    DRIVER,
    PASSENGER,
    BOTH
}

@Serializable
data class UserRegistrationRequest(
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val role: UserRole
)

@Serializable
data class UserResponse(
    val id: Long,
    val username: String,
    val email: String,
    val phoneNumber: String,
    val role: UserRole,
    val emailVerified: Boolean,
    val phoneVerified: Boolean
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val token: String,
    val user: UserResponse
)

@Serializable
data class VerifyEmailRequest(
    val userId: Long,
    val verificationCode: String
)

@Serializable
data class VerifyPhoneRequest(
    val userId: Long,
    val verificationCode: String
)

@Serializable
data class VerificationResponse(
    val success: Boolean,
    val message: String
)

@Serializable
data class RideCreationRequest(
    val driverId: Long,
    val origin: String,
    val destination: String,
    val departureTime: String,
    val price: Double,
    val availableSeats: Int
)

@Serializable
data class RideResponse(
    val id: Long,
    val driverId: Long,
    val driverName: String,
    val origin: String,
    val destination: String,
    val departureTime: String,
    val price: Double,
    val availableSeats: Int,
    val status: String
)

@Serializable
data class BookingRequest(
    val rideId: Long,
    val passengerId: Long,
    val seatsBooked: Int
)

@Serializable
data class BookingResponse(
    val id: Long,
    val rideId: Long,
    val passengerId: Long,
    val passengerName: String,
    val seatsBooked: Int,
    val status: String
)
