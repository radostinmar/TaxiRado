package com.taxirado.backend.dto

data class BookingRequest(
    val rideId: Long,
    val passengerId: Long,
    val seatsBooked: Int
)

data class BookingResponse(
    val id: Long,
    val rideId: Long,
    val passengerId: Long,
    val passengerName: String,
    val seatsBooked: Int,
    val status: String
)
