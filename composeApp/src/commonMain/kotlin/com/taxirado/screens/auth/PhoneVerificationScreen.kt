package com.taxirado.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.taxirado.ApiClient
import com.taxirado.UserResponse
import com.taxirado.VerifyPhoneRequest
import kotlinx.coroutines.launch

/**
 * Phone verification screen
 * Users enter the 6-digit SMS code sent to their phone
 */
@Composable
fun PhoneVerificationScreen(
    user: UserResponse,
    onVerified: () -> Unit
) {
    var verificationCode by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val apiClient = remember { ApiClient() }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Verify Your Phone", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "A 6-digit verification code has been sent to ${user.phoneNumber}",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "(Check your console logs for the code in this demo)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = verificationCode,
            onValueChange = { if (it.length <= 6) verificationCode = it },
            label = { Text("SMS Code") },
            placeholder = { Text("123456") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (successMessage.isNotEmpty()) {
            Text(
                text = successMessage,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                scope.launch {
                    isLoading = true
                    errorMessage = ""
                    successMessage = ""
                    val request = VerifyPhoneRequest(user.id, verificationCode)
                    val result = apiClient.verifyPhone(request)
                    result.onSuccess { response ->
                        if (response.success) {
                            successMessage = response.message
                            onVerified()
                        } else {
                            errorMessage = response.message
                        }
                    }.onFailure { error ->
                        errorMessage = "Verification failed: ${error.message}"
                    }
                    isLoading = false
                }
            },
            enabled = !isLoading && verificationCode.length == 6,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Verify Phone")
            }
        }
    }
}