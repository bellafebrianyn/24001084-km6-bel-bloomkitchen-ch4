package com.example.bloomkitchen.presentation.splashscreen

import androidx.lifecycle.ViewModel
import com.example.bloomkitchen.data.repository.UserRepository

class SplashViewModel(private val repository: UserRepository) : ViewModel() {
    fun isUserLoggedIn() = repository.isLoggedIn()
}
