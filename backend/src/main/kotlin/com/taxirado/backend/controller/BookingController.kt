package com.taxirado.backend.controller

import com.taxirado.backend.dto.BookingRequest
import com.taxirado.backend.dto.BookingResponse
import com.taxirado.backend.model.Booking
import com.taxirado.backend.model.RideStatus
import com.taxirado.backend.repository.BookingRepository
import com.taxirado.backend.repository.RideRepository
import com.taxirado.backend.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = ["*"])
class BookingController(
    private val bookingRepository: BookingRepository,
    private val rideRepository: RideRepository,
    private val userRepository: UserRepository
) {

    @PostMapping
    fun createBooking(@RequestBody request: BookingRequest): ResponseEntity<BookingResponse> {
        val ride = rideRepository.findById(request.rideId).orElse(null)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        val passenger = userRepository.findById(request.passengerId).orElse(null)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        if (ride.availableSeats < request.seatsBooked) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
        }

        val booking = Booking(
            ride = ride,
            passenger = passenger,
            seatsBooked = request.seatsBooked
        )
        val savedBooking = bookingRepository.save(booking)

        // Update available seats
        val updatedRide = ride.copy(
            availableSeats = ride.availableSeats - request.seatsBooked,
            status = if (ride.availableSeats - request.seatsBooked == 0) RideStatus.FULL else ride.status
        )
        rideRepository.save(updatedRide)

        val response = BookingResponse(
            id = savedBooking.id!!,
            rideId = ride.id!!,
            passengerId = passenger.id!!,
            passengerName = passenger.username,
            seatsBooked = savedBooking.seatsBooked,
            status = savedBooking.status.name
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }

    @GetMapping("/ride/{rideId}")
    fun getBookingsByRide(@PathVariable rideId: Long): ResponseEntity<List<BookingResponse>> {
        val bookings = bookingRepository.findByRideId(rideId)
        val responses = bookings.map { booking ->
            BookingResponse(
                id = booking.id!!,
                rideId = booking.ride.id!!,
                passengerId = booking.passenger.id!!,
                passengerName = booking.passenger.username,
                seatsBooked = booking.seatsBooked,
                status = booking.status.name
            )
        }
        return ResponseEntity.ok(responses)
    }

    @GetMapping("/passenger/{passengerId}")
    fun getBookingsByPassenger(@PathVariable passengerId: Long): ResponseEntity<List<BookingResponse>> {
        val bookings = bookingRepository.findByPassengerId(passengerId)
        val responses = bookings.map { booking ->
            BookingResponse(
                id = booking.id!!,
                rideId = booking.ride.id!!,
                passengerId = booking.passenger.id!!,
                passengerName = booking.passenger.username,
                seatsBooked = booking.seatsBooked,
                status = booking.status.name
            )
        }
        return ResponseEntity.ok(responses)
    }

    @GetMapping("/{id}")
    fun getBooking(@PathVariable id: Long): ResponseEntity<BookingResponse> {
        val booking = bookingRepository.findById(id).orElse(null)
            ?: return ResponseEntity.notFound().build()

        val response = BookingResponse(
            id = booking.id!!,
            rideId = booking.ride.id!!,
            passengerId = booking.passenger.id!!,
            passengerName = booking.passenger.username,
            seatsBooked = booking.seatsBooked,
            status = booking.status.name
        )
        return ResponseEntity.ok(response)
    }
}
