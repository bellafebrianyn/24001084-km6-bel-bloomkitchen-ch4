package com.example.bloomkitchen.data.repository

import com.example.bloomkitchen.data.datasouce.userpreference.UserPreferenceDataSource

interface UserPreferenceRepository {
    fun isUsingGridMode(): Boolean
    fun setUsingGridMode(isUsingGridMode: Boolean)
}

class UserPreferenceRepositoryImpl(private val dataSource: UserPreferenceDataSource) : UserPreferenceRepository {
    override fun isUsingGridMode(): Boolean {
        return dataSource.isUsingGridMode()
    }

    override fun setUsingGridMode(isUsingGridMode: Boolean) {
        return dataSource.setUsingGridMode(isUsingGridMode)
    }

}