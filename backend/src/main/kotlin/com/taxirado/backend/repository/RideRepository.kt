package com.taxirado.backend.repository

import com.taxirado.backend.model.Ride
import com.taxirado.backend.model.RideStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface RideRepository : JpaRepository<Ride, Long> {
    fun findByStatus(status: RideStatus): List<Ride>
    fun findByDriverId(driverId: Long): List<Ride>
    fun findByDepartureTimeAfter(time: LocalDateTime): List<Ride>
}
