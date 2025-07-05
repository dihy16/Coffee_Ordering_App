package com.example.coffeeorderapp.Rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeorderapp.R
import com.example.coffeeorderapp.cart.data.DatabaseModule
import com.example.coffeeorderapp.cart.data.ProductEntity
import com.example.coffeeorderapp.cart.data.ProductRepository

class RedeemFragment : Fragment() {
    private lateinit var viewModel: RedeemViewModel
    private lateinit var adapter: RedeemProductAdapter
    private val rewardsViewModel by viewModels<RewardsViewModel>({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_redeem, container, false)
        val context = requireContext().applicationContext
        val repository = DatabaseModule.getProductRepository(context)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RedeemViewModel(repository) as T
            }
        })[RedeemViewModel::class.java]

        val recyclerView = view.findViewById<RecyclerView>(R.id.redeemRecyclerView)
        adapter = RedeemProductAdapter { product ->
            redeemProductWithPoints(product)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner) { products ->
            println("DEBUG: RedeemFragment received ${products.size} products: ${products.map { it.name }}")
            adapter.submitList(products)
        }

        // Load products with a slight delay to ensure database is ready
        view.post {
            viewModel.loadProducts()
        }

        // Back button
        view.findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Hide nav bar
        requireActivity().findViewById<View>(R.id.bottomNav)?.visibility = View.GONE

        return view
    }

    private fun redeemProductWithPoints(product: ProductEntity) {
        val conversionRate = 100 // 1$ = 100 pts
        val requiredPoints = (product.price * conversionRate).toInt()
        val currentPoints = rewardsViewModel.totalPoints.value ?: 0
        if (currentPoints >= requiredPoints) {
            rewardsViewModel.setPointsForDebug(currentPoints - requiredPoints)
            Toast.makeText(requireContext(), "Redeemed successfully!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Not enough points!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when fragment becomes visible
        viewModel.loadProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Restore nav bar
        requireActivity().findViewById<View>(R.id.bottomNav)?.visibility = View.VISIBLE
    }
} 