package com.example.coffeeorderapp.Rewards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeorderapp.R
import com.example.coffeeorderapp.HomePage.View.LoyaltyProgressView
import androidx.navigation.fragment.findNavController
import android.widget.Toast
import com.example.coffeeorderapp.cart.data.ProductEntity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RewardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RewardsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val rewardsViewModel by viewModels<RewardsViewModel>()
    private lateinit var historyAdapter: RewardsHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rewards, container, false)

        // Loyalty section
        val loyaltyStatus = view.findViewById<View>(R.id.loyaltyStatus)
        val loyaltyProgressView = loyaltyStatus.findViewById<LoyaltyProgressView>(R.id.loyaltyProgressView)
        val stampCnt = loyaltyStatus.findViewById<TextView>(R.id.stampCnt)

        // DEBUG: Set stamps to 7 for testing. Remove before production!
//        rewardsViewModel.setStampsForDebug(7)
        // DEBUG: Set total points to 1000 for testing. Remove before production!
//        rewardsViewModel.setPointsForDebug(1000)

        // Points section
        val pointsValue = view.findViewById<TextView>(R.id.pointsValue)
        val redeemButton = view.findViewById<Button>(R.id.redeemButton)

        // History section
        val historyRecyclerView = view.findViewById<RecyclerView>(R.id.historyRecyclerView)
        historyAdapter = RewardsHistoryAdapter(emptyList())
        historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        historyRecyclerView.adapter = historyAdapter

        // Observe ViewModel
        rewardsViewModel.loyaltyStamps.observe(viewLifecycleOwner, Observer { stamps ->
            loyaltyProgressView.setProgress(stamps, 8)
            stampCnt.text = getString(R.string.stamp_count, stamps, 8)
            // Reset logic: if 8, clicking loyalty card resets
            loyaltyProgressView.setOnClickListener {
                if (stamps >= 8) {
                    rewardsViewModel.resetStamps()
                }
            }
        })
        rewardsViewModel.totalPoints.observe(viewLifecycleOwner, Observer { points ->
            pointsValue.text = points.toString()
        })
        rewardsViewModel.rewardHistory.observe(viewLifecycleOwner, Observer { history ->
            historyAdapter.updateItems(history)
        })

        // Redeem button (navigate to future screen)
        redeemButton.setOnClickListener {
            findNavController().navigate(R.id.action_rewardsFragment_to_redeemFragment)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        // No need to call rewardsViewModel.loadRewardHistory() anymore
    }

    private fun redeemProductWithPoints(product: ProductEntity) {
        val conversionRate = 100 // 1$ = 100 pts
        val requiredPoints = (product.price * conversionRate).toInt()
        rewardsViewModel.redeemWithPoints(requiredPoints) { success ->
            if (success) {
                Toast.makeText(requireContext(), "Redeemed successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Not enough points!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RewardsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RewardsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}