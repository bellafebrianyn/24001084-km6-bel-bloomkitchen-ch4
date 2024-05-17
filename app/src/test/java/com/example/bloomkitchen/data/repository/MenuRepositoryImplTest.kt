package com.example.bloomkitchen.data.repository

import app.cash.turbine.test
import com.example.bloomkitchen.data.datasouce.menu.MenuDataSource
import com.example.bloomkitchen.data.model.Cart
import com.example.bloomkitchen.data.model.User
import com.example.bloomkitchen.data.source.network.model.checkout.CheckoutResponse
import com.example.bloomkitchen.data.source.network.model.menu.ListMenuItemResponse
import com.example.bloomkitchen.data.source.network.model.menu.ListMenuResponse
import com.example.bloomkitchen.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MenuRepositoryImplTest {
    @MockK
    lateinit var datasource: MenuDataSource

    @MockK
    lateinit var userRepository: UserRepository
    private lateinit var menuRepository: MenuRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        menuRepository = MenuRepositoryImpl(datasource, userRepository)
    }

    @Test
    fun getMenu() {
        val menu1 =
            ListMenuItemResponse(
                imageUrl = "imgUrl",
                name = "Ayam Bakar",
                format_price = "Rp10.000",
                price = 5000.0,
                detail = "ayam bakar",
                resto_address = "jl. seturan",
            )
        val menu2 =
            ListMenuItemResponse(
                imageUrl = "imgUrl",
                name = "Ayam Goreng",
                format_price = "Rp20.000",
                price = 5000.0,
                detail = "ayam Goreng",
                resto_address = "jl. seturan",
            )

        val mockListMenu = listOf(menu1, menu2)
        val mockResponse = mockk<ListMenuResponse>()
        every { mockResponse.data } returns mockListMenu
        runTest {
            coEvery { datasource.getMenu(any()) } returns mockResponse
            menuRepository.getMenu("makanan").map {
                delay(100)
                it
            }.test {
                delay(2210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { datasource.getMenu(any()) }
            }
        }
    }

    @Test
    fun getMenu_when_category_null() {
        val menu1 =
            ListMenuItemResponse(
                imageUrl = "imgUrl",
                name = "Ayam Bakar",
                format_price = "Rp10.000",
                price = 5000.0,
                detail = "ayam bakar",
                resto_address = "jl. seturan",
            )
        val menu2 =
            ListMenuItemResponse(
                imageUrl = "imgUrl",
                name = "Ayam Goreng",
                format_price = "Rp20.000",
                price = 5000.0,
                detail = "ayam Goreng",
                resto_address = "jl. seturan",
            )

        val mockListMenu = listOf(menu1, menu2)
        val mockResponse = mockk<ListMenuResponse>()
        every { mockResponse.data } returns mockListMenu
        runTest {
            coEvery { datasource.getMenu(any()) } returns mockResponse
            menuRepository.getMenu().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val data = expectMostRecentItem()
                assertTrue(data is ResultWrapper.Success)
                coVerify { datasource.getMenu(any()) }
            }
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val cart1 = Cart(1, "3GHG", "Ayam Bakar", "imgUrl", 5000.0, 1, " spicy")
            val cart2 = Cart(2, "3gFG", "Ayam Goreng", "imgUrl", 5000.0, 1, "ga pedes")
            val items = listOf(cart1, cart2)

            val currentUser = User(id = "1", fullName = "John Doe", email = "john@example.com")
            coEvery { userRepository.getCurrentUser() } returns currentUser

            coEvery { datasource.createOrder(any()) } returns
                CheckoutResponse(
                    status = true,
                    message = "success",
                    code = 200,
                )

            menuRepository.createOrder(items).map {
                delay(100)
                it
            }.test {
                delay(201)
                val actualResult = expectMostRecentItem()
                assertTrue(actualResult is ResultWrapper.Success)
            }
            coVerify { userRepository.getCurrentUser() }
            coVerify { datasource.createOrder(any()) }
        }
    }
}
