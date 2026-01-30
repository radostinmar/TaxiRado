package com.taxirado

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.taxirado.navigation.Routes
import com.taxirado.screens.auth.*
import com.taxirado.screens.driver.DriverHomeScreen
import com.taxirado.screens.menu.MainMenuScreen
import com.taxirado.screens.passenger.PassengerHomeScreen
import com.taxirado.screens.role.RoleSelectionScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Main application composable
 * Sets up navigation and manages user state and authentication flow
 */
@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        var currentUser by remember { mutableStateOf<UserResponse?>(null) }
        var apiClient by remember { mutableStateOf(ApiClient()) }
        var pendingVerificationUser by remember { mutableStateOf<UserResponse?>(null) }

        NavHost(
            navController = navController,
            startDestination = Routes.LOGIN
        ) {
            // Login screen (new starting point)
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = { loginResponse ->
                        currentUser = loginResponse.user
                        apiClient.setToken(loginResponse.token)
                        when (loginResponse.user.role) {
                            UserRole.DRIVER -> navController.navigate(Routes.DRIVER_HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                            UserRole.PASSENGER -> navController.navigate(Routes.PASSENGER_HOME) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                            UserRole.BOTH -> navController.navigate(Routes.MAIN_MENU) {
                                popUpTo(Routes.LOGIN) { inclusive = true }
                            }
                        }
                    },
                    onNeedRegistration = {
                        navController.navigate(Routes.ROLE_SELECTION)
                    }
                )
            }

            // Role selection for new users
            composable(Routes.ROLE_SELECTION) {
                RoleSelectionScreen(
                    onRoleSelected = { role ->
                        navController.navigate(Routes.registration(role))
                    }
                )
            }

            // Registration screen
            composable(Routes.REGISTRATION) { backStackEntry ->
                val roleString = backStackEntry.arguments?.getString("role")
                val role = roleString?.let { UserRole.valueOf(it) } ?: UserRole.PASSENGER
                
                RegistrationScreen(
                    role = role,
                    onRegistered = { user ->
                        pendingVerificationUser = user
                        navController.navigate(Routes.VERIFY_EMAIL) {
                            popUpTo(Routes.ROLE_SELECTION) { inclusive = true }
                        }
                    }
                )
            }

            // Email verification
            composable(Routes.VERIFY_EMAIL) {
                pendingVerificationUser?.let { user ->
                    EmailVerificationScreen(
                        user = user,
                        onVerified = {
                            navController.navigate(Routes.VERIFY_PHONE)
                        }
                    )
                }
            }

            // Phone verification
            composable(Routes.VERIFY_PHONE) {
                pendingVerificationUser?.let { user ->
                    PhoneVerificationScreen(
                        user = user,
                        onVerified = {
                            // Both verifications complete, go to login
                            pendingVerificationUser = null
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }

            // Main menu for BOTH role users
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

            // Driver home screen
            composable(Routes.DRIVER_HOME) {
                DriverHomeScreen(
                    user = currentUser,
                    onBack = {
                        if (currentUser?.role == UserRole.BOTH) {
                            navController.popBackStack()
                        } else {
                            apiClient.clearToken()
                            currentUser = null
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }

            // Passenger home screen
            composable(Routes.PASSENGER_HOME) {
                PassengerHomeScreen(
                    user = currentUser,
                    onBack = {
                        if (currentUser?.role == UserRole.BOTH) {
                            navController.popBackStack()
                        } else {
                            apiClient.clearToken()
                            currentUser = null
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }
        }
    }
}
