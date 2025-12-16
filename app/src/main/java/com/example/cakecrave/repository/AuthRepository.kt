package com.example.cakecrave.repository

interface AuthRepository {

    // ================= SIGN UP =================
    fun signup(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    )

    // ================= LOGIN =================
    fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    )

    // ================= FORGOT PASSWORD =================

    // Step 1: Send reset link to Gmail
    fun sendResetLink(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    // Step 2: Confirm new password (used only for deep-link reset)
    fun confirmNewPassword(
        oobCode: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}
