package com.example.bloomkitchen.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Menu(
    var id: String? = UUID.randomUUID().toString(),
    var imageUrl: String,
    var name: String,
    var formatPrice: String,
    var price: Double,
    var detail: String,
    var restoAddress: String,
    var locationUrl: String,
) : Parcelable
