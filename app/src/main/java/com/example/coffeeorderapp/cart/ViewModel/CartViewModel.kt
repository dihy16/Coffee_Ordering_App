package com.example.coffeeorderapp.cart.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffeeorderapp.Details.Customization.CartItem
import com.example.coffeeorderapp.Details.Customization.CartRepository

class CartViewModel : ViewModel() {
    private val _cartItems = MutableLiveData<List<CartItem>>(CartRepository.getItems())
    val cartItems: LiveData<List<CartItem>> = _cartItems

    val totalPrice: LiveData<Double> = MutableLiveData(
        CartRepository.getItems().sumOf { it.totalPrice }
    )

    init {
        android.util.Log.d("CartViewModel", "Initialized with ${CartRepository.getItems().size} items")
        android.util.Log.d("CartViewModel", "Initial total price: ${CartRepository.getItems().sumOf { it.totalPrice }}")
    }

    fun removeItem(item: CartItem) {
        CartRepository.getItems().toMutableList().let { list ->
            list.remove(item)
            CartRepository.clear()
            list.forEach { CartRepository.addItem(it) }
            _cartItems.value = CartRepository.getItems()
            (totalPrice as MutableLiveData).value = list.sumOf { it.totalPrice }
        }
    }

    fun refreshCart() {
        val items = CartRepository.getItems()
        val total = items.sumOf { it.totalPrice }
        android.util.Log.d("CartViewModel", "Refreshing cart: ${items.size} items, total: $total")
        _cartItems.value = items
        (totalPrice as MutableLiveData).value = total
    }
} 