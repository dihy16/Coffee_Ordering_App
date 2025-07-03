package com.example.coffeeorderapp.HomePage.View

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeorderapp.HomePage.Adapter.LoyaltyIconAdapter

class LoyaltyProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val adapter = LoyaltyIconAdapter()

    init {
        val recyclerView = RecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = this@LoyaltyProgressView.adapter
        }
        addView(recyclerView)
    }

    fun setProgress(current: Int, total: Int) {
        adapter.updateProgress(current, total)
    }
}
