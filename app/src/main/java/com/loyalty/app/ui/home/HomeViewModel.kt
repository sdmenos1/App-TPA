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

@Serializable
data class UserData(
    val id: String,
    val full_name: String = "",
    val points: Int = 0,
    val stamps: Int = 0
)

class HomeViewModel : ViewModel() {
    private val _userState = MutableStateFlow<UserData?>(null)
    val userState: StateFlow<UserData?> = _userState

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        val currentUser = SupabaseClient.client.auth.currentUserOrNull()
        currentUser?.let {
            loadUserData(it.id)
        }
    }

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

    fun addStamp() {
        val current = _userState.value ?: return
        viewModelScope.launch {
            try {
                val newStamps = current.stamps + 1
                val newPoints = if (newStamps >= 10) current.points + 50 else current.points
                val finalStamps = if (newStamps >= 10) 0 else newStamps

                SupabaseClient.client.postgrest["profiles"].update(
                    mapOf(
                        "stamps" to finalStamps,
                        "points" to newPoints
                    )
                ) {
                    filter { eq("id", current.id) }
                }

                _userState.value = current.copy(stamps = finalStamps, points = newPoints)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
