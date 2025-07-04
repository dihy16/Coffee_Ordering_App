package com.example.coffeeorderapp.Details.Customization

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coffeeorderapp.R

@Composable
fun QuantitySelector(quantity: Int, onQuantityChange: (Int) -> Unit) {
    // 1) Container only as wide as its content
    OutlinedButton(
        onClick = { /* no-op */ },
        enabled = false,
        shape = RoundedCornerShape(50),
        border = BorderStroke(2.dp, Color.LightGray),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .height(32.dp)
            .wrapContentWidth()      // <- no fixed width
    ) {
        // 2) Center items tightly
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentWidth()
        ) {
            // 3) Small padding on each button
            TextButton(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
            ) {
                Text("–", color = Color.DarkGray, fontSize = 14.sp)
            }

            Text(
                text = quantity.toString(),
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(horizontal = 6.dp)
            )

            TextButton(
                onClick = { onQuantityChange(quantity + 1) },
                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
            ) {
                Text("+", color = Color.DarkGray, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun ShotSelector(shot: ShotType, onShotChange: (ShotType) -> Unit) {
    Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)) {
        OutlinedButton(
            onClick = { onShotChange(ShotType.Single) },
            border = androidx.compose.foundation.BorderStroke(
                width = 0.5.dp,
                color = if (shot == ShotType.Single) Color.DarkGray else Color.LightGray
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text("Single", color = colorResource(R.color.grey_navy))
        }

        OutlinedButton(
            onClick = { onShotChange(ShotType.Double) },
            border = androidx.compose.foundation.BorderStroke(
                width = 0.5.dp,
                color = if (shot == ShotType.Double) Color.DarkGray else Color.LightGray
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text("Double", color = colorResource(R.color.grey_navy))
        }
    }
}

@Composable
fun SelectTypeSelector(select: SelectType, onSelectChange: (SelectType) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(onClick = { onSelectChange(SelectType.Hot) }) {
            Icon(
                painter = painterResource(id = R.drawable.select_hot),
                modifier = Modifier.size(32.dp),
                contentDescription = "Hot",
                tint = if (select == SelectType.Hot) Color.DarkGray else Color.LightGray
            )
        }
        IconButton(onClick = { onSelectChange(SelectType.Cold) }) {
            Icon(
                painter = painterResource(id = R.drawable.select_cold),
                modifier = Modifier.size(32.dp),
                contentDescription = "Cold",
                tint = if (select == SelectType.Cold) Color.DarkGray else Color.LightGray
            )
        }
    }
}

@Composable
fun SizeSelector(size: SizeType, onSizeChange: (SizeType) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(onClick = { onSizeChange(SizeType.Small) }) {
            Icon(
                painter = painterResource(id = R.drawable.small_cup),
                modifier = Modifier.size(16.dp),
                contentDescription = "Small",
                tint = if (size == SizeType.Small) Color.DarkGray else Color.LightGray
            )
        }
        IconButton(onClick = { onSizeChange(SizeType.Medium) }) {
            Icon(
                painter = painterResource(id = R.drawable.medium_cup),
                modifier = Modifier.size(24.dp),
                contentDescription = "Medium",
                tint = if (size == SizeType.Medium) Color.DarkGray else Color.LightGray
            )
        }
        IconButton(onClick = { onSizeChange(SizeType.Large) }) {
            Icon(
                painter = painterResource(id = R.drawable.big_cup),
                modifier = Modifier.size(32.dp),
                contentDescription = "Large",
                tint = if (size == SizeType.Large) Color.DarkGray else Color.LightGray
            )
        }
    }
}

@Composable
fun IceSelector(ice: IceAmount, onIceChange: (IceAmount) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        IconButton(onClick = { onIceChange(IceAmount.None) }) {
            Icon(
                painter = painterResource(id = R.drawable.little_ice),
                modifier = Modifier.size(16.dp),
                contentDescription = "No Ice",
                tint = if (ice == IceAmount.None) Color.DarkGray else Color.LightGray
            )
        }
        IconButton(onClick = { onIceChange(IceAmount.Some) }) {
            Icon(
                painter = painterResource(id = R.drawable.medium_ice),
                modifier = Modifier.size(32.dp),
                contentDescription = "Some Ice",
                tint = if (ice == IceAmount.Some) Color.DarkGray else Color.LightGray
            )
        }
        IconButton(onClick = { onIceChange(IceAmount.A_Lot) }) {
            Icon(
                painter = painterResource(id = R.drawable.much_ice),
                modifier = Modifier.size(32.dp),
                contentDescription = "A Lot of Ice",
                tint = if (ice == IceAmount.A_Lot) Color.DarkGray else Color.LightGray
            )
        }
    }
}