package com.example.coffeeorderapp.Details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.coffeeorderapp.HomePage.Data.Coffee
import com.example.coffeeorderapp.MainActivity
import com.example.coffeeorderapp.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import com.example.coffeeorderapp.Details.Customization.CoffeeDetailScreen
import com.example.coffeeorderapp.Details.Customization.CoffeeCustomization

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.hideBottomNav()

        // 1) Compute full Coffee model and feed VM
        viewModel.setCoffee(
            Coffee(
                name       = args.coffeeName,
                imageResId = args.coffeeImageResId,
                price      = getCoffeePriceValue(args.coffeeName)
            )
        )

        // 2) Hook up the ComposeView
        view.findViewById<ComposeView>(R.id.compose_root).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // Observe your VM
                val coffee       by viewModel.coffee.observeAsState()
                val customization by viewModel.customization.observeAsState(CoffeeCustomization())
                val calculatedPrice by viewModel.price.observeAsState(0.0)

                coffee?.let {
                    CoffeeDetailScreen(
                        coffee           = it,
                        quantity         = customization.quantity,
                        onQuantityChange = { viewModel.updateCustomization(customization.copy(quantity = it)) },
                        shot             = customization.shot,
                        onShotChange     = { viewModel.updateCustomization(customization.copy(shot = it)) },
                        select           = customization.select,
                        onSelectChange   = { viewModel.updateCustomization(customization.copy(select = it)) },
                        size             = customization.size,
                        onSizeChange     = { viewModel.updateCustomization(customization.copy(size = it)) },
                        ice              = customization.ice,
                        onIceChange      = { viewModel.updateCustomization(customization.copy(ice = it)) },
                        calculatedPrice  = calculatedPrice,
                        onBack           = { findNavController().navigateUp() },
                        onAddToCart      = {
                            viewModel.addToCartWithCustomization(it)
                        }
                    )
                }
            }
        }

        // 3) Navigate after add-to-cart
        viewModel.addToCartEvent.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.ordersFragment)
        }
    }

    private fun getCoffeePriceValue(name: String) = when (name) {
        "Americano" -> 3.50
        "Cappuccino"-> 4.25
        "Mocha"     -> 4.75
        "Flat White"-> 4.00
        else         -> 4.00
    }

    override fun onDestroyView() {
        (activity as? MainActivity)?.showBottomNav()
        super.onDestroyView()
    }
}
