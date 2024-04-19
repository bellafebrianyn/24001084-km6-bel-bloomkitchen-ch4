package com.example.bloomkitchen.data.mapper

import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.data.source.network.model.menu.ListMenuItemResponse

fun ListMenuItemResponse?.toMenu() =
    Menu(
        imageUrl = this?.imageUrl.orEmpty(),
        name = this?.name.orEmpty(),
        formatPrice = this?.format_price.orEmpty(),
        price = this?.price ?: 0.0,
        detail = this?.detail.orEmpty(),
        restoAddress = this?.resto_address.orEmpty(),
        locationUrl = "https://maps.app.goo.gl/h4wQKqaBuXzftGK77"
    )

fun Collection<ListMenuItemResponse>?.toMenu() = this?.map {
    it.toMenu()
} ?: listOf()
