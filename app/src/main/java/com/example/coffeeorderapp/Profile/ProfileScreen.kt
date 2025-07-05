package com.example.coffeeorderapp.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeorderapp.Profile.Model.ProfileEntity
import com.example.coffeeorderapp.Profile.ViewModel.ProfileViewModel
import com.example.coffeeorderapp.R

@Composable
fun ProfileScreen(viewModel: ProfileViewModel, onBack: () -> Unit) {
    val profile by viewModel.profile.observeAsState()
    val isEditing by viewModel.isEditing.observeAsState(false)
    val poppinsBold = FontFamily(Font(R.font.poppins_bold))
    val poppinsRegular = FontFamily(Font(R.font.poppins_regular))

    // Local state for editing fields
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var initialized by remember { mutableStateOf(false) }

    // When entering edit mode, copy profile fields to local state
    LaunchedEffect(isEditing, profile) {
        val currentProfile = profile
        if (isEditing && currentProfile != null && !initialized) {
            fullName = currentProfile.fullName
            phoneNumber = currentProfile.phoneNumber
            email = currentProfile.email
            address = currentProfile.address
            initialized = true
        }
        if (!isEditing) {
            initialized = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.left_arrow),
                    contentDescription = "Back",
                    tint = Color(0xFF22313F),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Profile",
                style = TextStyle(
                    fontFamily = poppinsBold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF22313F)
                ),
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        ProfileItem(
            icon = R.drawable.profile,
            label = "Full name",
            value = profile?.fullName ?: "",
            isEditing = isEditing,
            onEditClick = { viewModel.setEditing(true) },
            onValueChange = { fullName = it },
            editableValue = if (isEditing) fullName else null
        )
        ProfileItem(
            icon = R.drawable.group, // Use phone icon if available
            label = "Phone number",
            value = profile?.phoneNumber ?: "",
            isEditing = isEditing,
            onEditClick = { viewModel.setEditing(true) },
            onValueChange = { phoneNumber = it },
            editableValue = if (isEditing) phoneNumber else null
        )
        ProfileItem(
            icon = R.drawable.message, // Use email icon if available
            label = "Email",
            value = profile?.email ?: "",
            isEditing = isEditing,
            onEditClick = { viewModel.setEditing(true) },
            onValueChange = { email = it },
            editableValue = if (isEditing) email else null
        )
        ProfileItem(
            icon = R.drawable.location, // Use location icon if available
            label = "Address",
            value = profile?.address ?: "",
            isEditing = isEditing,
            onEditClick = { viewModel.setEditing(true) },
            onValueChange = { address = it },
            editableValue = if (isEditing) address else null
        )
        if (isEditing) {
            Row(modifier = Modifier.padding(top = 24.dp)) {
                val currentProfile = profile
                Button(onClick = {
                    // Save changes
                    if (currentProfile != null) {
                        viewModel.updateProfile(
                            currentProfile.copy(
                                fullName = fullName,
                                phoneNumber = phoneNumber,
                                email = email,
                                address = address
                            )
                        )
                    }
                }) {
                    Text("Save", fontFamily = poppinsBold)
                }
                Spacer(modifier = Modifier.width(16.dp))
                OutlinedButton(onClick = { viewModel.setEditing(false) }) {
                    Text("Cancel", fontFamily = poppinsRegular)
                }
            }
        }
    }
}

@Composable
fun ProfileItem(
    icon: Int,
    label: String,
    value: String,
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onValueChange: (String) -> Unit,
    editableValue: String?
) {
    val poppinsBold = FontFamily(Font(R.font.poppins_bold))
    val poppinsRegular = FontFamily(Font(R.font.poppins_regular))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFF2F6FA), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = TextStyle(
                    fontFamily = poppinsRegular,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    color = Color(0xFFB2B8C2)
                )
            )
            if (isEditing && editableValue != null) {
                OutlinedTextField(
                    value = editableValue,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        fontFamily = poppinsBold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF22313F)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            } else {
                Text(
                    text = value,
                    style = TextStyle(
                        fontFamily = poppinsBold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF22313F)
                    )
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onEditClick) {
            Icon(
                painter = painterResource(id = R.drawable.edit),
                contentDescription = "Edit $label",
                tint = Color(0xFFB2B8C2),
                modifier = Modifier.size(24.dp)
            )
        }
    }
} 