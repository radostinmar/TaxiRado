package com.taxirado.backend.repository

import com.taxirado.backend.model.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : JpaRepository<Booking, Long> {
    fun findByRideId(rideId: Long): List<Booking>
    fun findByPassengerId(passengerId: Long): List<Booking>
}
