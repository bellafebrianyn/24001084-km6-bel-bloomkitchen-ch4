package com.example.bloomkitchen.data.repository

import com.example.bloomkitchen.data.datasouce.menu.MenuDataSource
import com.example.bloomkitchen.data.mapper.toMenu
import com.example.bloomkitchen.data.model.Cart
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.data.source.network.model.checkout.CheckoutItemPayload
import com.example.bloomkitchen.data.source.network.model.checkout.CheckoutRequestPayload
import com.example.bloomkitchen.utils.ResultWrapper
import com.example.bloomkitchen.utils.proceedFlow
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MenuRepository {
    fun getMenu(categorySlug: String? = null): Flow<ResultWrapper<List<Menu>>>
    fun createOrder(products: List<Cart>): Flow<ResultWrapper<Boolean>>

}

class MenuRepositoryImpl(
    private val dataSource: MenuDataSource,
    private val userRepository: UserRepository
) : MenuRepository {
    override fun getMenu(categorySlug: String?): Flow<ResultWrapper<List<Menu>>> {
        return flow {
            emit(ResultWrapper.Loading())
            delay(2000)
            val menuData = dataSource.getMenu(categorySlug).data.toMenu()
            emit(ResultWrapper.Success(menuData))
        }
    }

    override fun createOrder(menu: List<Cart>): Flow<ResultWrapper<Boolean>> {
        return proceedFlow {
            val currentUser = userRepository.getCurrentUser()
            val total = menu.sumOf { it.menuPrice }
            dataSource.createOrder(CheckoutRequestPayload(
                total = total,
                fullName = currentUser?.fullName,
                orders = menu.map {
                    CheckoutItemPayload(
                        menuName = it.menuName,
                        quantity = it.itemQuantity,
                        notes = it.itemNotes,
                        menuPrice = it.menuPrice.toInt()
                    )
                }
            )).status ?: false
        }
    }
}