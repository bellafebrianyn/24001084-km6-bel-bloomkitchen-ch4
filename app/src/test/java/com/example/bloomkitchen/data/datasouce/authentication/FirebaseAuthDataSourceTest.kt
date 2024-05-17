package com.example.bloomkitchen.data.datasouce.authentication

import com.example.bloomkitchen.data.source.firebase.FirebaseService
import com.google.firebase.auth.FirebaseUser
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FirebaseAuthDataSourceTest {
    @MockK
    lateinit var service: FirebaseService
    private lateinit var dataSource: AuthDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = FirebaseAuthDataSource(service)
    }

    @Test
    fun doLogin() {
        runTest {
            coEvery { dataSource.doLogin(any(), any()) } returns true
            val actualResult = service.doLogin("bella@gmail.com", "bellafn12345")
            coVerify { dataSource.doLogin(any(), any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun doRegister() {
        runTest {
            coEvery { dataSource.doRegister(any(), any(), any()) } returns true
            val actualResult = service.doRegister("Bella Febriany Nawangsari", "bella@gmail.com", "bellafn12345")
            coVerify { dataSource.doRegister(any(), any(), any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun updateProfile() {
        runTest {
            coEvery { dataSource.updateProfile(any()) } returns true
            val actualResult = service.updateProfile("Bella Febriany N")
            coVerify { dataSource.updateProfile(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun updatePassword() {
        runTest {
            coEvery { dataSource.updatePassword(any()) } returns true
            val actualResult = service.updatePassword("bellafn")
            coVerify { dataSource.updatePassword(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun updateEmail() {
        runTest {
            coEvery { dataSource.updateEmail(any()) } returns true
            val actualResult = service.updateEmail("bella@gmail.com")
            coVerify { dataSource.updateEmail(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun forgetPassword() {
        runTest {
            coEvery { dataSource.forgetPassword(any()) } returns true
            val actualResult = service.forgetPassword("bella@gmail.com")
            coVerify { dataSource.forgetPassword(any()) }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun requestChangePasswordByEmail() {
        runTest {
            every { dataSource.requestChangePasswordByEmail() } returns true
            val actualResult = service.requestChangePasswordByEmail()
            verify { dataSource.requestChangePasswordByEmail() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun doLogout() {
        runTest {
            every { dataSource.doLogout() } returns true
            val actualResult = service.doLogout()
            verify { dataSource.doLogout() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun isLoggedIn() {
        runTest {
            every { dataSource.isLoggedIn() } returns true
            val actualResult = service.isLoggedIn()
            verify { dataSource.isLoggedIn() }
            assertEquals(true, actualResult)
        }
    }

    @Test
    fun getCurrentUser() {
        runTest {
            val firebaseUser = mockk<FirebaseUser>()
            every { firebaseUser.uid } returns "1"
            every { firebaseUser.displayName } returns "Bella Febriany N"
            every { firebaseUser.email } returns "bella@gmail.com"
            every { service.getCurrentUser() } returns firebaseUser

            val result = dataSource.getCurrentUser()
            assertEquals("1", result?.id)
            assertEquals("Bella Febriany N", result?.fullName)
            assertEquals("bella@gmail.com", result?.email)
            verify { service.getCurrentUser() }
        }
    }
}
