package com.taxirado.screens.passenger

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taxirado.ApiClient
import com.taxirado.BookingRequest
import com.taxirado.RideResponse
import com.taxirado.UserResponse
import com.taxirado.components.RideCard
import kotlinx.coroutines.launch

/**
 * Home screen for passengers
 * Displays available rides and allows booking
 */
@Composable
fun PassengerHomeScreen(user: UserResponse?, onBack: () -> Unit) {
    var rides by remember { mutableStateOf<List<RideResponse>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val apiClient = remember { ApiClient() }

    LaunchedEffect(Unit) {
        isLoading = true
        val result = apiClient.getAvailableRides()
        result.onSuccess { ridesList ->
            rides = ridesList
        }.onFailure { error ->
            errorMessage = "Failed to load rides: ${error.message}"
        }
        isLoading = false
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Available Rides", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (successMessage.isNotEmpty()) {
            Text(successMessage, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(rides) { ride ->
                    RideCard(
                        ride = ride,
                        onBook = {
                            scope.launch {
                                user?.id?.let { userId ->
                                    val request = BookingRequest(
                                        rideId = ride.id,
                                        passengerId = userId,
                                        seatsBooked = 1
                                    )
                                    val result = apiClient.createBooking(request)
                                    result.onSuccess {
                                        successMessage = "Booking successful!"
                                        // Refresh rides
                                        val refreshResult = apiClient.getAvailableRides()
                                        refreshResult.onSuccess { ridesList ->
                                            rides = ridesList
                                        }
                                    }.onFailure { error ->
                                        errorMessage = "Booking failed: ${error.message}"
                                    }
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
