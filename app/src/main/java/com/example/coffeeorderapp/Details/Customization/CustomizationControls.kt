package com.example.coffeeorderapp.Details.Customization

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeorderapp.HomePage.Data.Coffee
import com.example.coffeeorderapp.R


private val poppinsRegular = FontFamily(Font(R.font.poppins_regular))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeDetailScreen(
    coffee: Coffee,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    shot: ShotType,
    onShotChange: (ShotType) -> Unit,
    select: SelectType,
    onSelectChange: (SelectType) -> Unit,
    size: SizeType,
    onSizeChange: (SizeType) -> Unit,
    ice: IceAmount,
    onIceChange: (IceAmount) -> Unit,
    calculatedPrice: Double,
    onBack: () -> Unit,
    onAddToCart: () -> Unit,
    onCartClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Details", fontFamily = poppinsRegular) },
                navigationIcon = {
                    IconButton(onClick = onBack,
                        modifier = Modifier.padding(start = 16.dp)
                        ) {
                        Icon(
                            painterResource(R.drawable.left_arrow), 
                            contentDescription = "Back",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick,
                        modifier = Modifier.padding(end = 16.dp)
                        ) {
                        Icon(
                            painter = painterResource(R.drawable.buy),
                            contentDescription = "Cart",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Button(
                onClick = onAddToCart,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp, vertical = 32.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.grey_navy))
            ) {
                Text("Add to Cart", color = Color.White, fontFamily = poppinsRegular)
            }
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(horizontal = 32.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            // Image with background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(R.color.detail_bg))
                    .padding(32.dp)
            ) {
                Image(
                    painter = painterResource(coffee.imageResId),
                    contentDescription = coffee.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(Modifier.height(24.dp))
            // 1. Name + quantity in one row
            OptionRow(label = coffee.name) {
                QuantitySelector(quantity, onQuantityChange)
            }
            HorizontalDivider(
                color = colorResource(R.color.divider),
                thickness = 2.dp
            )
            // 2. Shot row
            Spacer(Modifier.height(24.dp))
            OptionRow(label = "Shot") {
                ShotSelector(shot, onShotChange)
            }
            HorizontalDivider(
                color = colorResource(R.color.divider),
                thickness = 2.dp
            )
            // 3. Select (Hot/Cold)
            Spacer(Modifier.height(24.dp))
            OptionRow(label = "Select") {
                SelectTypeSelector(select, onSelectChange)
            }
            HorizontalDivider(
                color = colorResource(R.color.divider),
                thickness = 2.dp
            )
            // 4. Size
            Spacer(Modifier.height(24.dp))
            OptionRow(label = "Size") {
                SizeSelector(size, onSizeChange)
            }
            HorizontalDivider(
                color = colorResource(R.color.divider),
                thickness = 2.dp
            )
            // 5. Ice
            Spacer(Modifier.height(24.dp))
            OptionRow(label = "Ice") {
                IceSelector(ice, onIceChange)
            }

            // 6. Total Amount
            Spacer(Modifier.height(32.dp))
            OptionRow(label = "Total Amount") {
                Text(
                    text = "$${"%.2f".format(calculatedPrice)}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsRegular
                )
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun OptionRow(label: String, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            fontFamily = poppinsRegular
        )
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

@Composable
fun CartPreviewSheet(
    cartItems: List<CartItem>,
    totalPrice: Double,
    onViewCart: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Cart Preview",
            fontWeight = FontWeight.Bold,
            fontFamily = poppinsRegular,
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Divider()
        if (cartItems.isEmpty()) {
            Text(
                text = "Your cart is empty.",
                fontFamily = poppinsRegular,
                color = colorResource(R.color.coffee_grey),
                modifier = Modifier.padding(vertical = 24.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .padding(vertical = 8.dp)
            ) {
                cartItems.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.coffee.name,
                                fontWeight = FontWeight.Bold,
                                fontFamily = poppinsRegular,
                                fontSize = 16.sp
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
                    Divider()
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsRegular,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$${"%.2f".format(totalPrice)}",
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsRegular,
                    fontSize = 18.sp,
                    color = colorResource(R.color.grey_navy)
                )
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onViewCart,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.grey_navy))
            ) {
                Text("View Full Cart", color = Color.White, fontFamily = poppinsRegular)
            }
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close", fontFamily = poppinsRegular)
            }
        }
    }
}
