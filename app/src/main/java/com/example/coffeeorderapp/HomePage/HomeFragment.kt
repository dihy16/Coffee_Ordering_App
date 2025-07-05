package com.example.coffeeorderapp.HomePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coffeeorderapp.HomePage.Adapter.CoffeeAdapter
import com.example.coffeeorderapp.HomePage.ViewModel.CoffeeViewModel
import com.example.coffeeorderapp.HomePage.ViewModel.LoyaltyViewModel
import com.example.coffeeorderapp.R
import com.example.coffeeorderapp.databinding.FragmentHomeBinding
import com.example.coffeeorderapp.cart.data.DatabaseModule
import androidx.lifecycle.ViewModelProvider
import com.example.coffeeorderapp.Profile.ViewModel.ProfileViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Fragment‑scoped ViewModels
    private val coffeeViewModel: CoffeeViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val repository = DatabaseModule.getProductRepository(requireContext().applicationContext)
                @Suppress("UNCHECKED_CAST")
                return CoffeeViewModel(repository) as T
            }
        }
    }
    private val loyaltyViewModel by viewModels<LoyaltyViewModel>()
    private val profileViewModel: ProfileViewModel by activityViewModels()

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

        // Profile icon click
        binding.header.root.findViewById<View>(R.id.iconProfile)?.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }

        // Observe profile name and update header
        profileViewModel.profile.observe(viewLifecycleOwner) { profile ->
            val name = profile?.fullName ?: ""
            binding.header.root.findViewById<TextView>(R.id.tvName)?.text = name
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
            println("DEBUG: HomeFragment received ${it.size} coffees: ${it.map { coffee -> coffee.name }}")
            coffeeAdapter.updateItems(it)
        }
        
        // Load coffees with a slight delay to ensure database is ready
        view.post {
            coffeeViewModel.loadCoffees()
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data when fragment becomes visible
        coffeeViewModel.loadCoffees()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
