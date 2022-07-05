package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlineshop.AllProductsActivity;
import com.example.onlineshop.AdminLoginActivity;
import com.example.onlineshop.SearchProductActivity;
import com.example.onlineshop.databinding.ActivityAdminDashboardBinding;
import com.example.onlineshop.AddProductActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    private ActivityAdminDashboardBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Database
        auth = FirebaseAuth.getInstance();

        getSupportActionBar().setTitle("Dashboard");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_dashboard_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.logout:
                auth.signOut();
                startActivity(new Intent(AdminDashboardActivity.this, AdminLoginActivity.class));
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickAddProduct(View view){
        startActivity(new Intent(AdminDashboardActivity.this, AddProductActivity.class));
        finish();
    }

    public void onClickDeleteProduct(View view){
        Toast.makeText(this, "Delete Product Clicked...", Toast.LENGTH_SHORT).show();
    }

    public void onClickSearchProduct(View view){
        startActivity(new Intent(AdminDashboardActivity.this, SearchProductActivity.class));
        finish();
    }

    public void onClickAllProducts(View view) {
        startActivity(new Intent(AdminDashboardActivity.this, AllProductsActivity.class));
        finish();
    }
}