package com.example.coffeeorderapp.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeorderapp.Details.Customization.CartItem
import com.example.coffeeorderapp.R

private val poppinsRegular = FontFamily(Font(R.font.poppins_regular))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<CartItem>,
    totalPrice: Double,
    onRemoveItem: (CartItem) -> Unit,
    onCheckout: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart", fontFamily = poppinsRegular) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painterResource(R.drawable.left_arrow),
                            contentDescription = "Back",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Total Price",
                        color = colorResource(R.color.coffee_grey),
                        fontFamily = poppinsRegular,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$${"%.2f".format(totalPrice)}",
                        color = colorResource(R.color.grey_navy),
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppinsRegular,
                        fontSize = 24.sp
                    )
                }
                Button(
                    onClick = onCheckout,
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.grey_navy)),
                    modifier = Modifier.height(56.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.buy),
                        contentDescription = "Checkout",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Checkout", color = Color.White, fontFamily = poppinsRegular)
                }
            }
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(cartItems, key = { it.hashCode() }) { item ->
                var swiped by remember { mutableStateOf(false) }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(item) {
                            detectHorizontalDragGestures { change, dragAmount ->
                                if (dragAmount < -50) {
                                    swiped = true
                                }
                            }
                        }
                ) {
                    if (swiped) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFFFEAEA)),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            IconButton(onClick = { onRemoveItem(item) }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_delete),
                                    contentDescription = "Delete",
                                    tint = Color(0xFFFF6B6B),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                    CartItemRow(item = item)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.detail_bg))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(item.coffee.imageResId),
            contentDescription = item.coffee.name,
            modifier = Modifier.size(64.dp),
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.coffee.name,
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsRegular,
                fontSize = 16.sp,
                color = colorResource(R.color.text_detail_color)
            )
            Text(
                text = "${item.customization.shot.name.lowercase()} | ${item.customization.select.name.lowercase()} | ${item.customization.size.name.lowercase()} | ${item.customization.ice.name.replace("_", " ").lowercase()}",
                fontSize = 12.sp,
                color = colorResource(R.color.coffee_grey),
                fontFamily = poppinsRegular
            )
            Text(
                text = "x${item.customization.quantity}",
                fontSize = 12.sp,
                color = colorResource(R.color.coffee_grey),
                fontFamily = poppinsRegular
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = "$${"%.2f".format(item.totalPrice)}",
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsRegular,
            fontSize = 16.sp,
            color = colorResource(R.color.grey_navy)
        )
    }
} 