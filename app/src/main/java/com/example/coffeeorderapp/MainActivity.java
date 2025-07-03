package com.example.coffeeorderapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.coffeeorderapp.HomePage.Adapter.CoffeeAdapter;
import com.example.coffeeorderapp.HomePage.ViewModel.CoffeeViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.coffeeorderapp.HomePage.ViewModel.LoyaltyViewModel;
import com.example.coffeeorderapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private CoffeeViewModel coffeeViewModel;
    private CoffeeAdapter adapter;
    private LoyaltyViewModel loyaltyViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        coffeeViewModel = new ViewModelProvider(this).get(CoffeeViewModel.class);
        loyaltyViewModel = new ViewModelProvider(this).get(LoyaltyViewModel.class);


        loyaltyViewModel.getLoyaltyProgress().observe(this, progress -> {
            if (progress != null) {
                binding.loyaltyStatus.loyaltyProgressView.setProgress(progress.getCurrent(), progress.getTotal());
                binding.loyaltyStatus.stampCnt.setText(getString(R.string.stamp_count, progress.getCurrent(), progress.getTotal()));
            }
        });

        adapter = new CoffeeAdapter(new ArrayList<>());
        binding.coffeeRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        binding.coffeeRecyclerView.setAdapter(adapter);

        coffeeViewModel.getCoffees().observe(this, coffees -> adapter.updateItems(coffees));
    }
}