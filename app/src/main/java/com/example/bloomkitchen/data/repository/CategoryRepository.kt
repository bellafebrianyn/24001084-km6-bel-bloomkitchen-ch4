package com.example.bloomkitchen.data.repository

import com.example.bloomkitchen.data.datasouce.category.CategoryDataSource
import com.example.bloomkitchen.data.mapper.toCategories
import com.example.bloomkitchen.data.model.Category
import com.example.bloomkitchen.utils.ResultWrapper
import com.example.bloomkitchen.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CategoryRepository {
    fun getCategories(): Flow<ResultWrapper<List<Category>>>
}

class CategoryRepositoryImpl(
    private val dataSource: CategoryDataSource
) : CategoryRepository {
    override fun getCategories(): Flow<ResultWrapper<List<Category>>> {
        return flow {
            emit(ResultWrapper.Loading())
            delay(2000)
            val categoryData = dataSource.getCategories().data.toCategories()
            emit(ResultWrapper.Success(categoryData))
        }
    }
}