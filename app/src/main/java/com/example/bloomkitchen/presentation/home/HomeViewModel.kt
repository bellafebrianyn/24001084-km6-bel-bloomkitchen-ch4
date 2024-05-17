package com.example.bloomkitchen.presentation.home

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bloomkitchen.data.model.Menu
import com.example.bloomkitchen.data.repository.CartRepository
import com.example.bloomkitchen.data.repository.CategoryRepository
import com.example.bloomkitchen.data.repository.MenuRepository
import com.example.bloomkitchen.data.repository.UserPreferenceRepository
import com.example.bloomkitchen.data.repository.UserRepository
import com.example.bloomkitchen.utils.proceedWhen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val menuRepository: MenuRepository,
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
) : ViewModel() {
    fun getMenu(categorySlug: String? = null) = menuRepository.getMenu(categorySlug).asLiveData(Dispatchers.IO)

    fun getCategories() = categoryRepository.getCategories().asLiveData(Dispatchers.IO)

    fun getCurrentUser() = userRepository.getCurrentUser()

    fun userIsLoggedIn() = userRepository.isLoggedIn()

    val menuCountLiveData =
        MutableLiveData(0).apply {
            postValue(0)
        }

    fun addItemToCart(menu: Menu) {
        menuCountLiveData.value = 1
        viewModelScope.launch {
            cartRepository.createCart(menu, 1).collect {
                it.proceedWhen(
                    doOnSuccess = {
                        Log.d(TAG, "addItemToCart: Success")
                    },
                    doOnError = {
                        Log.d(TAG, "addItemToCart: Error")
                    },
                    doOnEmpty = {
                        Log.d(TAG, "addItemToCart: Empty")
                    },
                )
            }
        }
    }

    fun isUsingGridMode() = userPreferenceRepository.isUsingGridMode()

    fun setUsingGridMode(isUsingGridMode: Boolean) = userPreferenceRepository.setUsingGridMode(isUsingGridMode)
}
