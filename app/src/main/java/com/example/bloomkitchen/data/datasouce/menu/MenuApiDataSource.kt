package com.example.bloomkitchen.data.datasouce.menu

import com.example.bloomkitchen.data.source.network.model.checkout.CheckoutRequestPayload
import com.example.bloomkitchen.data.source.network.model.checkout.CheckoutResponse
import com.example.bloomkitchen.data.source.network.model.menu.ListMenuResponse
import com.example.bloomkitchen.data.source.network.service.BloomKitchenApiService

class MenuApiDataSource(
    private val service: BloomKitchenApiService,
) : MenuDataSource {
    override suspend fun getMenu(categorySlug: String?): ListMenuResponse {
        return service.getMenu(categorySlug)
    }

    override suspend fun createOrder(payload: CheckoutRequestPayload): CheckoutResponse {
        return service.createOrder(payload)
    }
}
