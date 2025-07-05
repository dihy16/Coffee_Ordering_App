package com.example.coffeeorderapp.cart

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeorderapp.Details.Customization.CartItem
import com.example.coffeeorderapp.R

class CartAdapter(
    var items: List<CartItem>,
    private val onRemoveItem: ((CartItem) -> Unit)? = null
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coffeeImage: ImageView = itemView.findViewById(R.id.cartCoffeeImage)
        val coffeeName: TextView = itemView.findViewById(R.id.cartCoffeeName)
        val customizations: TextView = itemView.findViewById(R.id.cartCoffeeCustomizations)
        val quantity: TextView = itemView.findViewById(R.id.cartCoffeeQuantity)
        val price: TextView = itemView.findViewById(R.id.cartCoffeePrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = items[position]
        android.util.Log.d("CartAdapter", "Binding item at position $position: ${item.coffee.name}")
        holder.coffeeImage.setImageResource(item.coffee.imageResId)
        holder.coffeeName.text = item.coffee.name
        holder.customizations.text =
            "${item.customization.shot.name.lowercase()} | " +
            "${item.customization.select.name.lowercase()} | " +
            "${item.customization.size.name.lowercase()} | " +
            "${item.customization.ice.name.replace("_", " ").lowercase()}"
        holder.quantity.text = "x${item.customization.quantity}"
        holder.price.text = "$${"%.2f".format(item.totalPrice)}"
        // Optionally, set up swipe-to-remove or a delete button here
        holder.itemView.setOnLongClickListener {
            onRemoveItem?.invoke(item)
            true
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<CartItem>) {
        android.util.Log.d("CartAdapter", "Updating items: ${newItems.size} items")
        items = newItems
        notifyDataSetChanged()
    }
} 