package com.example.coffeeorderapp.HomePage.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoyaltyViewModel : ViewModel() {
    private val _loyaltyProgress = MutableLiveData<LoyaltyProgress>().apply{
        value = LoyaltyProgress(0, 8)
    }
    val loyaltyProgress: LiveData<LoyaltyProgress> = _loyaltyProgress

    data class LoyaltyProgress(
        val current: Int = 4,
        val total: Int = 8
    )

    fun updateLoyaltyProgress(current: Int) {
        _loyaltyProgress.value = LoyaltyProgress(current, 8)
    }
}