package com.example.coffeeorderapp.Profile.data

import com.example.coffeeorderapp.Profile.Model.ProfileEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(private val profileDao: ProfileDao) {
    suspend fun getProfile(): ProfileEntity? = withContext(Dispatchers.IO) {
        val count = profileDao.getProfileCount()
        println("DEBUG: ProfileRepository - Total profiles in database: $count")
        val profile = profileDao.getProfile()
        println("DEBUG: ProfileRepository.getProfile() returned: $profile")
        profile
    }

    suspend fun updateProfile(profile: ProfileEntity) = withContext(Dispatchers.IO) {
        profileDao.update(profile)
    }

    suspend fun insertProfile(profile: ProfileEntity) = withContext(Dispatchers.IO) {
        profileDao.insert(profile)
    }
} 