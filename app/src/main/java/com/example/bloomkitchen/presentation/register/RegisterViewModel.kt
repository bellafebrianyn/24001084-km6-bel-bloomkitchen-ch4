package com.example.bloomkitchen.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.bloomkitchen.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun doRegister(username: String, password: String, email: String, phoneNumber: String) =
        repository
            .doRegister(username, password, email, phoneNumber)
            .asLiveData(Dispatchers.IO)
}