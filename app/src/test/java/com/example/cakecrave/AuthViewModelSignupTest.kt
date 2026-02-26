package com.example.cakecrave

import com.example.cakecrave.repository.AuthRepository
import com.example.cakecrave.viewmodel.AuthViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AuthViewModelSignupTest {

    // ========== FAKE REPOSITORY (no Mockito needed) ==========
    private class FakeAuthRepository(
        private val shouldSucceed: Boolean,
        private val fakeMessage: String
    ) : AuthRepository {

        var signupCalled = false
        var lastSignupName = ""
        var lastSignupEmail = ""

        override fun signup(
            name: String,
            email: String,
            password: String,
            confirmPassword: String,
            onSuccess: (String) -> Unit,
            onError: (String) -> Unit
        ) {
            signupCalled = true
            lastSignupName = name
            lastSignupEmail = email
            if (shouldSucceed) {
                onSuccess(fakeMessage)
            } else {
                onError(fakeMessage)
            }
        }

        override fun login(
            email: String,
            password: String,
            onSuccess: (String) -> Unit,
            onError: (String) -> Unit
        ) { }

        override fun sendResetLink(
            email: String,
            onSuccess: () -> Unit,
            onError: (String) -> Unit
        ) { }

        override fun confirmNewPassword(
            oobCode: String,
            newPassword: String,
            onSuccess: () -> Unit,
            onError: (String) -> Unit
        ) { }
    }

    // ========== SIGNUP SUCCESS ==========
    @Test
    fun signup_success_test() {
        val repo = FakeAuthRepository(shouldSucceed = true, fakeMessage = "Signup successful")
        val viewModel = AuthViewModel(repo)

        var errorMsg = ""
        viewModel.signup("John", "john@gmail.com", "pass123", "pass123") { error ->
            errorMsg = error
        }

        // loginSuccess should be true (auto-login after signup)
        assertTrue(repo.signupCalled)
        assertEquals("John", repo.lastSignupName)
        assertEquals("john@gmail.com", repo.lastSignupEmail)
        assertTrue(viewModel.loginSuccess.value)
        assertEquals("", errorMsg)
    }

    // ========== SIGNUP FAILURE ==========
    @Test
    fun signup_failed_test() {
        val repo = FakeAuthRepository(shouldSucceed = false, fakeMessage = "Email already in use")
        val viewModel = AuthViewModel(repo)

        var errorMsg = ""
        viewModel.signup("John", "john@gmail.com", "pass123", "pass123") { error ->
            errorMsg = error
        }

        assertTrue(repo.signupCalled)
        assertFalse(viewModel.loginSuccess.value)
        assertEquals("Email already in use", errorMsg)
    }
}

