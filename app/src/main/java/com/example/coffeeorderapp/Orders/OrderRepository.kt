package com.example.coffeeorderapp.Orders

import com.example.coffeeorderapp.cart.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(private val db: AppDatabase) {
    private val orderDao = db.orderDao()
    private val orderItemDao = db.orderItemDao()

    suspend fun getOrdersWithItemsByStatus(status: String): List<OrderWithItems> = withContext(Dispatchers.IO) {
        orderDao.getOrdersWithItemsByStatus(status)
    }

    suspend fun addOrderWithItems(order: OrderEntity, items: List<OrderItemEntity>) = withContext(Dispatchers.IO) {
        val orderId = orderDao.insert(order).toInt()
        items.forEach { item ->
            orderItemDao.insert(item.copy(orderId = orderId))
        }
    }

    suspend fun updateOrderStatus(orderId: Int, newStatus: String) = withContext(Dispatchers.IO) {
        orderDao.updateStatus(orderId, newStatus)
    }

    suspend fun deleteOrder(order: OrderEntity) = withContext(Dispatchers.IO) {
        orderDao.delete(order)
    }
} 