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

class AuthViewModel : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                SupabaseClient.client.auth.signInWith(Email) {
                    this.email = email
                    this.password = password
                }
                _isAuthenticated.value = true
            } catch (e: Throwable) {
                val eMsg = e.cause?.toString() ?: e.toString()
                _error.value = eMsg
            } finally {
                _loading.value = false
            }
        }
    }

    fun signUp(email: String, password: String, fullName: String, dni: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try {
                SupabaseClient.client.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }
                
                val user = SupabaseClient.client.auth.currentUserOrNull()
                user?.let {
                    // Create profile in profiles table
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
    
    fun clearError() {
        _error.value = null
    }
}
