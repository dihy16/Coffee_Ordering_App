package com.example.coffeeorderapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import androidx.recyclerview.widget.GridLayoutManager;
import com.example.coffeeorderapp.HomePage.Adapter.CoffeeAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffeeorderapp.HomePage.ViewModel.CoffeeViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.coffeeorderapp.HomePage.View.LoyaltyProgressView;
import com.example.coffeeorderapp.HomePage.ViewModel.LoyaltyViewModel;

import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    private CoffeeViewModel coffeeViewModel;
    private CoffeeAdapter adapter;
    private LoyaltyProgressView loyaltyProgressView;
    private LoyaltyViewModel loyaltyViewModel;
    private TextView stampCnt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        loyaltyProgressView = findViewById(R.id.loyaltyProgressView);
        stampCnt = findViewById(R.id.stampCnt);
        loyaltyViewModel = new ViewModelProvider(this).get(LoyaltyViewModel.class);
        loyaltyViewModel.getLoyaltyProgress().observe(this, progress -> {
            if (progress != null) {
                loyaltyProgressView.setProgress(progress.getCurrent(), progress.getTotal());
            }
            stampCnt.setText(String.valueOf(progress.getCurrent() + "/" + progress.getTotal()));
        });

        coffeeViewModel = new ViewModelProvider(this).get(CoffeeViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.coffeeRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new CoffeeAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        coffeeViewModel.getCoffees().observe(this, coffees -> adapter.updateItems(coffees));
    }
}