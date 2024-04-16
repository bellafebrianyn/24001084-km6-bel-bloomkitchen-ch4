package com.example.bloomkitchen.data.repository

import com.example.bloomkitchen.data.datasouce.authentication.AuthDataSource
import com.example.bloomkitchen.data.model.User
import com.example.bloomkitchen.utils.ResultWrapper
import com.example.bloomkitchen.utils.proceedFlow
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

interface UserRepository {
    @Throws(exceptionClasses = [Exception::class])
    fun doRegister(
        username: String,
        password: String,
        email: String,
        phoneNumber: String
    ): Flow<ResultWrapper<Boolean>>

    fun updateProfile(username: String): Flow<ResultWrapper<Boolean>>
    fun updatePassword(newPassword: String): Flow<ResultWrapper<Boolean>>
    fun updateEmail(newEmail: String): Flow<ResultWrapper<Boolean>>
    fun getCurrentUser(): User?
}

class UserRepositoryImpl(private val dataSource: AuthDataSource) : UserRepository {
    override fun doRegister(
        username: String,
        password: String,
        email: String,
        phoneNumber: String
    ): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.doRegister(username, password, email, phoneNumber) }
    }

    override fun updateProfile(username: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateProfile(username = username) }
    }

    override fun updatePassword(newPassword: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updatePassword(newPassword) }
    }

    override fun updateEmail(newEmail: String): Flow<ResultWrapper<Boolean>> {
        return proceedFlow { dataSource.updateEmail(newEmail) }
    }

    override fun getCurrentUser(): User? {
        return dataSource.getCurrentUser()
    }

}