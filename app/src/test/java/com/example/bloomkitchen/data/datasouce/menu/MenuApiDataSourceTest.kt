package com.example.bloomkitchen.data.datasouce.menu

import com.example.bloomkitchen.data.source.network.model.checkout.CheckoutItemPayload
import com.example.bloomkitchen.data.source.network.model.checkout.CheckoutRequestPayload
import com.example.bloomkitchen.data.source.network.model.checkout.CheckoutResponse
import com.example.bloomkitchen.data.source.network.model.menu.ListMenuResponse
import com.example.bloomkitchen.data.source.network.service.BloomKitchenApiService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class MenuApiDataSourceTest {
    @MockK
    lateinit var service: BloomKitchenApiService
    lateinit var dataSource: MenuDataSource

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        dataSource = MenuApiDataSource(service)
    }

    @Test
    fun getMenu() {
        runTest {
            val mockResponse = mockk<ListMenuResponse>(relaxed = true)
            coEvery { service.getMenu() } returns mockResponse
            val actualResult = dataSource.getMenu()
            assertEquals(mockResponse, actualResult)
        }
    }

    @Test
    fun createOrder() {
        runTest {
            val payload =
                CheckoutRequestPayload(
                    fullName = "John Doe",
                    total = 50.0,
                    orders =
                        listOf(
                            CheckoutItemPayload(
                                menuName = "Burger",
                                quantity = 2,
                                notes = "No onions",
                                menuPrice = 10,
                            ),
                            CheckoutItemPayload(
                                menuName = "Fries",
                                quantity = 1,
                                notes = null,
                                menuPrice = 5,
                            ),
                        ),
                )

            val mockResponse = mockk<CheckoutResponse>(relaxed = true)
            coEvery { service.createOrder(payload) } returns mockResponse
            val actualResponse = dataSource.createOrder(payload)
            coVerify { service.createOrder(payload) }
            assertEquals(mockResponse, actualResponse)
        }
    }
}
