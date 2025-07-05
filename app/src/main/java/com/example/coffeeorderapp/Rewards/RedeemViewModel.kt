package com.example.coffeeorderapp.Rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeeorderapp.cart.data.ProductEntity
import com.example.coffeeorderapp.cart.data.ProductRepository
import kotlinx.coroutines.launch

class RedeemViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _products = MutableLiveData<List<ProductEntity>>()
    val products: LiveData<List<ProductEntity>> = _products

    private val _redeemSuccess = MutableLiveData<Boolean>()
    val redeemSuccess: LiveData<Boolean> = _redeemSuccess

    fun loadProducts() {
        viewModelScope.launch {
            val products = productRepository.getThreeCheapestProducts()
            println("DEBUG: RedeemViewModel loaded ${products.size} cheapest products: ${products.map { it.name }}")
            _products.value = products
        }
    }
} 