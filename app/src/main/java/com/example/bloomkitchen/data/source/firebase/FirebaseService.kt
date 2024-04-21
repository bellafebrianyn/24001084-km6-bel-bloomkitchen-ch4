package com.example.bloomkitchen.data.source.firebase

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await

interface FirebaseService {
    @Throws(exceptionClasses = [java.lang.Exception::class])
    suspend fun doLogin(email: String, password: String): Boolean

    @Throws(exceptionClasses = [Exception::class])
    suspend fun doRegister(fullName: String, email: String, password: String): Boolean
    suspend fun updateProfile(fullName: String? = null): Boolean
    suspend fun updatePassword(newPassword: String): Boolean
    suspend fun updateEmail(newEmail: String): Boolean
    suspend fun forgetPassword(email: String): Boolean
    fun requestChangePasswordByEmail(): Boolean
    fun doLogout(): Boolean
    fun isLoggedIn(): Boolean
    fun getCurrentUser(): FirebaseUser?
}

class FirebaseServiceImpl() : FirebaseService {

    private val firebaseAuth = FirebaseAuth.getInstance()
    override suspend fun doLogin(email: String, password: String): Boolean {
        val loginResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
        return loginResult.user != null
    }

    override suspend fun doRegister(
        fullName: String,
        email: String,
        password: String
    ): Boolean {
        val registerResult =  firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        registerResult.user?.updateProfile(
            userProfileChangeRequest {
                displayName = fullName
            }
        )?.await()
        return registerResult.user != null
    }

    override suspend fun updateProfile(fullName: String?): Boolean {
        getCurrentUser()?.updateProfile(
            userProfileChangeRequest {
                fullName?.let { displayName = fullName }
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

    override suspend fun forgetPassword(email: String): Boolean {
        email.let {
            firebaseAuth.sendPasswordResetEmail(it)
        }
        return true
    }

    override fun requestChangePasswordByEmail(): Boolean {
        getCurrentUser()?.email?.let {
            firebaseAuth.sendPasswordResetEmail(it)
        }
        return true
    }

    override fun doLogout(): Boolean {
        Firebase.auth.signOut()
        return true
    }

    override fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

}