package com.example.bloomkitchen.data.repository

import app.cash.turbine.test
import com.example.bloomkitchen.data.datasouce.authentication.AuthDataSource
import com.example.bloomkitchen.data.model.User
import com.example.bloomkitchen.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import okio.IOException
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserRepositoryImplTest {
    @MockK
    lateinit var dataSource: AuthDataSource
    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = UserRepositoryImpl(dataSource)
    }

    @Test
    fun doLogin_success() {
        val email = "bella@@gmail.com"
        val password = "bella32323"
        coEvery { dataSource.doLogin(any(), any()) } returns true
        runTest {
            repository.doLogin(email, password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.doLogin(any(), any()) }
            }
        }
    }

    @Test
    fun doLogin_loading() {
        val email = "bella@@gmail.com"
        val password = "bella32323"
        coEvery { dataSource.doLogin(any(), any()) } returns true
        runTest {
            repository.doLogin(email, password).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.doLogin(any(), any()) }
            }
        }
    }

    @Test
    fun doLogin_error() {
        val email = "bella@@gmail.com"
        val password = "bella32323"
        coEvery { dataSource.doLogin(any(), any()) } throws IOException("Login Error")
        runTest {
            repository.doLogin(email, password).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.doLogin(any(), any()) }
            }
        }
    }

    @Test
    fun doRegister_success() {
        val email = "bella@@gmail.com"
        val fullName = "Bella Febriany N"
        val password = "bella32323"
        coEvery { dataSource.doRegister(any(), any(), any()) } returns true
        runTest {
            repository.doRegister(fullName, email, password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.doRegister(any(), any(), any()) }
            }
        }
    }

    @Test
    fun doRegister_loading() {
        val email = "bella@@gmail.com"
        val fullName = "Bella Febriany N"
        val password = "bella32323"
        coEvery { dataSource.doRegister(any(), any(), any()) } returns true
        runTest {
            repository.doRegister(fullName, email, password).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.doRegister(any(), any(), any()) }
            }
        }
    }

    @Test
    fun doRegister_error() {
        val email = "bella@@gmail.com"
        val fullName = "Bella Febriany N"
        val password = "bella32323"
        coEvery { dataSource.doRegister(any(), any(), any()) } throws IOException("Register Error")
        runTest {
            repository.doRegister(fullName, email, password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.doRegister(any(), any(), any()) }
            }
        }
    }

    @Test
    fun updateProfile_success() {
        val fullName = "Bella Febriany "

        coEvery { dataSource.updateProfile(any()) } returns true
        runTest {
            repository.updateProfile(fullName).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.updateProfile(any()) }
            }
        }
    }

    @Test
    fun updateProfile_loading() {
        val fullName = "Bella Febriany "

        coEvery { dataSource.updateProfile(any()) } returns true
        runTest {
            repository.updateProfile(fullName).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.updateProfile(any()) }
            }
        }
    }

    @Test
    fun updateProfile_error() {
        val fullName = "Bella Febriany "

        coEvery { dataSource.updateProfile(any()) } throws IOException("Update Profile Error")
        runTest {
            repository.updateProfile(fullName).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.updateProfile(any()) }
            }
        }
    }

    @Test
    fun updatePassword_success() {
        val password = "bellafn3232"

        coEvery { dataSource.updatePassword(any()) } returns true
        runTest {
            repository.updatePassword(password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.updatePassword(any()) }
            }
        }
    }

    @Test
    fun updatePassword_loading() {
        val password = "bellafn3232"

        coEvery { dataSource.updatePassword(any()) } returns true
        runTest {
            repository.updatePassword(password).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.updatePassword(any()) }
            }
        }
    }

    @Test
    fun updatePassword_error() {
        val password = "bellafn3232"

        coEvery { dataSource.updatePassword(any()) } throws IOException("Update Password Error")
        runTest {
            repository.updatePassword(password).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.updatePassword(any()) }
            }
        }
    }

    @Test
    fun updateEmail_success() {
        val email = "bella@gmail.com"

        coEvery { dataSource.updateEmail(any()) } returns true
        runTest {
            repository.updateEmail(email).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.updateEmail(any()) }
            }
        }
    }

    @Test
    fun updateEmail_loading() {
        val email = "bella@gmail.com"

        coEvery { dataSource.updateEmail(any()) } returns true
        runTest {
            repository.updateEmail(email).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.updateEmail(any()) }
            }
        }
    }

    @Test
    fun updateEmail_error() {
        val email = "bella@gmail.com"

        coEvery { dataSource.updateEmail(any()) } throws IOException("Update Email Error")
        runTest {
            repository.updateEmail(email).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.updateEmail(any()) }
            }
        }
    }

    @Test
    fun forgetPassword_success() {
        val email = "bella@gmail.com"

        coEvery { dataSource.forgetPassword(any()) } returns true
        runTest {
            repository.forgetPassword(email).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                assertEquals(true, actualData.payload)
                coVerify { dataSource.forgetPassword(any()) }
            }
        }
    }

    @Test
    fun forgetPassword_loading() {
        val email = "bella@gmail.com"

        coEvery { dataSource.forgetPassword(any()) } returns true
        runTest {
            repository.forgetPassword(email).map {
                delay(100)
                it
            }.test {
                delay(111)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Loading)
                coVerify { dataSource.forgetPassword(any()) }
            }
        }
    }

    @Test
    fun forgetPassword_error() {
        val email = "bella@gmail.com"

        coEvery { dataSource.forgetPassword(any()) } throws IOException("Change Password Error")
        runTest {
            repository.forgetPassword(email).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                coVerify { dataSource.forgetPassword(any()) }
            }
        }
    }

    @Test
    fun requestChangePasswordByEmail() {
        runTest {
            every { dataSource.requestChangePasswordByEmail() } returns true
            val actualData = repository.requestChangePasswordByEmail()
            verify { dataSource.requestChangePasswordByEmail() }
            assertEquals(true, actualData)
        }
    }

    @Test
    fun doLogout() {
        runTest {
            every { dataSource.doLogout() } returns true
            val actualData = repository.doLogout()
            verify { dataSource.doLogout() }
            assertEquals(true, actualData)
        }
    }

    @Test
    fun isLoggedIn() {
        runTest {
            every { dataSource.isLoggedIn() } returns true
            val actualData = repository.isLoggedIn()
            verify { dataSource.isLoggedIn() }
            assertEquals(true, actualData)
        }
    }

    @Test
    fun getCurrentUser() {
        runTest {
            val currentUser = mockk<User>()
            every { currentUser.id } returns "1"
            every { currentUser.fullName } returns "Bella Febriany N"
            every { currentUser.email } returns "bella@gmail.com"
            every { dataSource.getCurrentUser() } returns currentUser

            val result = dataSource.getCurrentUser()
            assertEquals("1", result?.id)
            assertEquals("Bella Febriany N", result?.fullName)
            assertEquals("bella@gmail.com", result?.email)
            verify { repository.getCurrentUser() }
        }
    }
}
