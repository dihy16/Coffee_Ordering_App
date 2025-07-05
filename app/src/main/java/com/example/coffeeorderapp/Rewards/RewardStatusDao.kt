package com.example.coffeeorderapp.Rewards

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardStatusDao {
    @Query("SELECT * FROM reward_status WHERE id = 1")
    suspend fun getStatus(): RewardStatusEntity?

    @Query("SELECT * FROM reward_status WHERE id = 1")
    fun getStatusFlow(): Flow<RewardStatusEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(status: RewardStatusEntity)

    @Query("UPDATE reward_status SET loyaltyStamps = :stamps WHERE id = 1")
    suspend fun updateStamps(stamps: Int)

    @Query("UPDATE reward_status SET totalPoints = :points WHERE id = 1")
    suspend fun updatePoints(points: Int)

    @Query("UPDATE reward_status SET loyaltyStamps = 0 WHERE id = 1")
    suspend fun resetStamps()
} 