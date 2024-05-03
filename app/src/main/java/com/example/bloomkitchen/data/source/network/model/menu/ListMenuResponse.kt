package com.example.bloomkitchen.data.source.network.model.menu

import com.google.gson.annotations.SerializedName

data class ListMenuResponse(
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: List<ListMenuItemResponse>?,
)
