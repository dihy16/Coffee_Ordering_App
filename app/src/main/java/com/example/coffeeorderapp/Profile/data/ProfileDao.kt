package com.example.coffeeorderapp.Profile.data

import androidx.room.*
import com.example.coffeeorderapp.Profile.Model.ProfileEntity

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile WHERE id = 1 LIMIT 1")
    suspend fun getProfile(): ProfileEntity?

    @Query("SELECT COUNT(*) FROM profile")
    suspend fun getProfileCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: ProfileEntity)

    @Update
    suspend fun update(profile: ProfileEntity)

    @Query("DELETE FROM profile")
    suspend fun deleteAll()
} 