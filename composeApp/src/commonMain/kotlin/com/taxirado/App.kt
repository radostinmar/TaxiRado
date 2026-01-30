package com.taxirado

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.taxirado.navigation.Routes
import com.taxirado.screens.auth.RegistrationScreen
import com.taxirado.screens.driver.DriverHomeScreen
import com.taxirado.screens.menu.MainMenuScreen
import com.taxirado.screens.passenger.PassengerHomeScreen
import com.taxirado.screens.role.RoleSelectionScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Main application composable
 * Sets up navigation and manages user state across screens
 */
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
