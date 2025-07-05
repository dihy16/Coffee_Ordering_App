package com.example.coffeeorderapp.cart.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart")
data class CartEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coffeeName: String,
    val imageResId: Int,
    val price: Double,
    val quantity: Int,
    val shot: String,
    val select: String,
    val size: String,
    val ice: String
) 