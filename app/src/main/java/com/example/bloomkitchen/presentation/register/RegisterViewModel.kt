package com.example.bloomkitchen.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.bloomkitchen.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun doRegister(fullName: String, email: String, password: String) =
        repository
            .doRegister(fullName, email, password)
            .asLiveData(Dispatchers.IO)
}