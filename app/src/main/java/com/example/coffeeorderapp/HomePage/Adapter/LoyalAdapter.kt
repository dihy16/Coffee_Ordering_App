package com.example.coffeeorderapp.HomePage.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeorderapp.R

class LoyaltyIconAdapter : RecyclerView.Adapter<LoyaltyIconViewHolder>() {
    private var totalIcons = 8
    private var curNumIcons = 3

    fun updateProgress(filled: Int, total: Int = 8) {
        curNumIcons = filled
        totalIcons = total
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoyaltyIconViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_loyalty_icon, parent, false)
        return LoyaltyIconViewHolder(view)
    }

    override fun onBindViewHolder(holder: LoyaltyIconViewHolder, position: Int) {
        val isFilled = position < curNumIcons
        holder.bind(isFilled)
    }

    override fun getItemCount() = totalIcons
}

class LoyaltyIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val iconImage: ImageView = itemView.findViewById(R.id.iconImage)

    fun bind(isFilled: Boolean) {
        iconImage.setImageResource(
            if (isFilled) R.drawable.loyalty_cup else R.drawable.loyalty_empty
        )
    }
}
