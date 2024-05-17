package com.example.bloomkitchen.presentation.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catnip.kokomputer.tools.MainCoroutineRule
import com.example.bloomkitchen.data.model.User
import com.example.bloomkitchen.data.repository.UserRepository
import com.example.bloomkitchen.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class ProfileViewModelTest {
    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @MockK
    lateinit var userRepository: UserRepository

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel =
            spyk(
                ProfileViewModel(userRepository),
            )
        viewModel.editProfile.value = false
    }

    @Test
    fun getEditProfile() {
        assertFalse(viewModel.editProfile.value ?: true)
    }

    @Test
    fun updateProfile() {
        every { userRepository.updateProfile(any()) } returns
            flow {
                emit(ResultWrapper.Success(true))
            }
        viewModel.updateProfile("bellafn07")
        verify { userRepository.updateProfile(any()) }
    }

    @Test
    fun reqChangePasswordByEmail() {
        every { userRepository.requestChangePasswordByEmail() } returns true
        val result = viewModel.reqChangePasswordByEmail()
        assertEquals(true, result)
        verify { userRepository.requestChangePasswordByEmail() }
    }

    @Test
    fun isUserLoggedOut() {
        every { userRepository.doLogout() } returns true
        val result = viewModel.isUserLoggedOut()
        assertEquals(true, result)
        verify { userRepository.doLogout() }
    }

    @Test
    fun getCurrentUser() {
        val currentUser = mockk<User>()
        every { userRepository.getCurrentUser() } returns currentUser
        val result = viewModel.getCurrentUser()
        assertEquals(currentUser, result)
        verify { userRepository.getCurrentUser() }
    }
}
