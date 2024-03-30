package com.example.bloomkitchen.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bloomkitchen.data.datasource.ProfileDataSource
import com.example.bloomkitchen.data.datasource.ProfileDataSourceImpl
import com.example.bloomkitchen.data.model.Profile

class ProfileViewModel: ViewModel() {
    private val profileDataSource: ProfileDataSource by lazy {
        ProfileDataSourceImpl()
    }
    private val _profileData = MutableLiveData<Profile>()
    val profileData: LiveData<Profile>
        get() = _profileData

    val editProfile = MutableLiveData(false)

    fun getProfileData() {
        val profiles = profileDataSource.getProfileData()
        if (profiles.isNotEmpty()) {
            _profileData.value = profiles[0]
        }
    }

    fun changeEditMode() {
        val currentProfileData = editProfile.value ?: false
        editProfile.postValue(!currentProfileData)
    }
}
