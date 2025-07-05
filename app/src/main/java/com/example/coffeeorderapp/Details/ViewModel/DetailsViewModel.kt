package com.example.coffeeorderapp.Details.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeeorderapp.Details.Customization.CartItem
import com.example.coffeeorderapp.cart.data.DatabaseModule
import com.example.coffeeorderapp.HomePage.Data.Coffee
import com.example.coffeeorderapp.Details.Customization.CoffeeCustomization
import com.example.coffeeorderapp.Details.Customization.ShotType
import com.example.coffeeorderapp.Details.Customization.SizeType
import kotlinx.coroutines.launch

class DetailsViewModel(application: Application) : AndroidViewModel(application) {
    private val cartRepository = DatabaseModule.getCartRepository(application)
    
    private val _coffee = MutableLiveData<Coffee>()
    val coffee: LiveData<Coffee> = _coffee

    private val _addToCartEvent = MutableLiveData<Unit>()
    val addToCartEvent: LiveData<Unit> = _addToCartEvent

    private val _customization = MutableLiveData<CoffeeCustomization>(CoffeeCustomization())
    val customization: LiveData<CoffeeCustomization> = _customization

    private val _price = MutableLiveData<Double>(3.0)
    val price: LiveData<Double> = _price

    fun setCoffee(coffee: Coffee) {
        _coffee.value = coffee
        recalculatePrice()
    }

    fun addToCart() {
        _addToCartEvent.value = Unit
    }

    fun updateCustomization(update: CoffeeCustomization) {
        _customization.value = update
        recalculatePrice()
    }

    private fun recalculatePrice() {
        val coffee = _coffee.value ?: return
        val custom = _customization.value ?: CoffeeCustomization()
        var price = coffee.price
        
        // Add shot surcharge
        price += when (custom.shot) {
            ShotType.Single -> 0.0
            ShotType.Double -> 0.5
        }
        
        // Add size surcharge
        price += when (custom.size) {
            SizeType.Small -> 0.0
            SizeType.Medium -> 0.25
            SizeType.Large -> 0.5
        }
        
        _price.value = price * custom.quantity
    }

    fun addToCartWithCustomization(coffee: Coffee) {
        val custom = _customization.value ?: CoffeeCustomization()
        val currentPrice = _price.value ?: 3.0
        // Use the stored coffee object which has the correct price
        val storedCoffee = _coffee.value ?: coffee
        android.util.Log.d("DetailsViewModel", "Adding to cart: ${storedCoffee.name}, price: $currentPrice, quantity: ${custom.quantity}")
        
        viewModelScope.launch {
            cartRepository.addItem(CartItem(id = 0, storedCoffee, custom, currentPrice))
            _addToCartEvent.value = Unit
        }
    }
} 