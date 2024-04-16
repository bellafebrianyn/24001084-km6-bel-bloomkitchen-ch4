package com.example.bloomkitchen.data.datasouce.authentication

import com.example.bloomkitchen.data.model.User
import com.example.bloomkitchen.data.model.toUser
import com.example.bloomkitchen.data.source.firebase.FirebaseService
import java.lang.Exception

interface AuthDataSource {
    @Throws(exceptionClasses = [Exception::class])
    suspend fun doRegister(username: String, password: String, email: String, phoneNumber: String): Boolean
    suspend fun updateProfile(username: String? = null): Boolean
    suspend fun updatePassword(newPassword: String): Boolean
    suspend fun updateEmail(newEmail: String): Boolean
    fun getCurrentUser(): User?
}

class FirebaseAuthDataSource(private val service: FirebaseService): AuthDataSource {
    override suspend fun doRegister(
        username: String,
        password: String,
        email: String,
        phoneNumber: String
    ): Boolean {
        return service.doRegister(username, password, email, phoneNumber)
    }

    override suspend fun updateProfile(username: String?): Boolean {
        return service.updateProfile(username)
    }

    override suspend fun updatePassword(newPassword: String): Boolean {
        return service.updatePassword(newPassword)
    }

    override suspend fun updateEmail(newEmail: String): Boolean {
        return service.updateEmail(newEmail)
    }

    override fun getCurrentUser(): User? {
        return service.getCurrentUser().toUser()
    }

}

