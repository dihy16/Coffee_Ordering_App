package com.example.coffeeorderapp.HomePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffeeorderapp.HomePage.Adapter.CoffeeAdapter
import com.example.coffeeorderapp.HomePage.ViewModel.CoffeeViewModel
import com.example.coffeeorderapp.HomePage.ViewModel.LoyaltyViewModel
import com.example.coffeeorderapp.R
import com.example.coffeeorderapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Fragment‑scoped ViewModels
    private val coffeeViewModel by viewModels<CoffeeViewModel>()
    private val loyaltyViewModel by viewModels<LoyaltyViewModel>()

    private lateinit var coffeeAdapter: CoffeeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 1) Loyalty
        loyaltyViewModel.loyaltyProgress.observe(viewLifecycleOwner) { progress ->
            progress?.let {
                binding.loyaltyStatus.loyaltyProgressView
                    .setProgress(it.current, it.total)
                binding.loyaltyStatus.stampCnt.text = getString(R.string.stamp_count, it.current, it.total)
            }
        }

        // 2) Coffee grid
        coffeeAdapter = CoffeeAdapter(emptyList()) { coffee ->
            // Navigate to details fragment with coffee data
            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                coffeeName = coffee.name,
                coffeeImageResId = coffee.imageResId
            )
            findNavController().navigate(action)
        }
        binding.coffeeRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = coffeeAdapter
        }
        coffeeViewModel.coffees.observe(viewLifecycleOwner) {
            coffeeAdapter.updateItems(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
