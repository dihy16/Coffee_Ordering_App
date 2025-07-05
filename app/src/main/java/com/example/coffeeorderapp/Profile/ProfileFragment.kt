package com.example.coffeeorderapp.Profile

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.coffeeorderapp.R
import com.example.coffeeorderapp.Profile.ViewModel.ProfileViewModel
import androidx.navigation.fragment.findNavController

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? com.example.coffeeorderapp.MainActivity)?.hideBottomNav()
        val composeView = ComposeView(requireContext())
        composeView.setContent {
            ProfileScreen(viewModel) {
                findNavController().popBackStack()
            }
        }
        (view as? android.widget.FrameLayout)?.removeAllViews()
        (view as? android.widget.FrameLayout)?.addView(composeView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? com.example.coffeeorderapp.MainActivity)?.showBottomNav()
    }
} 