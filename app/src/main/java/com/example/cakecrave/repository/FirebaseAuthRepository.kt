package com.example.cakecrave.repository

import com.example.cakecrave.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class FirebaseAuthRepository : AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    // ================= SIGN UP =================
    override fun signup(
        name: String,              // ✅ MUST be here
        email: String,
        password: String,
        confirmPassword: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        if (password != confirmPassword) {
            onError("Passwords do not match")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val uid = auth.currentUser?.uid
                    if (uid == null) {
                        onError("User ID not found")
                        return@addOnCompleteListener
                    }

                    // ✅ SAVE FULL USER OBJECT
                    val user = User(
                        uid = uid,
                        email = email,
                        name = name
                    )

                    database.child("users")
                        .child(uid)
                        .setValue(user)
                        .addOnCompleteListener { dbTask ->
                            if (dbTask.isSuccessful) {
                                onSuccess("Signup successful")
                            } else {
                                onError("Signup succeeded but failed to save user data")
                            }
                        }

                } else {
                    onError(task.exception?.message ?: "Signup failed")
                }
            }
    }

    // ================= LOGIN =================
    override fun login(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess("Login successful")
                } else {
                    onError(task.exception?.message ?: "Login failed")
                }
            }
    }

    // ================= FORGOT PASSWORD =================
    override fun sendResetLink(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Failed to send reset email")
                }
            }
    }

    // ================= CONFIRM NEW PASSWORD =================
    override fun confirmNewPassword(
        oobCode: String,
        newPassword: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        auth.confirmPasswordReset(oobCode, newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception?.message ?: "Password reset failed")
                }
            }
    }
}
