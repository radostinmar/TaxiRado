package com.taxirado.backend.dto

import java.time.LocalDateTime

data class RideCreationRequest(
    val driverId: Long,
    val origin: String,
    val destination: String,
    val departureTime: LocalDateTime,
    val price: Double,
    val availableSeats: Int
)

data class RideResponse(
    val id: Long,
    val driverId: Long,
    val driverName: String,
    val origin: String,
    val destination: String,
    val departureTime: LocalDateTime,
    val price: Double,
    val availableSeats: Int,
    val status: String
)
