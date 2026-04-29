package com.loyalty.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.loyalty.app.ui.auth.LoginScreen
import com.loyalty.app.ui.main.MainScreen
import com.loyalty.app.ui.theme.AppFidelizacionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppFidelizacionTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("main") {
                        MainScreen()
                    }
                }
            }
        }
    }
}
