package com.example.cakecrave.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cakecrave.repository.AuthRepository
import com.example.cakecrave.repository.FirebaseAuthRepository

class AuthViewModel : ViewModel() {

    private val repository: AuthRepository = FirebaseAuthRepository()

    // ================= SIGN UP =================
    fun signup(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        repository.signup(
            name = name,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    // ================= LOGIN =================
    fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        repository.login(
            email = email,
            password = password,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    // ================= FORGOT PASSWORD =================

    // Step 1: Send reset email
    fun sendResetLink(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        repository.sendResetLink(
            email = email,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    // Step 2: Confirm new password (deep-link only)
    fun confirmNewPassword(
        oobCode: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        repository.confirmNewPassword(
            oobCode = oobCode,
            newPassword = newPassword,
            onSuccess = onSuccess,
            onError = onError
        )
    }
}
