package com.example.coffeeorderapp.HomePage.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffeeorderapp.HomePage.Data.Coffee
import com.example.coffeeorderapp.R

class CoffeeViewModel : ViewModel() {
    private val _coffees = MutableLiveData<List<Coffee>>()
    val coffees: LiveData<List<Coffee>> get() = _coffees

    init {
        // initial load
        _coffees.value = listOf(
            Coffee("Americano",  R.drawable.americano),
            Coffee("Cappuccino", R.drawable.cappucino),
            Coffee("Mocha",      R.drawable.mocha),
            Coffee("Flat White", R.drawable.flatwhite)
        )
    }
    fun loadCoffees() {
        _coffees.value = listOf(
            Coffee("Americano",  R.drawable.americano),
            Coffee("Cappuccino", R.drawable.cappucino),
            Coffee("Mocha",      R.drawable.mocha),
            Coffee("Flat White", R.drawable.flatwhite)
        )
    }
}