package com.example.coffeeorderapp.Rewards

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reward_status")
data class RewardStatusEntity(
    @PrimaryKey val id: Int = 1, // Always 1 row
    val loyaltyStamps: Int = 0,  // 0-8
    val totalPoints: Int = 0     // Accumulated points
) 