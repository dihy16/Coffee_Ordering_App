package com.example.coffeeorderapp.Rewards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeorderapp.R
import com.example.coffeeorderapp.cart.data.ProductEntity

class RedeemProductAdapter(
    private val onRedeemClick: (ProductEntity) -> Unit
) : ListAdapter<ProductEntity, RedeemProductAdapter.RedeemProductViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RedeemProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_redeem_product, parent, false)
        return RedeemProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: RedeemProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class RedeemProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImage: ImageView = itemView.findViewById(R.id.productImage)
        private val productName: TextView = itemView.findViewById(R.id.productName)
        private val validityText: TextView = itemView.findViewById(R.id.validityText)
        private val redeemPointsButton: Button = itemView.findViewById(R.id.redeemPointsButton)

        fun bind(product: ProductEntity) {
            productImage.setImageResource(product.imageResId)
            productName.text = product.name
            validityText.text = "Valid until 04.07.21" // Placeholder, can be dynamic
            val conversionRate = 100 // 1$ = 100 pts
            val points = (product.price * conversionRate).toInt()
            redeemPointsButton.text = "$points pts"
            redeemPointsButton.setOnClickListener {
                onRedeemClick(product)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<ProductEntity>() {
        override fun areItemsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ProductEntity, newItem: ProductEntity): Boolean {
            return oldItem == newItem
        }
    }
} 