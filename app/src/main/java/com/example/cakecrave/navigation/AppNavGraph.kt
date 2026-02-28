package com.example.cakecrave.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.cakecrave.view.*
import com.example.cakecrave.view.favorites.FavoritesScreen
import com.example.cakecrave.viewmodel.*

@Composable
fun AppNavGraph(
    navController: NavHostController
) {

    val authViewModel: AuthViewModel = viewModel()

    // ✅ SINGLE SOURCE OF TRUTH (VERY IMPORTANT)
    val productViewModel: ProductViewModel = viewModel()
    val favoritesViewModel: FavoritesViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {

        composable(Routes.WELCOME) {
            WelcomeScreen {
                navController.navigate(Routes.LOGIN)
            }
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onSignupClick = { navController.navigate(Routes.SIGNUP) },
                onForgotPassword = {
                    // Guard: only navigate if we're still on LOGIN
                    if (navController.currentDestination?.route == Routes.LOGIN) {
                        navController.navigate(Routes.FORGOT_PASSWORD)
                    }
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignupScreen(
                viewModel = authViewModel,
                onSignupSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SIGNUP) { inclusive = true }
                    }
                },
                onLoginClick = { navController.popBackStack() }
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                viewModel = authViewModel,
                onEmailSent = {
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                navController = navController,
                productViewModel = productViewModel,
                favoritesViewModel = favoritesViewModel
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                navController = navController,
                favoritesViewModel = favoritesViewModel
            )
        }

        composable(
            route = Routes.PRODUCT_DETAIL,
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val productId =
                backStackEntry.arguments?.getString("productId")
                    ?: return@composable

            ProductDetailRoute(
                productId = productId,
                productVM = productViewModel, // ✅ SAME INSTANCE
                onBack = { navController.popBackStack() }
            )
        }
    }
}
