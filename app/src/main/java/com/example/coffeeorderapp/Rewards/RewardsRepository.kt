package com.example.coffeeorderapp.Rewards

import android.content.Context
import com.example.coffeeorderapp.cart.data.DatabaseModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow

class RewardsRepository(context: Context) {
    private val rewardStatusDao = DatabaseModule.getRewardStatusDao(context)

    suspend fun getStatus(): RewardStatusEntity? = withContext(Dispatchers.IO) {
        rewardStatusDao.getStatus()
    }

    suspend fun setStamps(stamps: Int) = withContext(Dispatchers.IO) {
        val current = rewardStatusDao.getStatus() ?: RewardStatusEntity()
        rewardStatusDao.insertOrUpdate(current.copy(loyaltyStamps = stamps))
    }

    suspend fun setPoints(points: Int) = withContext(Dispatchers.IO) {
        val current = rewardStatusDao.getStatus() ?: RewardStatusEntity()
        rewardStatusDao.insertOrUpdate(current.copy(totalPoints = points))
    }

    suspend fun subtractPoints(pointsToSubtract: Int): Boolean = withContext(Dispatchers.IO) {
        val current = rewardStatusDao.getStatus() ?: RewardStatusEntity()
        if (current.totalPoints >= pointsToSubtract) {
            val newPoints = current.totalPoints - pointsToSubtract
            rewardStatusDao.updatePoints(newPoints)
            println("DEBUG: RewardsRepository - Subtracted $pointsToSubtract points. From: ${current.totalPoints}, To: $newPoints")
            true
        } else {
            println("DEBUG: RewardsRepository - Not enough points to subtract. Current: ${current.totalPoints}, Required: $pointsToSubtract")
            false
        }
    }

    suspend fun resetStamps() = withContext(Dispatchers.IO) {
        val current = rewardStatusDao.getStatus() ?: RewardStatusEntity()
        rewardStatusDao.insertOrUpdate(current.copy(loyaltyStamps = 0))
    }

    suspend fun incrementStampsAndPoints(stampInc: Int, pointInc: Int) = withContext(Dispatchers.IO) {
        val current = rewardStatusDao.getStatus() ?: RewardStatusEntity()
        val newStamps = if (current.loyaltyStamps + stampInc >= 8) 0 else current.loyaltyStamps + stampInc
        val newPoints = current.totalPoints + pointInc
        rewardStatusDao.insertOrUpdate(current.copy(loyaltyStamps = newStamps, totalPoints = newPoints))
    }

    fun getStatusFlow(): Flow<RewardStatusEntity?> = rewardStatusDao.getStatusFlow()
} 