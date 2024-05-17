package com.example.bloomkitchen.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.catnip.kokomputer.tools.MainCoroutineRule
import com.catnip.kokomputer.tools.getOrAwaitValue
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.data.model.User
import com.example.bloomkitchen.data.repository.CartRepository
import com.example.bloomkitchen.data.repository.CategoryRepository
import com.example.bloomkitchen.data.repository.MenuRepository
import com.example.bloomkitchen.data.repository.UserPreferenceRepository
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

class HomeViewModelTest {
    @get:Rule
    val testRule: TestRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule: TestRule = MainCoroutineRule(UnconfinedTestDispatcher())

    @MockK
    lateinit var categoryRepository: CategoryRepository

    @MockK
    lateinit var menuRepository: MenuRepository

    @MockK
    lateinit var cartRepository: CartRepository

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var userPreferenceRepository: UserPreferenceRepository

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel =
            spyk(
                HomeViewModel(
                    categoryRepository,
                    menuRepository,
                    cartRepository,
                    userRepository,
                    userPreferenceRepository,
                ),
            )
    }

    @Test
    fun getMenu() {
        every { menuRepository.getMenu() } returns
            flow {
                emit(ResultWrapper.Success(listOf(mockk(), mockk())))
            }
        every { menuRepository.getMenu(any()) } returns
            flow {
                emit(ResultWrapper.Success(listOf(mockk(), mockk())))
            }
        val result = viewModel.getMenu().getOrAwaitValue()
        assertEquals(2, result.payload?.size)
        verify { menuRepository.getMenu(any()) }
    }

    @Test
    fun getCategories() {
        every { categoryRepository.getCategories() } returns
            flow {
                emit(ResultWrapper.Success(listOf(mockk(), mockk())))
            }
        val result = viewModel.getCategories().getOrAwaitValue()
        assertEquals(2, result.payload?.size)
        verify { categoryRepository.getCategories() }
    }

    @Test
    fun getCurrentUser() {
        val user1 =
            User(
                id = "1",
                fullName = "Bella Febriany Nawangsari",
                email = "bella@gmail.com",
            )

        every { userRepository.getCurrentUser() } returns user1

        val result = viewModel.getCurrentUser()
        assertEquals(user1, result)
        verify { userRepository.getCurrentUser() }
    }

    @Test
    fun userIsLoggedIn() {
        every { userRepository.isLoggedIn() } returns true
        val result = viewModel.userIsLoggedIn()
        assertEquals(true, result)
        verify { userRepository.isLoggedIn() }
    }

    @Test
    fun getCountLiveData() {
        val mockMenu =
            Menu(
                imageUrl = "exampleImgUrl",
                name = "Ayam Bakar",
                formatPrice = "Rp10.000",
                price = 10000.0,
                detail = "ayam bakar dibakar dengan arang",
                locationUrl = "exampleLocationUrl",
                restoAddress = "example",
            )
        assert(viewModel.menuCountLiveData.value == 0)
        viewModel.addItemToCart(mockMenu)
        assert(viewModel.menuCountLiveData.value == 1)
    }

    @Test
    fun addItemToCart_success() {
        val menu1 =
            Menu(
                imageUrl = "http://nasigoreng.jpg",
                name = "Nasi Goreng",
                formatPrice = "15.000",
                price = 15000.0,
                detail = "Nasi Goreng Ayam Pedas",
                restoAddress = "jl.seturan",
                locationUrl = "http://tokonasigoreng",
            )
        every { cartRepository.createCart(any(), any()) } returns
            flow { emit(ResultWrapper.Success(true)) }
        viewModel.addItemToCart(menu1)
        verify { cartRepository.createCart(menu1, 1) }
    }

    @Test
    fun addItemToCart_error() {
        val menu1 =
            Menu(
                imageUrl = "http://nasigoreng.jpg",
                name = "Nasi Goreng",
                formatPrice = "15.000",
                price = 15000.0,
                detail = "Nasi Goreng Ayam Pedas",
                restoAddress = "jl.seturan",
                locationUrl = "http://tokonasigoreng",
            )
        every { cartRepository.createCart(any(), any()) } returns
            flow { emit(ResultWrapper.Error(IllegalStateException("Error: Add Item"))) }
        viewModel.addItemToCart(menu1)
        verify { cartRepository.createCart(menu1, 1) }
    }

    @Test
    fun addItemToCart_empty() {
        val menu1 =
            Menu(
                imageUrl = "http://nasigoreng.jpg",
                name = "Nasi Goreng",
                formatPrice = "15.000",
                price = 15000.0,
                detail = "Nasi Goreng Ayam Pedas",
                restoAddress = "jl.seturan",
                locationUrl = "http://tokonasigoreng",
            )
        every { cartRepository.createCart(any(), any()) } returns
            flow { emit(ResultWrapper.Empty(false)) }
        viewModel.addItemToCart(menu1)
        verify { cartRepository.createCart(menu1, 1) }
    }

    @Test
    fun isUsingGridMode() {
        every { userPreferenceRepository.isUsingGridMode() } returns true
        val result = viewModel.isUsingGridMode()
        assertEquals(true, result)
        verify { userPreferenceRepository.isUsingGridMode() }
    }

    @Test
    fun setUsingGridMode() {
        every { userPreferenceRepository.setUsingGridMode(any()) } returns Unit
        val result = viewModel.setUsingGridMode(true)
        assertEquals(Unit, result)
        verify { userPreferenceRepository.setUsingGridMode(any()) }
    }
}
