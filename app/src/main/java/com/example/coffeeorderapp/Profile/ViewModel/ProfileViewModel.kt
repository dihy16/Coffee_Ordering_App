package com.example.coffeeorderapp.Profile.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeeorderapp.Profile.Model.ProfileEntity
import com.example.coffeeorderapp.Profile.data.ProfileRepository
import com.example.coffeeorderapp.cart.data.DatabaseModule
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val profileRepository = DatabaseModule.getProfileRepository(application)

    private val _profile = MutableLiveData<ProfileEntity?>()
    val profile: LiveData<ProfileEntity?> = _profile

    private val _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean> = _isEditing

    init {
        loadProfile()
    }

    fun refreshProfile() {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            val profile = profileRepository.getProfile()
            println("DEBUG: ProfileViewModel loaded profile: $profile")
            _profile.value = profile
        }
    }

    fun setEditing(editing: Boolean) {
        _isEditing.value = editing
    }

    fun updateProfile(profile: ProfileEntity) {
        viewModelScope.launch {
            profileRepository.updateProfile(profile)
            _profile.value = profile
            setEditing(false)
        }
    }

    fun insertProfile(profile: ProfileEntity) {
        viewModelScope.launch {
            profileRepository.insertProfile(profile)
            _profile.value = profile
        }
    }
} 