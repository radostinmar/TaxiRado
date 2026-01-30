package com.taxirado

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

// Navigation routes
object Routes {
    const val ROLE_SELECTION = "role_selection"
    const val REGISTRATION = "registration/{role}"
    const val MAIN_MENU = "main_menu"
    const val DRIVER_HOME = "driver_home"
    const val PASSENGER_HOME = "passenger_home"
    
    fun registration(role: UserRole) = "registration/${role.name}"
}

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        var currentUser by remember { mutableStateOf<UserResponse?>(null) }

        NavHost(
            navController = navController,
            startDestination = Routes.ROLE_SELECTION
        ) {
            composable(Routes.ROLE_SELECTION) {
                RoleSelectionScreen(
                    onRoleSelected = { role ->
                        navController.navigate(Routes.registration(role))
                    }
                )
            }

            composable(Routes.REGISTRATION) { backStackEntry ->
                val roleString = backStackEntry.arguments?.getString("role")
                val role = roleString?.let { UserRole.valueOf(it) } ?: UserRole.PASSENGER
                
                RegistrationScreen(
                    role = role,
                    onRegistered = { user ->
                        currentUser = user
                        when (user.role) {
                            UserRole.DRIVER -> navController.navigate(Routes.DRIVER_HOME) {
                                popUpTo(Routes.ROLE_SELECTION) { inclusive = true }
                            }
                            UserRole.PASSENGER -> navController.navigate(Routes.PASSENGER_HOME) {
                                popUpTo(Routes.ROLE_SELECTION) { inclusive = true }
                            }
                            UserRole.BOTH -> navController.navigate(Routes.MAIN_MENU) {
                                popUpTo(Routes.ROLE_SELECTION) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable(Routes.MAIN_MENU) {
                MainMenuScreen(
                    onDriverSelected = {
                        navController.navigate(Routes.DRIVER_HOME)
                    },
                    onPassengerSelected = {
                        navController.navigate(Routes.PASSENGER_HOME)
                    }
                )
            }

            composable(Routes.DRIVER_HOME) {
                DriverHomeScreen(
                    user = currentUser,
                    onBack = {
                        if (currentUser?.role == UserRole.BOTH) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(Routes.ROLE_SELECTION) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable(Routes.PASSENGER_HOME) {
                PassengerHomeScreen(
                    user = currentUser,
                    onBack = {
                        if (currentUser?.role == UserRole.BOTH) {
                            navController.popBackStack()
                        } else {
                            navController.navigate(Routes.ROLE_SELECTION) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    }
}

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

@Composable
fun RegistrationScreen(role: UserRole, onRegistered: (UserResponse) -> Unit) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val apiClient = remember { ApiClient() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Register as ${role.name}", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = ""
                    val request = UserRegistrationRequest(username, password, email, role)
                    val result = apiClient.registerUser(request)
                    result.onSuccess { user ->
                        onRegistered(user)
                    }.onFailure { error ->
                        errorMessage = "Registration failed: ${error.message}"
                    }
                    isLoading = false
                }
            },
            enabled = !isLoading && username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Register")
            }
        }
    }
}

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

@Composable
fun DriverHomeScreen(user: UserResponse?, onBack: () -> Unit) {
    var origin by remember { mutableStateOf("") }
    var destination by remember { mutableStateOf("") }
    var departureTime by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var seats by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val apiClient = remember { ApiClient() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Button(onClick = onBack) {
            Text("Back")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text("Announce Your Ride", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = origin,
            onValueChange = { origin = it },
            label = { Text("Origin") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Destination") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = departureTime,
            onValueChange = { departureTime = it },
            label = { Text("Departure Time (YYYY-MM-DDTHH:MM:SS)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = seats,
            onValueChange = { seats = it },
            label = { Text("Available Seats") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (successMessage.isNotEmpty()) {
            Text(successMessage, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = ""
                    successMessage = ""
                    user?.id?.let { userId ->
                        val request = RideCreationRequest(
                            driverId = userId,
                            origin = origin,
                            destination = destination,
                            departureTime = departureTime,
                            price = price.toDoubleOrNull() ?: 0.0,
                            availableSeats = seats.toIntOrNull() ?: 0
                        )
                        val result = apiClient.createRide(request)
                        result.onSuccess {
                            successMessage = "Ride announced successfully!"
                            origin = ""
                            destination = ""
                            departureTime = ""
                            price = ""
                            seats = ""
                        }.onFailure { error ->
                            errorMessage = "Failed to announce ride: ${error.message}"
                        }
                    }
                    isLoading = false
                }
            },
            enabled = !isLoading && origin.isNotEmpty() && destination.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Announce Ride")
            }
        }
    }
}

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
