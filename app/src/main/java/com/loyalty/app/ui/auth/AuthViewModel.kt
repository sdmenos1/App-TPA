package com.loyalty.app.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loyalty.app.network.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel encargado de la lógica de autenticación (Login, Registro, Cerrar Sesión).
 * Se comunica con Supabase para gestionar los usuarios.
 */
class AuthViewModel : ViewModel() {
    // Estado que indica si hay una operación en curso (cargando)
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    // Estado para almacenar mensajes de error y mostrarlos en la UI
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Estado que indica si el usuario está autenticado
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    /**
     * Función para iniciar sesión con email y contraseña.
     */
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                // Llamada a Supabase para autenticar
                SupabaseClient.client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                _isAuthenticated.value = true // Login exitoso
            } catch (e: Throwable) {
                // Si hay error (ej. contraseña incorrecta), lo guardamos
                val eMsg = e.cause?.toString() ?: e.toString()
                _error.value = eMsg
            } finally {
                _loading.value = false
            }
        }
    }

    /**
     * Función para registrar un nuevo usuario y crear su perfil en la base de datos.
     */
    fun signUp(email: String, password: String, fullName: String, dni: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                // 1. Registro en el sistema de Autenticación
                SupabaseClient.client.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                
                val user = SupabaseClient.client.auth.currentUserOrNull()
                user?.let {
                    // 2. Si se registró bien, creamos su fila en la tabla de base de datos 'profiles'
                    val profile = mapOf(
                        "id" to it.id,
                        "full_name" to fullName,
                        "dni" to dni,
                        "email" to email
                    )
                    SupabaseClient.client.postgrest["profiles"].insert(profile)
                }
                
                _error.value = "Registro completado. Por favor verifica tu email."
            } catch (e: Throwable) {
                val eMsg = e.cause?.toString() ?: e.toString()
                _error.value = eMsg
            } finally {
                _loading.value = false
            }
        }
    }
    
    /**
     * Limpia el mensaje de error de la pantalla.
     */
    fun clearError() {
        _error.value = null
    }

    /**
     * Cierra la sesión activa del usuario.
     */
    fun signOut() {
        viewModelScope.launch {
            try {
                SupabaseClient.client.auth.signOut()
                _isAuthenticated.value = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
