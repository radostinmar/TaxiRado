package com.taxirado.backend.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "rides")
data class Ride(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    val driver: User,

    @Column(nullable = false)
    val origin: String,

    @Column(nullable = false)
    val destination: String,

    @Column(nullable = false)
    val departureTime: LocalDateTime,

    @Column(nullable = false)
    val price: Double,

    @Column(nullable = false)
    val availableSeats: Int,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val status: RideStatus = RideStatus.AVAILABLE
)

enum class RideStatus {
    AVAILABLE,
    FULL,
    COMPLETED,
    CANCELLED
}
