package com.example.bloomkitchen.data.source.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

interface FirebaseService {
    @Throws(exceptionClasses = [Exception::class])
    suspend fun doRegister(username: String, password: String, email: String, phoneNumber: String): Boolean
    suspend fun updateProfile(username: String? = null): Boolean
    suspend fun updatePassword(newPassword: String): Boolean
    suspend fun updateEmail(newEmail: String): Boolean
    fun getCurrentUser(): FirebaseUser?
}

class FirebaseServiceImpl() : FirebaseService {

    private val firebaseAuth = FirebaseAuth.getInstance()
    override suspend fun doRegister(
        username: String,
        password: String,
        email: String,
        phoneNumber: String
    ): Boolean {
        val registerResult =  firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        registerResult.user?.updateProfile(
            userProfileChangeRequest {
                displayName = username
            }
        )?.await()
        return registerResult.user != null
    }

    override suspend fun updateProfile(username: String?): Boolean {
        getCurrentUser()?.updateProfile(
            userProfileChangeRequest {
                username?.let { displayName = username }
            }
        )?.await()
        return true
    }

    override suspend fun updatePassword(newPassword: String): Boolean {
        getCurrentUser()?.updatePassword(newPassword)?.await()
        return true
    }

    override suspend fun updateEmail(newEmail: String): Boolean {
        getCurrentUser()?.verifyBeforeUpdateEmail(newEmail)?.await()
        return true
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

}