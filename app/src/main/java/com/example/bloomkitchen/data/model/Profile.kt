package com.example.bloomkitchen.data.model

import java.util.UUID

data class Profile(
    var id: String = UUID.randomUUID().toString(),
    var image: String,
    var username: String,
    var email: String,
    var phoneNumber: Number
)
