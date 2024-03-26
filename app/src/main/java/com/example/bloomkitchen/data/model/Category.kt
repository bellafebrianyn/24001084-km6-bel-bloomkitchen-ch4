package com.example.bloomkitchen.data.model

import java.util.UUID

data class Category(
    var id: String = UUID.randomUUID().toString(),
    var imgUrl: String,
    var name: String,
)
