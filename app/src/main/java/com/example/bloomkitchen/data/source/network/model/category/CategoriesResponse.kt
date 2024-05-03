package com.example.bloomkitchen.data.source.network.model.category

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CategoriesResponse(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<CategoryItemResponse>?,
)
