package com.example.bloomkitchen.data.datasouce.category

import com.example.bloomkitchen.data.source.network.model.category.CategoriesResponse

interface CategoryDataSource {
    suspend fun getCategories(): CategoriesResponse
}
