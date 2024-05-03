package com.example.bloomkitchen.presentation.main

import androidx.lifecycle.ViewModel
import com.example.bloomkitchen.data.repository.UserRepository

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun isUserLoggedIn() = userRepository.isLoggedIn()
}
