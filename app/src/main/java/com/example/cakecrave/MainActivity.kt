package com.example.cakecrave

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.cakecrave.navigation.AppNavGraph
import com.example.cakecrave.ui.theme.CakeCraveTheme
import com.example.cakecrave.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CakeCraveTheme {
                val navController = rememberNavController()

                // ✅ Proper ViewModel creation
                val authViewModel: AuthViewModel = viewModel()

                // ✅ Pass BOTH required params
                AppNavGraph(
                    navController = navController,
                    authViewModel = authViewModel
                )
            }
        }
    }
}
