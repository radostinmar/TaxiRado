package com.taxirado.screens.role

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.taxirado.UserRole

/**
 * Screen where users select their role: Driver, Passenger, or Both
 */
@Composable
fun RoleSelectionScreen(onRoleSelected: (UserRole) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to TaxiRado", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Select your role:", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { onRoleSelected(UserRole.DRIVER) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("I'm a Driver")
        }
        
        Button(
            onClick = { onRoleSelected(UserRole.PASSENGER) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("I'm a Passenger")
        }
        
        Button(
            onClick = { onRoleSelected(UserRole.BOTH) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            Text("I'm Both")
        }
    }
}
