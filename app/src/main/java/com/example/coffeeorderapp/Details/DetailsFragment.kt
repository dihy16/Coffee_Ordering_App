package com.example.coffeeorderapp.Details

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.fragment.app.activityViewModels
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
import com.example.coffeeorderapp.Details.ViewModel.DetailsViewModel
import com.example.coffeeorderapp.cart.ViewModel.CartViewModel
import androidx.compose.runtime.remember
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.coffeeorderapp.Details.Customization.CartPreviewSheet

class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    @OptIn(ExperimentalMaterial3Api::class)
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
                val coffee       by viewModel.coffee.observeAsState()
                val customization by viewModel.customization.observeAsState(CoffeeCustomization())
                val calculatedPrice by viewModel.price.observeAsState(0.0)
                val cartItems by cartViewModel.cartItems.observeAsState(emptyList())
                val cartTotal by cartViewModel.totalPrice.observeAsState(0.0)

                var showCartPreview by remember { mutableStateOf(false) }
                val sheetState = rememberModalBottomSheetState()
                val coroutineScope = rememberCoroutineScope()

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
                            android.util.Log.d("DetailsFragment", "Add to cart clicked for: \\${it.name}")
                            viewModel.addToCartWithCustomization(it)
                        },
                        onCartClick      = {
                            showCartPreview = true
                        }
                    )
                }
                if (showCartPreview) {
                    ModalBottomSheet(
                        onDismissRequest = { showCartPreview = false },
                        sheetState = sheetState
                    ) {
                        CartPreviewSheet(
                            cartItems = cartItems,
                            totalPrice = cartTotal,
                            onViewCart = {
                                showCartPreview = false
                                findNavController().navigate(R.id.action_detailsFragment_to_cartFragment)
                            },
                            onDismiss = { showCartPreview = false }
                        )
                    }
                }
            }
        }

        // 3) Navigate after add-to-cart
        viewModel.addToCartEvent.observe(viewLifecycleOwner) {
            android.util.Log.d("DetailsFragment", "Add to cart event triggered")
            Toast.makeText(requireContext(), "Added to cart!", Toast.LENGTH_SHORT).show()
            // Navigate to cart with popUpTo to clear back stack
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.homeFragment, false)
                .build()
            findNavController().navigate(R.id.cartFragment, null, navOptions)
        }
    }

    private fun getCoffeePriceValue(name: String) = when (name) {
        "Americano" -> 3.50
        "Cappuccino"-> 4.25
        "Mocha"     -> 4.75
        "Flat White"-> 4.00
        else         -> 4.00
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.hideBottomNav()
    }

    override fun onPause() {
        (activity as? MainActivity)?.showBottomNav()
        super.onPause()
    }
}
