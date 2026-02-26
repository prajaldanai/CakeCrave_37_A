package com.example.cakecrave

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented UI tests for CakeCrave Login & Signup screens.
 *
 * These tests run on an emulator/device using Jetpack Compose testing APIs.
 * No real Firebase calls are needed — we only verify UI behaviour.
 *
 * The app starts at WelcomeScreen, so each test first navigates:
 *   WelcomeScreen → LoginScreen
 */
@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    // ============================================================
    // HELPER: navigate from WelcomeScreen to LoginScreen
    // ============================================================
    private fun navigateToLogin() {
        composeRule.onNodeWithTag("getStartedBtn").performClick()
        composeRule.waitForIdle()
    }

    // ============================================================
    // TEST 1 — Email field accepts text input
    // ============================================================
    @Test
    fun emailField_acceptsTextInput() {
        navigateToLogin()

        composeRule.onNodeWithTag("email")
            .performTextInput("test@gmail.com")

        composeRule.onNodeWithTag("email")
            .assertTextContains("test@gmail.com")
    }

    // ============================================================
    // TEST 2 — Password field accepts text input
    // ============================================================
    @Test
    fun passwordField_acceptsTextInput() {
        navigateToLogin()

        composeRule.onNodeWithTag("password")
            .performTextInput("123456")

        // Password field uses visual transformation, so we check the node exists
        composeRule.onNodeWithTag("password")
            .assertExists()
    }

    // ============================================================
    // TEST 3 — Login button exists and is clickable
    // ============================================================
    @Test
    fun loginButton_existsAndClickable() {
        navigateToLogin()

        composeRule.onNodeWithTag("loginBtn")
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    // ============================================================
    // TEST 4 — Full login flow: type email + password, click login
    // (Does NOT depend on Firebase — only checks UI interaction)
    // ============================================================
    @Test
    fun loginFlow_typeCredentialsAndClickLogin() {
        navigateToLogin()

        composeRule.onNodeWithTag("email")
            .performTextInput("test@gmail.com")

        composeRule.onNodeWithTag("password")
            .performTextInput("123456")

        composeRule.onNodeWithTag("loginBtn")
            .assertIsDisplayed()
            .performClick()

        // After clicking login, the button should still exist
        // (Firebase may fail on test device — we only verify UI didn't crash)
        composeRule.onNodeWithTag("loginBtn")
            .assertExists()
    }

    // ============================================================
    // TEST 5 — Register navigation: click "Sign up" → SignupScreen appears
    // (Uses Compose navigation, NOT Intents — single-Activity app)
    // ============================================================
    @Test
    fun registerNavigation_opensSignupScreen() {
        navigateToLogin()

        // Click the "Sign up" link on Login screen
        composeRule.onNodeWithTag("registerBtn")
            .performClick()

        composeRule.waitForIdle()

        // Verify SignupScreen appeared by checking for its unique tags
        composeRule.onNodeWithTag("name")
            .assertExists()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("regEmail")
            .assertExists()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("regPassword")
            .assertExists()
            .assertIsDisplayed()

        composeRule.onNodeWithTag("createAccountBtn")
            .assertExists()
            .assertIsDisplayed()
            .assertHasClickAction()
    }

    // ============================================================
    // TEST 6 — Signup screen: fill all fields and verify button
    // ============================================================
    @Test
    fun signupScreen_fillFieldsAndVerifyButton() {
        navigateToLogin()

        // Navigate to Signup
        composeRule.onNodeWithTag("registerBtn").performClick()
        composeRule.waitForIdle()

        // Fill in the signup form
        composeRule.onNodeWithTag("name")
            .performTextInput("John Doe")

        composeRule.onNodeWithTag("regEmail")
            .performTextInput("john@gmail.com")

        composeRule.onNodeWithTag("regPassword")
            .performTextInput("pass123")

        // Verify Create Account button is displayed and clickable
        composeRule.onNodeWithTag("createAccountBtn")
            .assertIsDisplayed()
            .assertHasClickAction()
    }
}

