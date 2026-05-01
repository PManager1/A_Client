package com.example.birdy.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.birdy.data.HomeData
import com.example.birdy.data.HomeServiceCategory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * ViewModel for the Home screen.
 * Manages UI state including animated search placeholder, categories, and loading state.
 * Matches the iOS Home view's state management.
 */
class HomeViewModel : ViewModel() {

    // --- Animated search placeholder (matches iOS timer cycling) ---
    var currentServiceIndex by mutableIntStateOf(0)
        private set

    private var animationJob: Job? = null

    // --- User state ---
    var userFirstName by mutableStateOf("Guest")
        private set

    // --- Category state ---
    var userSelectedCategories by mutableStateOf<List<HomeServiceCategory>>(emptyList())
        private set

    var isLoadingCategories by mutableStateOf(true)
        private set

    init {
        startPlaceholderAnimation()
        loadCategories()
    }

    /**
     * Starts cycling through placeholder service names every 2 seconds.
     * Matches iOS: Timer.scheduledTimer(withTimeInterval: 2.0, repeats: true)
     */
    private fun startPlaceholderAnimation() {
        animationJob = viewModelScope.launch {
            while (true) {
                delay(2000)
                currentServiceIndex = (currentServiceIndex + 1) % HomeData.placeholderServices.size
            }
        }
    }

    /**
     * Loads categories - either from backend or fallback.
     * Matches iOS: fetchUserSelectedServices() + loadFallbackCategories()
     */
    private fun loadCategories() {
        viewModelScope.launch {
            try {
                // TODO: Implement backend fetch with OkHttp/Retrofit
                // For now, load fallback categories immediately (no artificial delay)
                loadFallbackCategories()
            } catch (e: Exception) {
                loadFallbackCategories()
            }
        }
    }

    /**
     * Fallback categories when backend is unavailable.
     * Matches iOS: loadFallbackCategories() — loads Electrical & Plumbing
     */
    private fun loadFallbackCategories() {
        userSelectedCategories = HomeData.fallbackCategories
        isLoadingCategories = false
    }

    /**
     * Update user name (to be called after auth/profile fetch).
     * Matches iOS: authManager.fetchUserProfile()
     */
    fun updateUserProfile(firstName: String) {
        userFirstName = firstName
    }

    override fun onCleared() {
        super.onCleared()
        animationJob?.cancel()
    }
}