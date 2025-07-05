package com.example.coffeeorderapp.Orders

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.coffeeorderapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class OrderSuccessFragment : Fragment(R.layout.fragment_order_success) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val composeView = ComposeView(requireContext())
        composeView.setContent {
            OrderSuccessScreen(onTrackOrder = {
                val navController = findNavController()
                navController.popBackStack(navController.graph.startDestinationId, false)
                val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)
                bottomNav.selectedItemId = R.id.ordersFragment
            })
        }
        (view as? android.widget.FrameLayout)?.removeAllViews()
        (view as? android.widget.FrameLayout)?.addView(composeView)
    }
} 