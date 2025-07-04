package com.example.coffeeorderapp.Details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffeeorderapp.HomePage.Data.Coffee

class DetailsViewModel : ViewModel() {
    private val _coffee = MutableLiveData<Coffee>()
    val coffee: LiveData<Coffee> = _coffee

    private val _addToCartEvent = MutableLiveData<Unit>()
    val addToCartEvent: LiveData<Unit> = _addToCartEvent

    fun setCoffee(coffee: Coffee) {
        _coffee.value = coffee
    }

    fun addToCart() {
        // Business logic for adding to cart can go here
        _addToCartEvent.value = Unit
    }
} 