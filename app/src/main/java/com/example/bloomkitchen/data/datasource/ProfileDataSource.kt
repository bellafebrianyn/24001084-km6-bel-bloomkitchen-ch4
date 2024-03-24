package com.example.bloomkitchen.data.datasource

import com.example.bloomkitchen.data.model.Profile

interface ProfileDataSource {
    fun getProfileData(): List<Profile>
}

class ProfileDataSourceImpl() : ProfileDataSource {
    override fun getProfileData(): List<Profile> {
        return mutableListOf(
            Profile(
                image = "https://imgx.parapuan.co/crop/0x0:0x0/700x465/photo/2023/01/27/karinajpg-20230123020721jpg-20230127122335.jpg",
                username = "bellafebrianyn",
                email = "bellafebrianynws@gmail.com",
                phoneNumber = 62868986983323,
            )
        )
    }
}

