package com.example.bloomkitchen.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.bloomkitchen.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {
    val editProfile = MutableLiveData(false)

    fun changeEditMode() {
        val currentProfileData = editProfile.value ?: false
        editProfile.postValue(!currentProfileData)
    }

    fun updateProfile(updatedFullName: String? = null) =
        userRepository
            .updateProfile(updatedFullName)
            .asLiveData(Dispatchers.IO)

    fun reqChangePasswordByEmail() = userRepository.requestChangePasswordByEmail()

    fun isUserLoggedOut() = userRepository.doLogout()

    fun getCurrentUser() = userRepository.getCurrentUser()
}
