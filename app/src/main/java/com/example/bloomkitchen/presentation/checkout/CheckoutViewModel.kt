package com.example.bloomkitchen.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.bloomkitchen.data.repository.CartRepository
import kotlinx.coroutines.Dispatchers

class CheckoutViewModel(private val cartRepository: CartRepository) : ViewModel() {

    val checkoutData = cartRepository.getCheckoutData().asLiveData(Dispatchers.IO)

}