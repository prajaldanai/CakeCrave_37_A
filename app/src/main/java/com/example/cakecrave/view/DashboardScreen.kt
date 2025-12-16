package com.example.cakecrave.view

// ================= IMPORTS =================
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cakecrave.viewmodel.ProfileViewModel
import com.example.cakecrave.viewmodel.ProductViewModel

// ================= SCREEN =================
@Composable
fun DashboardScreen() {

    // 🔥 ViewModels (shared inside Dashboard)
    val profileVM: ProfileViewModel = viewModel()
    val productVM: ProductViewModel = viewModel()

    val profile = profileVM.profile
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (selectedTab) {

                // ================= HOME =================
                0 -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        DashboardHeader(
                            userName = profile.name.ifBlank { "User" },
                            photoUrl = profile.photoUrl
                        )

                        // ✅ FIX: PASS productVM
                        HomeContent(
                            productVM = productVM,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // ================= ADD PRODUCT =================
                1 -> {
                    AddProductScreen(
                        productVM = productVM
                    )
                }

                // ================= FAVORITES =================
                2 -> {
                    PlaceholderScreen("Favorite Screen")
                }

                // ================= ACCOUNT =================
                3 -> {
                    AccountScreen(
                        profileViewModel = profileVM
                    )
                }
            }
        }
    }
}
