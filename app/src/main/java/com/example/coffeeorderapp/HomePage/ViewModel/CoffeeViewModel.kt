package com.example.coffeeorderapp.HomePage.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeeorderapp.cart.data.ProductEntity
import com.example.coffeeorderapp.cart.data.ProductRepository
import kotlinx.coroutines.launch

class CoffeeViewModel(private val productRepository: ProductRepository) : ViewModel() {
    private val _coffees = MutableLiveData<List<ProductEntity>>()
    val coffees: LiveData<List<ProductEntity>> get() = _coffees

    fun loadCoffees() {
        viewModelScope.launch {
            val products = productRepository.getAllProducts()
            println("DEBUG: CoffeeViewModel loaded ${products.size} products: ${products.map { it.name }}")
            _coffees.value = products
        }
    }
}