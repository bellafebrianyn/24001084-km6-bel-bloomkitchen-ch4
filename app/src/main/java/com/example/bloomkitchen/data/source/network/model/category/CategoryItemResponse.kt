package com.example.bloomkitchen.data.source.network.model.category

import com.google.gson.annotations.SerializedName

data class CategoryItemResponse(
    @SerializedName("image_url")
    val imageUrl: String?,
    @SerializedName("nama")
    val name: String?
)
