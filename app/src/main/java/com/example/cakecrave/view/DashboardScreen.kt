package com.example.cakecrave.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
    productViewModel: ProductViewModel,      // ✅ SHARED FROM AppNavGraph
    favoritesViewModel: FavoritesViewModel
) {

    // 🔐 AUTH GUARD (KEEP)
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

    // ✅ PROFILE VM (LOCAL IS OK)
    val profileVM: ProfileViewModel = viewModel()

    // 🔄 LOAD PROFILE ONCE
    LaunchedEffect(Unit) {
        profileVM.loadProfile()
    }

    var selectedTab by remember { mutableStateOf(0) }

    // 🔔 PRODUCT MESSAGE HANDLING (KEEP)
    val message by productViewModel.message.collectAsState()
    LaunchedEffect(message) {
        if (message.contains("success", ignoreCase = true)) {
            selectedTab = 0
            productViewModel.clearMessage()
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

                        // ✅ PROFILE HEADER BACK (PHOTO + NAME)
                        DashboardHeader(profileVM = profileVM)

                        // ✅ HOME CONTENT WITH NAV buttons
                        HomeContent(
                            navController = navController,
                            productVM = productViewModel,
                            favoritesViewModel = favoritesViewModel,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }
                }

                // ================= ADD PRODUCT to the list  =================
                1 -> {
                    AddProductScreen(productVM = productViewModel)
                }

                // ================= FAVORITES =================
                2 -> {
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
