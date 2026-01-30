package com.taxirado.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "bookings")
data class Booking(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "ride_id", nullable = false)
    val ride: Ride,

    @ManyToOne
    @JoinColumn(name = "passenger_id", nullable = false)
    val passenger: User,

    @Column(nullable = false)
    val seatsBooked: Int = 1,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: BookingStatus = BookingStatus.CONFIRMED
)

enum class BookingStatus {
    CONFIRMED,
    CANCELLED
}
