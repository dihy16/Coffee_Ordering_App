package com.example.coffeeorderapp.cart.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coffeeorderapp.Orders.OrderEntity
import com.example.coffeeorderapp.Orders.OrderDao
import com.example.coffeeorderapp.Orders.OrderItemEntity
import com.example.coffeeorderapp.Orders.OrderItemDao

@Database(entities = [CartEntity::class, OrderEntity::class, OrderItemEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
} 