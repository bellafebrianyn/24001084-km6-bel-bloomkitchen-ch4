package com.example.bloomkitchen.presentation.home

import androidx.lifecycle.ViewModel
import com.example.bloomkitchen.data.repository.CategoryRepository
import com.example.bloomkitchen.data.repository.MenuRepository

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val menuRepository: MenuRepository
): ViewModel() {

    fun getMenuList() = menuRepository.getMenuList()
    fun getCategories() = categoryRepository.getCategories()
}