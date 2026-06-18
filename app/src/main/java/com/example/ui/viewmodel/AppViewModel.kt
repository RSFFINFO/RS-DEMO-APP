package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.database.AppDatabase
import com.example.data.model.User
import com.example.data.model.WebApp
import com.example.data.repository.AppRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AppRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = AppRepository(database.userDao(), database.webAppDao())
    }

    // --- Authentication State ---
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _regSuccess = MutableStateFlow<Boolean>(false)
    val regSuccess: StateFlow<Boolean> = _regSuccess.asStateFlow()

    // --- WebApp List State (reactive flow mapped to logged-in user) ---
    val webApps: StateFlow<List<WebApp>> = _currentUser
        .flatMapLatest { user ->
            if (user != null) {
                repository.getAppsForUser(user.username)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // --- Active Opened WebApp View ---
    private val _selectedWebApp = MutableStateFlow<WebApp?>(null)
    val selectedWebApp: StateFlow<WebApp?> = _selectedWebApp.asStateFlow()

    // --- Navigation Tabs Footer state ---
    private val _currentTab = MutableStateFlow("home")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    // --- Clear Auth Errors ---
    fun clearErrors() {
        _authError.value = null
        _regSuccess.value = false
    }

    // --- Register Logic ---
    fun register(username: String, name: String, passwordHash: String) {
        viewModelScope.launch {
            if (username.isBlank() || name.isBlank() || passwordHash.isBlank()) {
                _authError.value = "মোবাইল/ইউজারনেম, নাম এবং পাসওয়ার্ড খালি রাখা যাবে না।"
                return@launch
            }
            val newUser = User(
                username = username.trim(),
                fullName = name.trim(),
                passwordHash = passwordHash
            )
            val success = repository.registerUser(newUser)
            if (success) {
                _regSuccess.value = true
                _authError.value = null
            } else {
                _authError.value = "এই ইউজারনেম দিয়ে ইতিমধ্যেই রেজিস্ট্রেশন করা আছে।"
            }
        }
    }

    // --- Login Logic ---
    fun login(username: String, passwordHash: String) {
        viewModelScope.launch {
            if (username.isBlank() || passwordHash.isBlank()) {
                _authError.value = "ইউজারনেম এবং পাসওয়ার্ড প্রদান করুন।"
                return@launch
            }
            val user = repository.getUserByUsername(username.trim())
            if (user != null && user.passwordHash == passwordHash) {
                _currentUser.value = user
                _authError.value = null
                _currentTab.value = "home"
            } else {
                _authError.value = "ভুল ইউজারনেম অথবা পাসওয়ার্ড!"
            }
        }
    }

    // --- Logout ---
    fun logout() {
        _currentUser.value = null
        _selectedWebApp.value = null
        _currentTab.value = "home"
        clearErrors()
    }

    // --- Create Custom App ---
    fun createWebApp(name: String, url: String, iconName: String, iconColor: String, packageName: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            // Basic sanitization
            var formattedUrl = url.trim()
            if (!formattedUrl.startsWith("http://") && !formattedUrl.startsWith("https://")) {
                formattedUrl = "https://$formattedUrl"
            }
            
            val cleanPackage = if (packageName.isBlank()) {
                "com.webtoapp.${name.lowercase().replace("\\s".toRegex(), "")}"
            } else {
                packageName.trim()
            }

            val app = WebApp(
                name = name.trim(),
                url = formattedUrl,
                iconName = iconName,
                iconColor = iconColor,
                packageName = cleanPackage,
                creatorUsername = user.username
            )
            repository.insertApp(app)
        }
    }

    // --- Delete App ---
    fun deleteWebApp(app: WebApp) {
        viewModelScope.launch {
            repository.deleteApp(app)
            if (_selectedWebApp.value?.id == app.id) {
                _selectedWebApp.value = null
            }
        }
    }

    // --- Open / Select App ---
    fun selectWebApp(app: WebApp?) {
        _selectedWebApp.value = app
    }

    // --- Update Footer Tab ---
    fun setTab(tab: String) {
        _currentTab.value = tab
    }
}
