package com.example.bloomkitchen.data.source.network.model.menu

import com.google.gson.annotations.SerializedName

data class ListMenuItemResponse(
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("nama")
    val name: String?,
    @SerializedName("harga_format")
    val format_price: String?,
    @SerializedName("harga")
    val price: Double?,
    @SerializedName("detail")
    val detail: String?,
    @SerializedName("alamat_resto")
    val resto_address: String?,
)
