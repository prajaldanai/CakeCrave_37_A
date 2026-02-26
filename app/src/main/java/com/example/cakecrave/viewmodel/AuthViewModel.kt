package com.example.cakecrave.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.cakecrave.repository.AuthRepository
import com.example.cakecrave.repository.FirebaseAuthRepository

class AuthViewModel(
    private val repository: AuthRepository = FirebaseAuthRepository()
) : ViewModel() {

    // ================= LOGIN STATE =================
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    // ================= SIGN UP =================
    fun signup(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        onError: (String) -> Unit
    ) {
        repository.signup(
            name = name,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            onSuccess = {
                // ✅ Auto-login after signup
                _loginSuccess.value = true
            },
            onError = onError
        )
    }

    // ================= LOGIN =================
    fun login(
        email: String,
        password: String,
        onError: (String) -> Unit
    ) {
        repository.login(
            email = email,
            password = password,
            onSuccess = {
                _loginSuccess.value = true
            },
            onError = onError
        )
    }

    // ================= RESET LOGIN STATE =================
    fun resetLoginState() {
        _loginSuccess.value = false
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
