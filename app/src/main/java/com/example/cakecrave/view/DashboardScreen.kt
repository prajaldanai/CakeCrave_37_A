package com.example.cakecrave.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.cakecrave.navigation.Routes
import com.example.cakecrave.viewmodel.FavoritesViewModel
import com.example.cakecrave.viewmodel.ProfileViewModel
import com.example.cakecrave.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun DashboardScreen(
    navController: NavHostController,
    favoritesViewModel: FavoritesViewModel
) {

    // 🔐 Auth guard
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (currentUser == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Please login to continue")
        }
        return
    }

    // ✅ ViewModels (unchanged)
    val profileVM: ProfileViewModel = viewModel()
    val productVM: ProductViewModel = viewModel()

    // ✅ Load profile AFTER UI starts
    LaunchedEffect(Unit) {
        profileVM.loadProfile()
    }

    var selectedTab by remember { mutableStateOf(0) }

    val message by productVM.message.collectAsState()
    LaunchedEffect(message) {
        if (message.contains("success", ignoreCase = true)) {
            selectedTab = 0
            productVM.clearMessage()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (selectedTab) {

                // ================= HOME =================
                0 -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        DashboardHeader(profileVM = profileVM)

                        HomeContent(
                            productVM = productVM,
                            favoritesViewModel = favoritesViewModel, // ✅ PASS HERE
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }
                }

                // ================= ADD PRODUCT =================
                1 -> {
                    AddProductScreen(productVM = productVM)
                }

                // ================= FAVORITES =================
                2 -> {
                    // ✅ Navigate to Favorites screen
                    LaunchedEffect(Unit) {
                        navController.navigate(Routes.FAVORITES)
                    }
                }

                // ================= ACCOUNT =================
                3 -> {
                    AccountScreen(
                        onBack = null,
                        profileViewModel = profileVM
                    )
                }
            }
        }
    }
}
