package com.example.bloomkitchen.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.bloomkitchen.data.repository.CartRepository
import com.example.bloomkitchen.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository
    ) : ViewModel() {

    val checkoutData = cartRepository.getCheckoutData().asLiveData(Dispatchers.IO)

    fun deleteAllCarts() {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteAllCarts().collect()
        }
    }

    fun isUserLoggedOut() = userRepository.doLogout()
    fun isUserLoggedIn() = userRepository.isLoggedIn()
}