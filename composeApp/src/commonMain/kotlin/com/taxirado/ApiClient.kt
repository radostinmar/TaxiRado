package com.taxirado

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * API Client for TaxiRado backend
 * Handles HTTP requests with JWT token authentication
 */
class ApiClient(
    private val baseUrl: String = "http://localhost:8080",
    private var jwtToken: String? = null
) {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    fun setToken(token: String) {
        jwtToken = token
    }

    fun clearToken() {
        jwtToken = null
    }

    private fun HttpRequestBuilder.addAuthHeader() {
        jwtToken?.let { token ->
            header("Authorization", "Bearer $token")
        }
    }

    // Authentication endpoints
    suspend fun register(request: UserRegistrationRequest): Result<UserResponse> = try {
        val response: UserResponse = client.post("$baseUrl/api/auth/register") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun login(request: LoginRequest): Result<LoginResponse> = try {
        val response: LoginResponse = client.post("$baseUrl/api/auth/login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        setToken(response.token)
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun verifyEmail(request: VerifyEmailRequest): Result<VerificationResponse> = try {
        val response: VerificationResponse = client.post("$baseUrl/api/auth/verify-email") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun verifyPhone(request: VerifyPhoneRequest): Result<VerificationResponse> = try {
        val response: VerificationResponse = client.post("$baseUrl/api/auth/verify-phone") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // User endpoints (require authentication)
    suspend fun getAllUsers(): Result<List<UserResponse>> = try {
        val response: List<UserResponse> = client.get("$baseUrl/api/users") {
            addAuthHeader()
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Ride endpoints (require authentication)
    suspend fun createRide(request: RideCreationRequest): Result<RideResponse> = try {
        val response: RideResponse = client.post("$baseUrl/api/rides") {
            contentType(ContentType.Application.Json)
            addAuthHeader()
            setBody(request)
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getAvailableRides(): Result<List<RideResponse>> = try {
        val response: List<RideResponse> = client.get("$baseUrl/api/rides/available") {
            addAuthHeader()
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    // Booking endpoints (require authentication)
    suspend fun createBooking(request: BookingRequest): Result<BookingResponse> = try {
        val response: BookingResponse = client.post("$baseUrl/api/bookings") {
            contentType(ContentType.Application.Json)
            addAuthHeader()
            setBody(request)
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getPassengerBookings(passengerId: Long): Result<List<BookingResponse>> = try {
        val response: List<BookingResponse> = client.get("$baseUrl/api/bookings/passenger/$passengerId") {
            addAuthHeader()
        }.body()
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
