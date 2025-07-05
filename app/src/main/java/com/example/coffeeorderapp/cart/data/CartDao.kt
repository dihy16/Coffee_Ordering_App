package com.example.coffeeorderapp.cart.data

import androidx.room.*

@Dao
interface CartDao {
    @Query("SELECT * FROM cart")
    suspend fun getAll(): List<CartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartEntity)

    @Delete
    suspend fun delete(item: CartEntity)

    @Query("DELETE FROM cart")
    suspend fun clear()
} 