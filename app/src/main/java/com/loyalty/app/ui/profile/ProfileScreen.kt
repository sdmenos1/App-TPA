package com.loyalty.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.loyalty.app.ui.auth.AuthViewModel
import com.loyalty.app.ui.home.HomeViewModel
import com.loyalty.app.ui.theme.*

/**
 * Pantalla de Perfil del usuario.
 * Permite visualizar datos, editarlos y cerrar sesión.
 */
@Composable
fun ProfileScreen(
    homeViewModel: HomeViewModel = viewModel(), // Gestor de datos del usuario (nombre, puntos, etc.)
    authViewModel: AuthViewModel = viewModel(), // Gestor de autenticación (cerrar sesión)
    onLogout: () -> Unit = {} // Acción que se ejecuta al cerrar sesión con éxito
) {
    // Obtenemos los datos del usuario desde el ViewModel
    val userState by homeViewModel.userState.collectAsState()
    
    // Variable de estado para controlar si estamos en modo "lectura" o "edición"
    var isEditing by remember { mutableStateOf(false) }
    
    // Estados temporales para guardar lo que el usuario escribe antes de darle a "Guardar"
    var name by remember(userState) { mutableStateOf(userState?.full_name ?: "") }
    var dni by remember(userState) { mutableStateOf(userState?.dni ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título de la pantalla
        Text(
            text = "Mi Perfil",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        // Foto de perfil o Avatar (Círculo con icono de persona)
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Zinc900),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Amber400,
                modifier = Modifier.size(60.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Si el usuario pulsó editar, mostramos cuadros de texto (Inputs)
        if (isEditing) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Amber400,
                    unfocusedBorderColor = Zinc800
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = dni,
                onValueChange = { dni = it },
                label = { Text("DNI") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Amber400,
                    unfocusedBorderColor = Zinc800
                )
            )
        } else {
            // Si no estamos editando, mostramos la información en tarjetas fijas
            InfoCard(label = "Nombre", value = userState?.full_name ?: "Cargando...")
            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(label = "DNI", value = userState?.dni ?: "No registrado")
            Spacer(modifier = Modifier.height(12.dp))
            InfoCard(label = "Email", value = userState?.email ?: "Cargando...")
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para alternar entre Editar y Guardar
        Button(
            onClick = {
                if (isEditing) {
                    // Si estábamos editando, al pulsar el botón guardamos los cambios en la base de datos
                    homeViewModel.updateProfile(name, dni)
                }
                isEditing = !isEditing // Cambiamos el modo (de editar a ver, o viceversa)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isEditing) SuccessGreen else Amber400,
                contentColor = Color.Black
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = if (isEditing) "GUARDAR CAMBIOS" else "EDITAR PERFIL",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de cerrar sesión hacia abajo

        // Botón para salir de la aplicación
        TextButton(
            onClick = {
                authViewModel.signOut() // Cierra la sesión en Supabase
                onLogout() // Ejecuta la navegación hacia la pantalla de Login
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "CERRAR SESIÓN",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

/**
 * Componente visual para mostrar un dato del perfil (ej. Nombre) de forma elegante.
 */
@Composable
fun InfoCard(label: String, value: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Zinc900,
        shape = MaterialTheme.shapes.medium,
        border = androidx.compose.foundation.BorderStroke(1.dp, Zinc800)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = label, color = Zinc500, fontSize = 12.sp) // Etiqueta pequeña (ej. "DNI")
            Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium) // Valor (ej. "12345678X")
        }
    }
}
