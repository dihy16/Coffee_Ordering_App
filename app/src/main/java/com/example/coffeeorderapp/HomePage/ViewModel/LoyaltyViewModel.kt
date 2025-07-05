package com.example.coffeeorderapp.HomePage.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeeorderapp.Rewards.RewardsRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoyaltyViewModel(application: Application) : AndroidViewModel(application) {
    private val rewardsRepository = RewardsRepository(application)
    private val _loyaltyProgress = MutableLiveData<LoyaltyProgress>()
    val loyaltyProgress: LiveData<LoyaltyProgress> = _loyaltyProgress

    init {
        observeLoyaltyProgress()
    }

    private fun observeLoyaltyProgress() {
        viewModelScope.launch {
            rewardsRepository.getStatusFlow().collectLatest { status ->
                val safeStatus = status ?: com.example.coffeeorderapp.Rewards.RewardStatusEntity()
                _loyaltyProgress.postValue(LoyaltyProgress(safeStatus.loyaltyStamps, 8))
            }
        }
    }

    data class LoyaltyProgress(
        val current: Int = 0,
        val total: Int = 8
    )
}