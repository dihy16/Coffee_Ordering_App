package com.example.coffeeorderapp.Orders

import androidx.room.*

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE status = :status ORDER BY date DESC")
    suspend fun getByStatus(status: String): List<OrderEntity>

    @Query("SELECT * FROM orders ORDER BY date DESC")
    suspend fun getAll(): List<OrderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity): Long

    @Update
    suspend fun update(order: OrderEntity)

    @Query("UPDATE orders SET status = :newStatus WHERE id = :orderId")
    suspend fun updateStatus(orderId: Int, newStatus: String)

    @Delete
    suspend fun delete(order: OrderEntity)

    @Transaction
    @Query("SELECT * FROM orders WHERE status = :status ORDER BY date DESC")
    suspend fun getOrdersWithItemsByStatus(status: String): List<OrderWithItems>

    @Transaction
    @Query("SELECT * FROM orders ORDER BY date DESC")
    suspend fun getAllOrdersWithItems(): List<OrderWithItems>
    
    @Query("DELETE FROM orders")
    suspend fun deleteAll()
} 