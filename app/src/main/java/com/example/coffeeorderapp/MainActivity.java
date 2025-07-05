package com.example.coffeeorderapp;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.coffeeorderapp.cart.data.DataInitializer;
import com.example.coffeeorderapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        // Initialize database synchronously before UI setup
        try {
            DataInitializer.INSTANCE.initializeDatabaseSynchronously(this);
            
            // Additional safety measure - clear and re-seed products
            DataInitializer.INSTANCE.clearAndReseedProducts(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        NavHostFragment navHost =
                (NavHostFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHost.getNavController();
        binding.bottomNav.setItemIconTintList(getColorStateList(R.color.bottom_nav_color));
        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        binding.bottomNav.setOnApplyWindowInsetsListener(null);
        binding.bottomNav.setPadding(0, 0, 0, 0);
    }

    public void hideBottomNav() {
        View bottomNav = findViewById(R.id.bottomNav);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.GONE);
        }
    }

    public void showBottomNav() {
        View bottomNav = findViewById(R.id.bottomNav);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
    }
}