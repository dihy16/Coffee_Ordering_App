package com.example.coffeeorderapp.cart.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeeorderapp.Details.Customization.CartItem
import com.example.coffeeorderapp.cart.data.DatabaseModule
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartRepository = DatabaseModule.getCartRepository(application)
    
    private val _cartItems = MutableLiveData<List<CartItem>>()
    val cartItems: LiveData<List<CartItem>> = _cartItems

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    init {
        loadCartItems()
    }

    private fun loadCartItems() {
        viewModelScope.launch {
            val items = cartRepository.getAllItems()
            _cartItems.value = items
            _totalPrice.value = items.sumOf { it.totalPrice }
        }
    }

    fun removeItem(item: CartItem) {
        android.util.Log.d("CartViewModel", "Removing item with id: ${item.id}, name: ${item.coffee.name}")
        viewModelScope.launch {
            cartRepository.removeItem(item)
            android.util.Log.d("CartViewModel", "Item removed from database, reloading cart")
            loadCartItems() // Reload to update UI
        }
    }

    fun addItem(item: CartItem) {
        viewModelScope.launch {
            cartRepository.addItem(item)
            loadCartItems() // Reload to update UI
        }
    }

    fun refreshCart() {
        loadCartItems()
    }
} 