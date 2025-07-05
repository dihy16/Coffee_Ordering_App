package com.example.coffeeorderapp.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.coffeeorderapp.R
import com.example.coffeeorderapp.MainActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.ImageButton
import com.example.coffeeorderapp.cart.ViewModel.CartViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import android.widget.LinearLayout
import androidx.compose.ui.platform.ComposeView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.res.stringResource
import com.example.coffeeorderapp.Orders.ViewModel.OrderViewModel
import com.example.coffeeorderapp.Orders.OrderEntity
import com.example.coffeeorderapp.Orders.OrderItemEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.coffeeorderapp.Profile.ViewModel.ProfileViewModel

private val poppinsRegular = FontFamily(Font(R.font.poppins_regular))

class CartFragment : Fragment(R.layout.fragment_cart) {
    private val viewModel: CartViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()
    private val profileViewModel: ProfileViewModel by activityViewModels()
    private lateinit var cartAdapter: CartAdapter
    private var pendingOrderAdded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Setup back button
        val backButton = view.findViewById<ImageButton>(R.id.cartBackButton)
        backButton?.setOnClickListener {
            android.util.Log.d("CartFragment", "Back button clicked")
            findNavController().navigateUp()
        }
        
        val recyclerView = view.findViewById<RecyclerView>(R.id.cartRecyclerView)
        cartAdapter = CartAdapter(emptyList()) { item ->
            viewModel.removeItem(item)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = cartAdapter

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                android.util.Log.d("CartFragment", "Swiped item at position: $position")
                if (position != RecyclerView.NO_POSITION && position < cartAdapter.items.size) {
                    val item = cartAdapter.items[position]
                    android.util.Log.d("CartFragment", "Removing item: ${item.coffee.name}")
                    viewModel.removeItem(item)
                } else {
                    android.util.Log.e("CartFragment", "Invalid position: $position, items size: ${cartAdapter.items.size}")
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {
                    val itemView = viewHolder.itemView
                    val paint = Paint()
                    val icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_delete)
                    val iconMargin = (itemView.height - 64) / 2 // icon size 32dp
                    val iconTop = itemView.top + (itemView.height - 64) / 2
                    val iconBottom = iconTop + 64
                    val iconLeft = itemView.right - 64 - iconMargin
                    val iconRight = itemView.right - iconMargin

                    // Draw rounded red background
                    paint.color = 0xFFFFEAEA.toInt()
                    val background = RectF(
                        itemView.right.toFloat() + dX,
                        itemView.top.toFloat() + 8,
                        itemView.right.toFloat() - 8,
                        itemView.bottom.toFloat() - 8
                    )
                    c.drawRoundRect(background, 32f, 32f, paint)

                    // Draw delete icon
                    icon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                    icon?.draw(c)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        val emptyCompose = view.findViewById<ComposeView>(R.id.cartEmptyCompose)
        viewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.updateItems(items)
            if (items.isEmpty()) {
                emptyCompose.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
                emptyCompose.setContent {
                    CartEmptyState()
                }
            } else {
                emptyCompose.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        // Update total price
        val totalPriceView = view.findViewById<TextView>(R.id.cartTotalPrice)
        viewModel.totalPrice.observe(viewLifecycleOwner) { price ->
            android.util.Log.d("CartFragment", "Total price updated: $price")
            totalPriceView?.text = "$${"%.2f".format(price)}"
        }

        orderViewModel.ongoingOrders.observe(viewLifecycleOwner) { orders ->
            if (pendingOrderAdded && orders.isNotEmpty()) {
                findNavController().navigate(R.id.action_cartFragment_to_orderSuccessFragment)
                pendingOrderAdded = false
            }
        }

        val bottomLayout = view.findViewById<LinearLayout>(R.id.cartBottomLayout)
        val composeFAB = ComposeView(requireContext()).apply {
            setContent {
                val totalPrice by viewModel.totalPrice.observeAsState(0.0)
                BottomBarWithCheckout(
                    totalPrice = totalPrice,
                    onCheckout = {
                        val cartItems = viewModel.cartItems.value ?: emptyList()
                        val profile = profileViewModel.profile.value
                        if (cartItems.isNotEmpty() && profile != null) {
                            val order = OrderEntity(
                                date = getCurrentDateTimeString(),
                                address = profile.address,
                                status = "ongoing",
                                totalPrice = cartItems.sumOf { it.totalPrice }
                            )
                            val orderItems = cartItems.map { item ->
                                OrderItemEntity(
                                    orderId = 0, // will be set in repository
                                    coffeeName = item.coffee.name,
                                    imageResId = item.coffee.imageResId,
                                    price = item.totalPrice,
                                    quantity = item.customization.quantity,
                                    shot = item.customization.shot.name,
                                    select = item.customization.select.name,
                                    size = item.customization.size.name,
                                    ice = item.customization.ice.name
                                )
                            }
                            orderViewModel.addOrderWithItems(order, orderItems)
                            viewModel.clearCart()
                            pendingOrderAdded = true
                        }
                        // Do NOT navigate here!
                    }
                )
            }
        }
        bottomLayout.removeAllViews()
        bottomLayout.addView(composeFAB)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideBottomNav()
        viewModel.refreshCart()
    }

    override fun onPause() {
        (activity as? MainActivity)?.showBottomNav()
        super.onPause()
    }

    // Helper function to get current date and time string
    private fun getCurrentDateTimeString(): String {
        val sdf = SimpleDateFormat("dd MMMM | HH:mm a", Locale.getDefault())
        return sdf.format(Date())
    }
}

@Composable
fun BottomBarWithCheckout(totalPrice: Double, onCheckout: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 32.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
                .padding(start = 8.dp)
        ) {
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
        Spacer(modifier = Modifier.width(16.dp))
        ExtendedFloatingActionButton(
            text = { Text("Checkout", color = Color.White) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.buy),
                    contentDescription = "Checkout",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            },
            onClick = onCheckout,
            containerColor = colorResource(R.color.grey_navy),
            modifier = Modifier.height(56.dp)
                .padding(start = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(32.dp)
        )
    }
}

@Composable
fun CartEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.buy), // Use your cart icon if available
            contentDescription = "Empty Cart",
            tint = colorResource(R.color.grey_navy),
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your cart is empty!",
            color = colorResource(R.color.grey_navy),
            fontFamily = poppinsRegular,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.cart_empty),
            color = colorResource(R.color.coffee_grey),
            fontFamily = poppinsRegular,
            fontSize = 14.sp
        )
    }
} 