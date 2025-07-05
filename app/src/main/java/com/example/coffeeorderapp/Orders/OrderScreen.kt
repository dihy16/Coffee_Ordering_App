package com.example.coffeeorderapp.Orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeorderapp.Orders.ViewModel.OrderViewModel

@Composable
fun OrderScreen(viewModel: OrderViewModel) {
    var selectedTab by remember { mutableStateOf(0) }
    val ongoingOrders by viewModel.ongoingOrders.observeAsState(emptyList())
    val historyOrders by viewModel.historyOrders.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "My Order",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TabRow(selectedTabIndex = selectedTab) {
            Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                Text("On going", modifier = Modifier.padding(16.dp))
            }
            Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                Text("History", modifier = Modifier.padding(16.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (selectedTab == 0) {
            OrderList(
                orders = ongoingOrders,
                onOrderCompleted = { orderId -> viewModel.moveOrderToHistory(orderId) },
                isOngoing = true
            )
        } else {
            OrderList(
                orders = historyOrders,
                onOrderCompleted = {},
                isOngoing = false
            )
        }
    }
}

@Composable
fun OrderList(orders: List<OrderWithItems>, onOrderCompleted: (Int) -> Unit, isOngoing: Boolean) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(orders) { orderWithItems ->
            OrderGroupItem(orderWithItems, onOrderCompleted, isOngoing)
            Divider()
        }
    }
}

@Composable
fun OrderGroupItem(orderWithItems: OrderWithItems, onOrderCompleted: (Int) -> Unit, isOngoing: Boolean) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .let {
            if (isOngoing) it.clickable { onOrderCompleted(orderWithItems.order.id) } else it
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(orderWithItems.order.date, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.weight(1f))
            Text("$${"%.2f".format(orderWithItems.order.totalPrice)}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(4.dp))
        orderWithItems.items.forEach { item ->
            Text(item.coffeeName, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(
                text = "x${item.quantity} | ${item.shot} | ${item.select} | ${item.size} | ${item.ice}",
                fontSize = 12.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(2.dp))
        }
        Text(orderWithItems.order.address, fontSize = 12.sp, color = Color.Gray)
        if (isOngoing) {
            Text(
                "Tap to mark as completed",
                fontSize = 11.sp,
                color = Color(0xFF6C7A89),
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
} 