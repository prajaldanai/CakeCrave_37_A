package com.example.cakecrave.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cakecrave.view.*
import com.example.cakecrave.view.favorites.FavoritesScreen
import com.example.cakecrave.viewmodel.AuthViewModel
import com.example.cakecrave.viewmodel.FavoritesViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController
) {
    // ✅ Auth VM (shared across auth screens)
    val authViewModel: AuthViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {

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
                    navController.popBackStack()
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

        // ================= DASHBOARD =================
        composable(Routes.DASHBOARD) {

            // ✅ Favorites VM CREATED AFTER LOGIN
            val favoritesViewModel: FavoritesViewModel = viewModel()

            DashboardScreen(
                navController = navController,
                favoritesViewModel = favoritesViewModel
            )
        }

        // ================= FAVORITES =================
        composable(Routes.FAVORITES) {

            // ✅ SAME Favorites VM INSTANCE (shared)
            val favoritesViewModel: FavoritesViewModel = viewModel()

            FavoritesScreen(
                navController = navController,          // ✅ FIX: PASS NAV CONTROLLER
                favoritesViewModel = favoritesViewModel // ✅ KEEP THIS
            )
        }
    }
}
