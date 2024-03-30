package com.example.bloomkitchen.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.data.repository.CartRepository
import com.example.bloomkitchen.data.repository.CategoryRepository
import com.example.bloomkitchen.data.repository.MenuRepository
import com.example.bloomkitchen.utils.ResultWrapper
import kotlinx.coroutines.Dispatchers

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val menuRepository: MenuRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    fun getMenuList() = menuRepository.getMenuList()
    fun getCategories() = categoryRepository.getCategories()

    val menuCountLiveData = MutableLiveData(0).apply {
        postValue(0)
    }
    val priceLiveData = MutableLiveData<Double>().apply {
        postValue(0.0)
    }

    fun addItemToCart(menu: Menu) {
        menuCountLiveData.value = 1

        cartRepository.createCart(menu, 1)
            .asLiveData(Dispatchers.IO)
            .observeForever { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        println("Add to Cart Success")
                    }

                    is ResultWrapper.Error -> {
                        println("Add to Cart Failed")
                    }

                    is ResultWrapper.Loading -> {
                        println("Loading..")
                    }

                    else -> {
                    }
                }
            }
    }

}