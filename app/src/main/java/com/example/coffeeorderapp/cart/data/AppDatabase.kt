package com.example.coffeeorderapp.cart.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.coffeeorderapp.Orders.OrderEntity
import com.example.coffeeorderapp.Orders.OrderDao
import com.example.coffeeorderapp.Orders.OrderItemEntity
import com.example.coffeeorderapp.Orders.OrderItemDao
import com.example.coffeeorderapp.Rewards.RewardStatusEntity
import com.example.coffeeorderapp.Rewards.RewardStatusDao
import com.example.coffeeorderapp.cart.data.ProductEntity
import com.example.coffeeorderapp.cart.data.ProductDao

@Database(entities = [CartEntity::class, OrderEntity::class, OrderItemEntity::class, RewardStatusEntity::class, ProductEntity::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
    abstract fun rewardStatusDao(): RewardStatusDao
    abstract fun productDao(): ProductDao
} 