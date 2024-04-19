package com.example.bloomkitchen.data.datasouce.category

import com.example.bloomkitchen.data.source.network.model.category.CategoriesResponse
import com.example.bloomkitchen.data.source.network.service.BloomKitchenApiService

class CategoryApiDataSource(private val service: BloomKitchenApiService): CategoryDataSource {
    override suspend fun getCategories(): CategoriesResponse {
        return service.getCategories()
    }

}