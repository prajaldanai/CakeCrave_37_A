package com.example.cakecrave

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.cakecrave.navigation.AppNavGraph
import com.example.cakecrave.ui.theme.CakeCraveTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        Log.d("DEBUG_FLOW", " MainActivity onCreate")


        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            Log.e("CakeCraveCrash", " Uncaught exception", throwable)
        }

        setContent {
            Log.d("DEBUG_FLOW", " setContent started")

            CakeCraveTheme {

                Log.d("DEBUG_FLOW", " Theme applied")

                val navController = rememberNavController()
                Log.d("DEBUG_FLOW", " NavController created")

                AppNavGraph(navController = navController)

                Log.d("DEBUG_FLOW", " AppNavGraph rendered")
            }

        }
    }
}
