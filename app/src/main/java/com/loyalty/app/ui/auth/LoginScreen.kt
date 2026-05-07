package com.loyalty.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.loyalty.app.ui.theme.*

/**
 * Pantalla de inicio de sesión y registro.
 * Permite a los usuarios entrar a su cuenta o crear una nueva.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit, // Función que se llama cuando el usuario entra correctamente
    viewModel: AuthViewModel = viewModel() // Gestor de la lógica de autenticación
) {
    // Variables de estado para guardar lo que el usuario escribe en los campos
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    
    // Controla si mostramos el formulario de Login o el de Registro
    var isRegistering by remember { mutableStateOf(false) }

    // Obtenemos los estados desde el ViewModel
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isAuthenticated by viewModel.isAuthenticated.collectAsState()

    // Si el usuario se autentica con éxito, disparamos la navegación
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated) {
            onLoginSuccess()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Zinc950 // Fondo oscuro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo circular con degradado y un candado
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Amber400, Amber600)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Título dinámico según el modo
            Text(
                text = if (isRegistering) "Crear Cuenta" else "Bienvenido",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 28.sp
            )

            Text(
                text = if (isRegistering) "Únete a nuestro programa de fidelidad" else "Gestiona tus sellos y recompensas",
                color = Zinc500,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Formulario de entrada de datos
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campos adicionales que solo se muestran si el usuario se está registrando
                if (isRegistering) {
                    CustomTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = "Nombre Completo",
                        icon = Icons.Default.Person
                    )
                    CustomTextField(
                        value = dni,
                        onValueChange = { dni = it },
                        placeholder = "DNI",
                        icon = Icons.Default.CreditCard,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                // Campos comunes: Email y Contraseña
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Correo electrónico",
                    icon = Icons.Default.Email,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Contraseña",
                    icon = Icons.Default.Lock,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Botón de Acción (Iniciar Sesión o Registrarse)
                Button(
                    onClick = {
                        if (isRegistering) {
                            viewModel.signUp(email, password, fullName, dni)
                        } else {
                            viewModel.signIn(email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Amber400, Amber600)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (loading) {
                            // Círculo de carga si el proceso está en curso
                            CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                        } else {
                            Text(
                                text = if (isRegistering) "Registrarse" else "Iniciar Sesión",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                // Botón para cambiar entre el modo Login y el modo Registro
                Text(
                    text = if (isRegistering) "¿Ya tienes cuenta? Inicia sesión" else "¿No tienes cuenta? Regístrate gratis",
                    color = Amber400,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable { isRegistering = !isRegistering }
                        .padding(vertical = 16.dp)
                )
            }

            // Si hay un error, lo mostramos en color rojo
            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

/**
 * Componente de campo de texto personalizado con icono y estilo moderno.
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isPassword: Boolean = false, // Si es true, oculta el texto (para contraseñas)
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Zinc500) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = Zinc500) },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(1.dp, Zinc800, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Zinc900,
            unfocusedContainerColor = Zinc900,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = Amber400,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        singleLine = true
    )
}
