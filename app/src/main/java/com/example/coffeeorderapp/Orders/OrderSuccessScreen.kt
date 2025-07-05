package com.example.coffeeorderapp.Orders

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeorderapp.R

@Composable
fun OrderSuccessScreen(onTrackOrder: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(R.drawable.order_success), // You need to add this drawable
            contentDescription = "Order Success",
            modifier = Modifier.size(180.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Order Success",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Your order has been placed successfully.\nFor more details, go to my orders.",
            color = Color.Gray,
            fontSize = 15.sp,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 8.dp),
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = onTrackOrder,
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF39505C)),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Track My Order", color = Color.White, fontSize = 16.sp)
        }
    }
} 