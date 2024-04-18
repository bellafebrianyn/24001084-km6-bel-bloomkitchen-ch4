package com.example.bloomkitchen.data.model

import androidx.annotation.DrawableRes
import java.util.UUID

data class Profile(
    var id: String = UUID.randomUUID().toString(),
    @DrawableRes
    var image: Int,
    var username: String,
    var email: String
)
