package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlineshop.AllProductsActivity;
import com.example.onlineshop.databinding.ActivityAdminDashboardBinding;
import com.example.onlineshop.AddProductActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    ActivityAdminDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    public void onClickAddProduct(View view){
        startActivity(new Intent(AdminDashboardActivity.this, AddProductActivity.class));
        finish();
    }

    public void onClickDeleteProduct(View view){
        Toast.makeText(this, "Delete Product Clicked...", Toast.LENGTH_SHORT).show();
    }

    public void onClickSearchProduct(View view){
        Toast.makeText(this, "Search Product Clicked...", Toast.LENGTH_SHORT).show();
    }

    public void onClickAllProducts(View view) {
        startActivity(new Intent(AdminDashboardActivity.this, AllProductsActivity.class));
        finish();
    }
}