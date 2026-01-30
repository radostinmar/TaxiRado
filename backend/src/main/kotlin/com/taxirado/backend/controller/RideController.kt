package com.taxirado.backend.controller

import com.taxirado.backend.dto.RideCreationRequest
import com.taxirado.backend.dto.RideResponse
import com.taxirado.backend.model.Ride
import com.taxirado.backend.model.RideStatus
import com.taxirado.backend.repository.RideRepository
import com.taxirado.backend.repository.UserRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/rides")
@CrossOrigin(origins = ["*"])
@Tag(name = "Rides", description = "Ride management API - create and browse rides")
class RideController(
    private val rideRepository: RideRepository,
    private val userRepository: UserRepository
) {

    @PostMapping
    @Operation(summary = "Create a new ride", description = "Drivers can announce a new ride with origin, destination, time, and price")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Ride created successfully"),
        ApiResponse(responseCode = "404", description = "Driver not found")
    ])
    fun createRide(@RequestBody request: RideCreationRequest): ResponseEntity<RideResponse> {
        val driver = userRepository.findById(request.driverId).orElse(null)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        val ride = Ride(
            driver = driver,
            origin = request.origin,
            destination = request.destination,
            departureTime = request.departureTime,
            price = request.price,
            availableSeats = request.availableSeats
        )
        val savedRide = rideRepository.save(ride)
        val response = RideResponse(
            id = savedRide.id!!,
            driverId = driver.id!!,
            driverName = driver.username,
            origin = savedRide.origin,
            destination = savedRide.destination,
            departureTime = savedRide.departureTime,
            price = savedRide.price,
            availableSeats = savedRide.availableSeats,
            status = savedRide.status.name
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping
    @Operation(summary = "Get all rides", description = "Retrieve all rides in the system")
    @ApiResponse(responseCode = "200", description = "List of all rides")
    fun getAllRides(): ResponseEntity<List<RideResponse>> {
        val rides = rideRepository.findAll()
        val responses = rides.map { ride ->
            RideResponse(
                id = ride.id!!,
                driverId = ride.driver.id!!,
                driverName = ride.driver.username,
                origin = ride.origin,
                destination = ride.destination,
                departureTime = ride.departureTime,
                price = ride.price,
                availableSeats = ride.availableSeats,
                status = ride.status.name
            )
        }
        return ResponseEntity.ok(responses)
    }

    @GetMapping("/available")
    @Operation(summary = "Get available rides", description = "Retrieve all available future rides that passengers can book")
    @ApiResponse(responseCode = "200", description = "List of available rides")
    fun getAvailableRides(): ResponseEntity<List<RideResponse>> {
        val rides = rideRepository.findByStatus(RideStatus.AVAILABLE)
            .filter { it.departureTime.isAfter(LocalDateTime.now()) }
        val responses = rides.map { ride ->
            RideResponse(
                id = ride.id!!,
                driverId = ride.driver.id!!,
                driverName = ride.driver.username,
                origin = ride.origin,
                destination = ride.destination,
                departureTime = ride.departureTime,
                price = ride.price,
                availableSeats = ride.availableSeats,
                status = ride.status.name
            )
        }
        return ResponseEntity.ok(responses)
    }

    @GetMapping("/{id}")
    fun getRide(@PathVariable id: Long): ResponseEntity<RideResponse> {
        val ride = rideRepository.findById(id).orElse(null)
            ?: return ResponseEntity.notFound().build()

        val response = RideResponse(
            id = ride.id!!,
            driverId = ride.driver.id!!,
            driverName = ride.driver.username,
            origin = ride.origin,
            destination = ride.destination,
            departureTime = ride.departureTime,
            price = ride.price,
            availableSeats = ride.availableSeats,
            status = ride.status.name
        )
        return ResponseEntity.ok(response)
    }

    @GetMapping("/driver/{driverId}")
    fun getRidesByDriver(@PathVariable driverId: Long): ResponseEntity<List<RideResponse>> {
        val rides = rideRepository.findByDriverId(driverId)
        val responses = rides.map { ride ->
            RideResponse(
                id = ride.id!!,
                driverId = ride.driver.id!!,
                driverName = ride.driver.username,
                origin = ride.origin,
                destination = ride.destination,
                departureTime = ride.departureTime,
                price = ride.price,
                availableSeats = ride.availableSeats,
                status = ride.status.name
            )
        }
        return ResponseEntity.ok(responses)
    }
}
