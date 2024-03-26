package com.example.bloomkitchen.data.repository

import com.example.bloomkitchen.data.category.CategoryDataSource
import com.example.bloomkitchen.data.model.Category

interface CategoryRepository {
    fun getCategories(): List<Category>
}

class CategoryRepositoryImpl(private val dataSource: CategoryDataSource) : CategoryRepository {
    override fun getCategories(): List<Category> = dataSource.getCategories()
}