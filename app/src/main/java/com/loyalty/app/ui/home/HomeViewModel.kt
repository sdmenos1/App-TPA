package com.loyalty.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loyalty.app.network.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/**
 * Clase de datos que representa la información del perfil del usuario en la base de datos.
 */
@Serializable
data class UserData(
    val id: String,
    val full_name: String = "",
    val email: String = "",
    val dni: String = "",
    val points: Int = 0,
    val stamps: Int = 0
)

/**
 * ViewModel que gestiona la lógica de la pantalla de Inicio y los datos del perfil.
 */
class HomeViewModel : ViewModel() {
    // Estado del usuario cargado desde Supabase
    private val _userState = MutableStateFlow<UserData?>(null)
    val userState: StateFlow<UserData?> = _userState

    // Indica si se están cargando datos
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        // Al crear el ViewModel, intentamos cargar los datos del usuario actual
        val currentUser = SupabaseClient.client.auth.currentUserOrNull()
        currentUser?.let {
            loadUserData(it.id)
        }
    }

    /**
     * Obtiene la información del perfil desde la tabla 'profiles' en Supabase.
     */
    fun loadUserData(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = SupabaseClient.client.postgrest["profiles"]
                    .select(columns = Columns.ALL) {
                        filter {
                            eq("id", userId)
                        }
                    }
                
                val data = response.decodeSingleOrNull<UserData>()
                _userState.value = data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Lógica para añadir un sello. 
     * Cada 10 sellos, se otorgan 50 puntos y se reinicia el contador.
     */
    fun addStamp() {
        val current = _userState.value ?: return
        viewModelScope.launch {
            try {
                val newStamps = current.stamps + 1
                // Si llega a 10 sellos, sumamos puntos y reiniciamos sellos a 0
                val newPoints = if (newStamps >= 10) current.points + 50 else current.points
                val finalStamps = if (newStamps >= 10) 0 else newStamps

                // Actualizamos en la base de datos remota
                SupabaseClient.client.postgrest["profiles"].update(
                    mapOf(
                        "stamps" to finalStamps,
                        "points" to newPoints
                    )
                ) {
                    filter { eq("id", current.id) }
                }

                // Actualizamos el estado local para que la UI se refresque instantáneamente
                _userState.value = current.copy(stamps = finalStamps, points = newPoints)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Actualiza los datos personales del usuario.
     */
    fun updateProfile(fullName: String, dni: String) {
        val current = _userState.value ?: return
        viewModelScope.launch {
            try {
                SupabaseClient.client.postgrest["profiles"].update(
                    mapOf(
                        "full_name" to fullName,
                        "dni" to dni
                    )
                ) {
                    filter { eq("id", current.id) }
                }
                // Refrescamos el estado local con los nuevos valores
                _userState.value = current.copy(full_name = fullName, dni = dni)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
