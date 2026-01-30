package com.taxirado.navigation

import com.taxirado.UserRole

/**
 * Navigation routes for the TaxiRado app
 */
object Routes {
    const val ROLE_SELECTION = "role_selection"
    const val REGISTRATION = "registration/{role}"
    const val MAIN_MENU = "main_menu"
    const val DRIVER_HOME = "driver_home"
    const val PASSENGER_HOME = "passenger_home"
    
    fun registration(role: UserRole) = "registration/${role.name}"
}
