package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.onlineshop.adapters.AdminProductAdapter;
import com.example.onlineshop.adapters.ProductAdapter;
import com.example.onlineshop.databinding.ActivityAllProductsBinding;
import com.example.onlineshop.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllProductsActivity extends AppCompatActivity {

    private ActivityAllProductsBinding binding;
    private FirebaseDatabase database;
    private ArrayList<Product> products;
    private AdminProductAdapter adminProductAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Database
        database = FirebaseDatabase.getInstance();
        products = new ArrayList<Product>();


        // Set Adapter
        adminProductAdapter = new AdminProductAdapter(this,products);
        binding.recyclerView.setAdapter(adminProductAdapter);


        // Get Data from Firebase
        database.getReference().child("Products").addValueEventListener(new ValueEventListener() {
            Product product = null;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear Previous Products Data from List
                products.clear();

                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    product = snapshot1.getValue(Product.class);
                    products.add(product);
                }

                // Notify Adapter about Data Updating
                adminProductAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllProductsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}