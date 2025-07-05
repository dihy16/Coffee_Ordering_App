package com.example.coffeeorderapp.Orders

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val address: String,
    val status: String, // "ongoing" or "history"
    val totalPrice: Double
) 