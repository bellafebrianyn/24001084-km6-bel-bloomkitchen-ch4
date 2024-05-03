package com.example.bloomkitchen.data.source.network.model.checkout

import com.google.gson.annotations.SerializedName

data class CheckoutRequestPayload(
    @SerializedName("username")
    val fullName: String?,
    @SerializedName("total")
    val total: Double,
    @SerializedName("orders")
    val orders: List<CheckoutItemPayload>,
)
