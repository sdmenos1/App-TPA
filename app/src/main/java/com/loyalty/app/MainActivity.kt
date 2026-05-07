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

/**
 * Actividad principal de la aplicación.
 * Es el punto de entrada que configura el tema y la navegación base.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aplicamos el tema personalizado de la aplicación
            AppFidelizacionTheme {
                // El NavController gestiona el cambio entre pantallas
                val navController = rememberNavController()

                // Definimos el grafo de navegación (rutas disponibles)
                NavHost(navController = navController, startDestination = "login") {
                    
                    // Pantalla de Inicio de Sesión
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                // Al iniciar sesión con éxito, vamos a la pantalla principal
                                // 'popUpTo' con 'inclusive = true' elimina el login del historial
                                navController.navigate("main") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    
                    // Pantalla Principal (Contiene las pestañas: Home, Premios, Perfil)
                    composable("main") {
                        MainScreen(
                            onLogout = {
                                // Al cerrar sesión, volvemos al login y limpiamos el historial
                                navController.navigate("login") {
                                    popUpTo("main") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
