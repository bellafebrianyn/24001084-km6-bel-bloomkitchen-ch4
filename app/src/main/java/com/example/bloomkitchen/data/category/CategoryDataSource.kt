package com.example.bloomkitchen.data.category

import com.example.bloomkitchen.data.model.Category

interface CategoryDataSource {
    fun getCategories() : List<Category>
}