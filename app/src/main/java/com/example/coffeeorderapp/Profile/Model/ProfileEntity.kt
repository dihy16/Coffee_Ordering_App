package com.example.coffeeorderapp.Profile.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey val id: Int = 1, // Single user profile
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val address: String
) 