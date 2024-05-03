package com.example.bloomkitchen.data.datasouce.userpreference

import com.example.bloomkitchen.data.source.local.pref.UserPreference

interface UserPreferenceDataSource {
    fun isUsingGridMode(): Boolean

    fun setUsingGridMode(isUsingGridMode: Boolean)
}

class UserPreferenceDataSourceImpl(private val userPreference: UserPreference) : UserPreferenceDataSource {
    override fun isUsingGridMode(): Boolean {
        return userPreference.isUsingGridMode()
    }

    override fun setUsingGridMode(isUsingGridMode: Boolean) {
        return userPreference.setUsingGridMode(isUsingGridMode)
    }
}
