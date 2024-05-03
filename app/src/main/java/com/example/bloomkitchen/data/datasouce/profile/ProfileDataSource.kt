package com.example.bloomkitchen.data.datasource

import com.example.bloomkitchen.R
import com.example.bloomkitchen.data.model.Profile

interface ProfileDataSource {
    fun getProfileData(): List<Profile>
}

class ProfileDataSourceImpl() : ProfileDataSource {
    override fun getProfileData(): List<Profile> {
        return mutableListOf(
            Profile(
                image = R.drawable.ic_profile,
                username = "bellafebrianyn",
                email = "bellafebrianynws@gmail.com",
            ),
        )
    }
}
