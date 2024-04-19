package com.example.bloomkitchen.data.datasouce.menu

import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.data.source.network.model.checkout.CheckoutRequestPayload
import com.example.bloomkitchen.data.source.network.model.checkout.CheckoutResponse
import com.example.bloomkitchen.data.source.network.model.menu.ListMenuResponse

interface MenuDataSource {
    suspend fun getMenu(categorySlug: String? = null): ListMenuResponse
    suspend fun createOrder(payload : CheckoutRequestPayload) : CheckoutResponse
}