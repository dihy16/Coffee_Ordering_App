package com.example.coffeeorderapp.Orders.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeeorderapp.Orders.OrderEntity
import com.example.coffeeorderapp.Orders.OrderItemEntity
import com.example.coffeeorderapp.Orders.OrderRepository
import com.example.coffeeorderapp.Orders.OrderWithItems
import com.example.coffeeorderapp.cart.data.DatabaseModule
import com.example.coffeeorderapp.Rewards.RewardsRepository
import kotlinx.coroutines.launch

class OrderViewModel(application: Application) : AndroidViewModel(application) {
    private val orderRepository = DatabaseModule.getOrderRepository(application)

    private val _ongoingOrders = MutableLiveData<List<OrderWithItems>>()
    val ongoingOrders: LiveData<List<OrderWithItems>> = _ongoingOrders

    private val _historyOrders = MutableLiveData<List<OrderWithItems>>()
    val historyOrders: LiveData<List<OrderWithItems>> = _historyOrders

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _ongoingOrders.value = orderRepository.getOrdersWithItemsByStatus("ongoing")
            _historyOrders.value = orderRepository.getOrdersWithItemsByStatus("history")
        }
    }

    fun moveOrderToHistory(orderId: Int) {
        viewModelScope.launch {
            orderRepository.updateOrderStatus(orderId, "history")
            // Increment loyalty and points
            val rewardsRepo = RewardsRepository(getApplication())
            rewardsRepo.incrementStampsAndPoints(1, 12)
            loadOrders()
        }
    }

    fun addOrderWithItems(order: OrderEntity, items: List<OrderItemEntity>) {
        viewModelScope.launch {
            orderRepository.addOrderWithItems(order, items)
            loadOrders()
        }
    }
} 