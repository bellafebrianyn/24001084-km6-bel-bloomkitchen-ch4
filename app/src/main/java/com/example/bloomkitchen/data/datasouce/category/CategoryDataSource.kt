package com.example.bloomkitchen.data.datasouce.category

import com.example.bloomkitchen.data.model.Category

interface CategoryDataSource {
    fun getCategories() : List<Category>
}