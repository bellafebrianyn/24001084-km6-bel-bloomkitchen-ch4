package com.example.bloomkitchen.data.source.network.model.checkout

import com.google.gson.annotations.SerializedName

data class CheckoutItemPayload(
    @SerializedName("nama")
    val menuName: String,
    @SerializedName("qty")
    val quantity: Int,
    @SerializedName("catatan")
    val notes: String?,
    @SerializedName("harga")
    val menuPrice: Int,
)
