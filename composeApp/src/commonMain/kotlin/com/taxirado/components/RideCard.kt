package com.taxirado.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taxirado.RideResponse

/**
 * Card component to display ride information
 * Shows ride details and allows passengers to book
 */
@Composable
fun RideCard(ride: RideResponse, onBook: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("From: ${ride.origin}", style = MaterialTheme.typography.titleMedium)
            Text("To: ${ride.destination}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Driver: ${ride.driverName}")
            Text("Departure: ${ride.departureTime}")
            Text("Price: $${ride.price}")
            Text("Available Seats: ${ride.availableSeats}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onBook,
                enabled = ride.availableSeats > 0,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (ride.availableSeats > 0) "Book Now" else "Full")
            }
        }
    }
}
