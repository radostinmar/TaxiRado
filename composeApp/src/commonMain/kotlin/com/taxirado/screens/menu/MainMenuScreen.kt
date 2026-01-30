package com.taxirado.screens.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Main menu screen for users with "BOTH" role
 * Allows them to choose between driver and passenger modes
 */
@Composable
fun MainMenuScreen(onDriverSelected: () -> Unit, onPassengerSelected: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("What would you like to do?", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDriverSelected,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Offer a Ride")
        }

        Button(
            onClick = onPassengerSelected,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("Find a Ride")
        }
    }
}
