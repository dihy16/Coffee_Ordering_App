package com.example.coffeeorderapp.Details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.coffeeorderapp.HomePage.Data.Coffee
import com.example.coffeeorderapp.MainActivity
import com.example.coffeeorderapp.R
import com.example.coffeeorderapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNav()

        // Set coffee data in ViewModel from navigation args
        viewModel.setCoffee(Coffee(args.coffeeName, args.coffeeImageResId))

        // Observe coffee LiveData
        viewModel.coffee.observe(viewLifecycleOwner) { coffee ->
            binding.coffeeImage.setImageResource(coffee.imageResId)
            binding.coffeeName.text = coffee.name
            binding.coffeeDescription.text = getCoffeeDescription(coffee.name)
            binding.coffeePrice.text = getCoffeePrice(coffee.name)
        }

        // Observe add-to-cart event
        viewModel.addToCartEvent.observe(viewLifecycleOwner, Observer {
            Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_SHORT).show()
        })

        // Back button functionality
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Compose Add to Cart Button using ViewModel
        binding.addToCartComposeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    Button(
                        onClick = { viewModel.addToCart() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.grey_navy),
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Add to Cart",
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold))
                        )
                    }
                }
            }
        }
    }
    
    private fun getCoffeeDescription(coffeeName: String): String {
        return when (coffeeName) {
            "Americano" -> "A bold espresso shot diluted with hot water, creating a rich and smooth coffee experience."
            "Cappuccino" -> "Perfectly balanced espresso, steamed milk, and milk foam in equal parts."
            "Mocha" -> "Rich espresso combined with chocolate and steamed milk, topped with whipped cream."
            "Flat White" -> "Smooth espresso with velvety microfoam, creating a creamy texture."
            else -> "A delicious coffee beverage made with care."
        }
    }
    
    private fun getCoffeePrice(coffeeName: String): String {
        return when (coffeeName) {
            "Americano" -> "$3.50"
            "Cappuccino" -> "$4.25"
            "Mocha" -> "$4.75"
            "Flat White" -> "$4.00"
            else -> "$4.00"
        }
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.showBottomNav()
        super.onDestroyView()
        _binding = null
    }
} 