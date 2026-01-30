package com.taxirado

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClient(private val baseUrl: String = "http://localhost:8080") {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun registerUser(request: UserRegistrationRequest): Result<UserResponse> = try {
        val response: UserResponse = client.post("$baseUrl/api/users/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getAllUsers(): Result<List<UserResponse>> = try {
        val response: List<UserResponse> = client.get("$baseUrl/api/users").body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun createRide(request: RideCreationRequest): Result<RideResponse> = try {
        val response: RideResponse = client.post("$baseUrl/api/rides") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getAvailableRides(): Result<List<RideResponse>> = try {
        val response: List<RideResponse> = client.get("$baseUrl/api/rides/available").body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun createBooking(request: BookingRequest): Result<BookingResponse> = try {
        val response: BookingResponse = client.post("$baseUrl/api/bookings") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getPassengerBookings(passengerId: Long): Result<List<BookingResponse>> = try {
        val response: List<BookingResponse> = client.get("$baseUrl/api/bookings/passenger/$passengerId").body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
