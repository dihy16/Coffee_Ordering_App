package com.example.coffeeorderapp.Rewards

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeeorderapp.Orders.OrderRepository
import com.example.coffeeorderapp.Orders.OrderWithItems
import com.example.coffeeorderapp.Rewards.RewardsRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.delay

class RewardsViewModel(application: Application) : AndroidViewModel(application) {
    private val rewardsRepository = RewardsRepository(application)
    private val orderRepository = OrderRepository(
        com.example.coffeeorderapp.cart.data.DatabaseModule.getDatabase(application)
    )

    private val _loyaltyStamps = MutableLiveData<Int>()
    val loyaltyStamps: LiveData<Int> = _loyaltyStamps

    private val _totalPoints = MutableLiveData<Int>()
    val totalPoints: LiveData<Int> = _totalPoints

    private val _rewardHistory = MutableLiveData<List<RewardHistoryItem>>()
    val rewardHistory: LiveData<List<RewardHistoryItem>> = _rewardHistory



    init {
        observeStatus()
        observeRewardHistory()
    }

    private fun observeStatus() {
        viewModelScope.launch {
            rewardsRepository.getStatusFlow().collectLatest { status ->
                val safeStatus = status ?: com.example.coffeeorderapp.Rewards.RewardStatusEntity()
                _loyaltyStamps.postValue(safeStatus.loyaltyStamps)
                _totalPoints.postValue(safeStatus.totalPoints)
            }
        }
    }

    fun incrementStampsAndPoints(stampInc: Int, pointInc: Int) {
        viewModelScope.launch {
            rewardsRepository.incrementStampsAndPoints(stampInc, pointInc)
        }
    }

    fun resetStamps() {
        viewModelScope.launch {
            rewardsRepository.resetStamps()
        }
    }

    // Call this when a new order is completed to add points
    fun addPointsFromOrder(orderTotal: Double) {
        viewModelScope.launch {
            val pointsToAdd = calculatePoints(orderTotal)
            val currentPoints = _totalPoints.value ?: 0
            val newPoints = currentPoints + pointsToAdd
            println("DEBUG: Adding $pointsToAdd points from order ($$orderTotal). From: $currentPoints, To: $newPoints")
            rewardsRepository.setPoints(newPoints)
        }
    }

    private fun observeRewardHistory() {
        viewModelScope.launch {
            while (true) {
                val orders = orderRepository.getOrdersWithItemsByStatus("history")
                val history = orders.map { orderWithItems ->
                    val itemNames = orderWithItems.items.map { it.coffeeName }
                    val displayName = when {
                        itemNames.isEmpty() -> "Order"
                        itemNames.size <= 3 -> itemNames.joinToString(", ")
                        else -> itemNames.take(3).joinToString(", ") + " + ${itemNames.size - 3} more"
                    }
                    RewardHistoryItem(
                        coffeeName = displayName,
                        date = orderWithItems.order.date,
                        points = calculatePoints(orderWithItems.order.totalPrice)
                    )
                }.sortedByDescending { it.date }
                _rewardHistory.postValue(history)
                
                kotlinx.coroutines.delay(500)
            }
        }
    }



    private fun calculatePoints(totalPrice: Double): Int {
        // 1 point per $1 spent, rounded down
        return totalPrice.toInt()
    }

    // TODO: Remove this after debug
    fun setStampsForDebug(stamps: Int) {
        viewModelScope.launch {
            rewardsRepository.setStamps(stamps)
        }
    }

    fun setPointsForDebug(points: Int) {
        viewModelScope.launch {
            rewardsRepository.setPoints(points)
        }
    }

    fun redeemWithPoints(pointsToSubtract: Int, onResult: (Boolean) -> Unit) {
        val currentPoints = _totalPoints.value ?: 0
        println("DEBUG: Redeem attempt - Current points: $currentPoints, Required: $pointsToSubtract")
        
        if (currentPoints >= pointsToSubtract) {
            viewModelScope.launch {
                val success = rewardsRepository.subtractPoints(pointsToSubtract)
                if (success) {
                    val newPoints = currentPoints - pointsToSubtract
                    _totalPoints.postValue(newPoints)
                    println("DEBUG: Redeem successful - Updated UI to $newPoints points")
                    onResult(true)
                } else {
                    println("DEBUG: Redeem failed in repository")
                    onResult(false)
                }
            }
        } else {
            println("DEBUG: Not enough points - Current: $currentPoints, Required: $pointsToSubtract")
            onResult(false)
        }
    }



    data class RewardHistoryItem(
        val coffeeName: String,
        val date: String,
        val points: Int
    )
} 