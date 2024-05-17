package com.example.bloomkitchen.data.repository

import app.cash.turbine.test
import com.example.bloomkitchen.data.datasouce.cart.CartDataSource
import com.example.bloomkitchen.data.model.Cart
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.data.model.PriceItem
import com.example.bloomkitchen.data.source.local.database.entity.CartEntity
import com.example.bloomkitchen.utils.ResultWrapper
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CartRepositoryImplTest {
    @MockK
    lateinit var dataSource: CartDataSource

    private lateinit var repository: CartRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = CartRepositoryImpl(dataSource)
    }

    @Test
    fun getUserCartData_success() {
        val entity1 =
            CartEntity(
                id = 1,
                menuId = "AD658",
                menuName = "Nasi Goreng",
                menuImgUrl = "nasigorengbdka",
                menuPrice = 15000.0,
                itemQuantity = 3,
                itemNotes = "pedas",
            )
        val entity2 =
            CartEntity(
                id = 2,
                menuId = "GI580",
                menuName = "Ayam Goreng",
                menuImgUrl = "ayamgorengvdak",
                menuPrice = 15000.0,
                itemQuantity = 3,
                itemNotes = "ayam dada",
            )
        val mockList = listOf(entity1, entity2)
        val mockFlow = flow { emit(mockList) }
        every { dataSource.getAllCarts() } returns mockFlow

        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Success)
                assertEquals(mockList.size, actualData.payload?.first?.size)
                Assert.assertEquals(90000.0, actualData.payload?.second)
                verify { dataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun getUserCartData_loading() {
        val entity1 =
            CartEntity(
                id = 1,
                menuId = "AD658",
                menuName = "Nasi Goreng",
                menuImgUrl = "nasigorengbdka",
                menuPrice = 15000.0,
                itemQuantity = 3,
                itemNotes = "pedas",
            )
        val entity2 =
            CartEntity(
                id = 2,
                menuId = "GI580",
                menuName = "Ayam Goreng",
                menuImgUrl = "ayamgorengvdak",
                menuPrice = 15000.0,
                itemQuantity = 3,
                itemNotes = "ayam dada",
            )
        val mockList = listOf(entity1, entity2)
        val mockFlow = flow { emit(mockList) }
        every { dataSource.getAllCarts() } returns mockFlow

        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2111)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Loading)
                verify { dataSource.getAllCarts() }
            }
        }
    }

    /*@Test
    fun getUserCartData_error() {
        every { dataSource.getAllCarts() } returns
            flow {
                throw IllegalStateException("Error")
            }

        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Error)
                verify { dataSource.getAllCarts() }
            }
        }
    }*/

    @Test
    fun getUserCartData_empty() {
        val mockList = listOf<CartEntity>()
        val mockFlow = flow { emit(mockList) }
        every { dataSource.getAllCarts() } returns mockFlow

        runTest {
            repository.getUserCartData().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Empty)
                assertEquals(true, actualData.payload?.first?.isEmpty())
                verify { dataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_success() {
        val entity1 =
            CartEntity(
                id = 1,
                menuId = "AD658",
                menuName = "Nasi Goreng",
                menuImgUrl = "nasigorengbdka",
                menuPrice = 15000.0,
                itemQuantity = 3,
                itemNotes = "pedas",
            )
        val entity2 =
            CartEntity(
                id = 2,
                menuId = "GI580",
                menuName = "Ayam Goreng",
                menuImgUrl = "ayamgorengvdak",
                menuPrice = 15000.0,
                itemQuantity = 3,
                itemNotes = "ayam dada",
            )
        val mockPrice = listOf(PriceItem("Nasi Goreng", 45000.0), PriceItem("Ayam Goreng", 45000.0))
        val mockList = listOf(entity1, entity2)
        val mockFlow = flow { emit(mockList) }
        every { dataSource.getAllCarts() } returns mockFlow

        runTest {
            repository.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(2201)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Success)
                assertEquals(mockList.size, actualData.payload?.first?.size)
                assertEquals(mockPrice, actualData.payload?.second)
                assertEquals(90000.0, actualData.payload?.third)
                verify { dataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_loading() {
        val mockList = listOf<CartEntity>()
        val mockFlow = flow { emit(mockList) }
        every { dataSource.getAllCarts() } returns mockFlow
        runTest {
            repository.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(1110)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Loading)
                verify { dataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_error() {
        every { dataSource.getAllCarts() } returns
            flow {
                throw IllegalStateException("Error Checkout")
            }
        runTest {
            repository.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Error)
                verify { dataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun getCheckoutData_empty() {
        val mockList = listOf<CartEntity>()
        val mockFlow = flow { emit(mockList) }
        every { dataSource.getAllCarts() } returns mockFlow
        runTest {
            repository.getCheckoutData().map {
                delay(100)
                it
            }.test {
                delay(2210)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Empty)
                assertEquals(true, actualData.payload?.first?.isEmpty())
                verify { dataSource.getAllCarts() }
            }
        }
    }

    @Test
    fun createCart_success() {
        val mockMenu = mockk<Menu>(relaxed = true)
        every { mockMenu.id } returns "123"
        coEvery { dataSource.insertCart(any()) } returns 1
        runTest {
            repository.createCart(mockMenu, 3, "pedas")
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2201)
                    val actualData = expectMostRecentItem()
                    Assert.assertTrue(actualData is ResultWrapper.Success)
                    assertEquals(true, actualData.payload)
                    coVerify { dataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_success_on_notes_null() {
        val mockMenu = mockk<Menu>(relaxed = true)
        every { mockMenu.id } returns "123"
        coEvery { dataSource.insertCart(any()) } returns 1
        runTest {
            repository.createCart(mockMenu, 3)
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2201)
                    val actualData = expectMostRecentItem()
                    Assert.assertTrue(actualData is ResultWrapper.Success)
                    assertEquals(true, actualData.payload)
                    coVerify { dataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_error_when_id_null() {
        val mockMenu = mockk<Menu>(relaxed = true)
        every { mockMenu.id } returns null
        coEvery { dataSource.insertCart(any()) } returns 1
        runTest {
            repository.createCart(mockMenu, 3, "pedas")
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2010)
                    val actualData = expectMostRecentItem()
                    Assert.assertTrue(actualData is ResultWrapper.Error)
                    coVerify(atLeast = 0) { dataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_loading() {
        val mockMenu = mockk<Menu>(relaxed = true)
        every { mockMenu.id } returns "123"
        coEvery { dataSource.insertCart(any()) } returns 1
        runTest {
            repository.createCart(mockMenu, 3, "pedas")
                .map {
                    delay(100)
                    it
                }.test {
                    delay(101)
                    val actualData = expectMostRecentItem()
                    Assert.assertTrue(actualData is ResultWrapper.Loading)
                    coVerify { dataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_error() {
        val mockMenu = mockk<Menu>(relaxed = true)
        every { mockMenu.id } returns "123"
        coEvery { dataSource.insertCart(any()) } throws IllegalStateException("Error Create Cart")
        runTest {
            repository.createCart(mockMenu, 3, "pedas")
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2201)
                    val actualData = expectMostRecentItem()
                    Assert.assertTrue(actualData is ResultWrapper.Error)
                    coVerify { dataSource.insertCart(any()) }
                }
        }
    }

    @Test
    fun createCart_empty() {
        runTest {
            val mockList = listOf<CartEntity>()
            val mockFlow = flow { emit(mockList) }
            every { dataSource.getAllCarts() } returns mockFlow
            repository.getUserCartData()
                .map {
                    delay(100)
                    it
                }.test {
                    delay(2210)
                    val actualData = expectMostRecentItem()
                    Assert.assertTrue(actualData is ResultWrapper.Empty)
                    verify { dataSource.getAllCarts() }
                }
        }
    }

    @Test
    fun decreaseCart_when_quantity_more_than_0() {
        val mockCart =
            Cart(
                id = 2,
                menuId = "GI580",
                menuName = "Ayam Goreng",
                menuImgUrl = "ayamgorengvdak",
                menuPrice = 15000.0,
                itemQuantity = 3,
                itemNotes = "ayam dada",
            )
        coEvery { dataSource.deleteCart(any()) } returns 1
        coEvery { dataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 0) { dataSource.deleteCart(any()) }
                coVerify(atLeast = 1) { dataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun decreaseCart_when_quantity_less_than_1() {
        val mockCart =
            Cart(
                id = 2,
                menuId = "GI580",
                menuName = "Ayam Goreng",
                menuImgUrl = "ayamgorengvdak",
                menuPrice = 15000.0,
                itemQuantity = 0,
            )
        coEvery { dataSource.deleteCart(any()) } returns 1
        coEvery { dataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify { dataSource.deleteCart(any()) }
                coVerify(atLeast = 0) { dataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun increaseCart() {
        val mockCart =
            Cart(
                id = 2,
                menuId = "GI580",
                menuName = "Ayam Goreng",
                menuImgUrl = "ayamgorengvdak",
                menuPrice = 15000.0,
                itemQuantity = 3,
                itemNotes = "ayam dada",
            )
        coEvery { dataSource.updateCart(any()) } returns 1
        runTest {
            repository.increaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 1) { dataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun setCartNotes() {
        val mockCart =
            Cart(
                id = 2,
                menuId = "GI580",
                menuName = "Ayam Goreng",
                menuImgUrl = "ayamgorengvdak",
                menuPrice = 15000.0,
                itemQuantity = 3,
                itemNotes = "ayam dada",
            )

        coEvery { dataSource.updateCart(any()) } returns 1
        runTest {
            repository.decreaseCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Success)
                coVerify(atLeast = 0) { dataSource.deleteCart(any()) }
                coVerify(atLeast = 1) { dataSource.updateCart(any()) }
            }
        }
    }

    @Test
    fun deleteCart() {
        val mockCart =
            Cart(
                id = 2,
                menuId = "GI580",
                menuName = "Ayam Goreng",
                menuImgUrl = "ayamgorengvdak",
                menuPrice = 15000.0,
                itemQuantity = 3,
                itemNotes = "ayam dada",
            )
        coEvery { dataSource.deleteCart(any()) } returns 1
        runTest {
            repository.deleteCart(mockCart).map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Success)
            }
            coVerify { dataSource.deleteCart(any()) }
        }
    }

    @Test
    fun deleteAllCarts() {
        coEvery { dataSource.deleteAll() } returns Unit
        runTest {
            repository.deleteAllCarts().map {
                delay(100)
                it
            }.test {
                delay(210)
                val actualData = expectMostRecentItem()
                Assert.assertTrue(actualData is ResultWrapper.Success)
            }
            coVerify { dataSource.deleteAll() }
        }
    }
}
