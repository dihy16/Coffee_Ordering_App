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
            // Get the order details before updating status
            val ongoingOrders = orderRepository.getOrdersWithItemsByStatus("ongoing")
            val orderToMove = ongoingOrders.find { it.order.id == orderId }
            
            if (orderToMove != null) {
                // Calculate points based on order total (1 point per $1 spent)
                val pointsToAdd = orderToMove.order.totalPrice.toInt()
                
                orderRepository.updateOrderStatus(orderId, "history")
                // Increment loyalty and points based on actual order total
                val rewardsRepo = RewardsRepository(getApplication())
                rewardsRepo.incrementStampsAndPoints(1, pointsToAdd)
                
                println("DEBUG: Order moved to history - Order ID: $orderId, Total: $${orderToMove.order.totalPrice}, Points added: $pointsToAdd")
            } else {
                println("DEBUG: Order not found for ID: $orderId")
            }
            
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