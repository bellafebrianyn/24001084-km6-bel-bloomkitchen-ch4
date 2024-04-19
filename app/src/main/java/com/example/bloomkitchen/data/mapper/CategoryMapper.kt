package com.example.bloomkitchen.data.mapper

import com.example.bloomkitchen.data.model.Category
import com.example.bloomkitchen.data.source.network.model.category.CategoryItemResponse

fun CategoryItemResponse?.toCategory() =
    Category(
        imageUrl = this?.imageUrl.orEmpty(),
        name = this?.name.orEmpty()
    )

fun Collection<CategoryItemResponse>?.toCategories() =
    this?.map { it.toCategory() } ?: listOf()