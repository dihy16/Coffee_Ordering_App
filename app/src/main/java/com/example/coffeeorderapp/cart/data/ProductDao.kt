package com.example.coffeeorderapp.cart.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM products ORDER BY price ASC LIMIT 3")
    suspend fun getThreeCheapestProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)
} 