package com.example.coffeeorderapp.HomePage.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeorderapp.cart.data.ProductEntity
import com.example.coffeeorderapp.R

class CoffeeAdapter(
    private var items: List<ProductEntity>,
    private val onItemClick: (ProductEntity) -> Unit
) : RecyclerView.Adapter<CoffeeAdapter.CoffeeViewHolder>() {

    class CoffeeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.coffeeName)
        val imageView: ImageView = itemView.findViewById(R.id.coffeeImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.coffee_item, parent, false)
        return CoffeeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoffeeViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name
        holder.imageView.setImageResource(item.imageResId)
        
        // Set click listener for the entire item
        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<ProductEntity>) {
        items = newItems
        notifyDataSetChanged()
    }
}
