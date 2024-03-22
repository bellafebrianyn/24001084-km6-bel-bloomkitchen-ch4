package com.example.bloomkitchen.data.model

import androidx.annotation.DrawableRes
import java.util.UUID

data class Menu(
    var id: String = UUID.randomUUID().toString(),
    @DrawableRes
    var image: Int,
    var name: String,
    var price: Double,
    var desc: String,
    var location: String,
    var googleMapsLink : String
)
