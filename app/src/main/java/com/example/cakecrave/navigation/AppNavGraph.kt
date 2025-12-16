package com.example.cakecrave.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cakecrave.view.*
import com.example.cakecrave.viewmodel.AuthViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {

        // ================= DASHBOARD =================
        composable(Routes.DASHBOARD) {
            DashboardScreen()   // ✅ NO PARAMETERS
        }

        // ================= WELCOME =================
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        // ================= LOGIN =================
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onSignupClick = {
                    navController.navigate(Routes.SIGNUP)
                },
                onForgotPassword = {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                }
            )
        }

        // ================= SIGNUP =================
        composable(Routes.SIGNUP) {
            SignupScreen(
                viewModel = authViewModel,
                onSignupSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGNUP) { inclusive = true }
                    }
                },
                onLoginClick = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }

        // ================= FORGOT PASSWORD =================
        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onEmailSent = {
                    navController.navigate(Routes.RESET_PASSWORD)
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // ================= RESET PASSWORD =================
        composable(Routes.RESET_PASSWORD) {
            ResetPasswordScreen(
                onBackToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
    }
}
