package com.example.cakecrave

import com.example.cakecrave.repository.AuthRepository
import com.example.cakecrave.viewmodel.AuthViewModel
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.*

class AuthViewModelTest {

    // ========== LOGIN SUCCESS ==========
    @Test
    fun login_success_test() {
        val repo = mock<AuthRepository>()
        val viewModel = AuthViewModel(repo)

        // repo.login has params: (email, password, onSuccess, onError)
        // onSuccess is at index 2, type (String) -> Unit
        doAnswer { invocation ->
            val onSuccess = invocation.getArgument<(String) -> Unit>(2)
            onSuccess("Login successful")
            null
        }.`when`(repo).login(eq("test@gmail.com"), eq("123456"), any(), any())

        var errorMsg = ""
        viewModel.login("test@gmail.com", "123456") { error ->
            errorMsg = error
        }

        // loginSuccess should be true after success callback
        assertTrue(viewModel.loginSuccess.value)
        assertEquals("", errorMsg)
        verify(repo).login(eq("test@gmail.com"), eq("123456"), any(), any())
    }

    // ========== LOGIN FAILURE ==========
    @Test
    fun login_failed_test() {
        val repo = mock<AuthRepository>()
        val viewModel = AuthViewModel(repo)

        // onError is at index 3, type (String) -> Unit
        doAnswer { invocation ->
            val onError = invocation.getArgument<(String) -> Unit>(3)
            onError("Invalid credentials")
            null
        }.`when`(repo).login(eq("wrong@gmail.com"), eq("wrong"), any(), any())

        var errorMsg = ""
        viewModel.login("wrong@gmail.com", "wrong") { error ->
            errorMsg = error
        }

        // loginSuccess should remain false
        assertFalse(viewModel.loginSuccess.value)
        assertEquals("Invalid credentials", errorMsg)
        verify(repo).login(eq("wrong@gmail.com"), eq("wrong"), any(), any())
    }
}

