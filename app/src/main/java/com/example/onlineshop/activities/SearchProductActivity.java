package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.AdminDashboardActivity;
import com.example.onlineshop.MainActivity;
import com.example.onlineshop.adapters.ProductAdapter;
import com.example.onlineshop.databinding.ActivitySearchProductBinding;
import com.example.onlineshop.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchProductActivity extends AppCompatActivity {

    private ActivitySearchProductBinding binding;
    private FirebaseDatabase database;
    private ProductAdapter productAdapter;
    private ArrayList<Product> products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();

        initProducts();

        // RecyclerView Setting
        binding.productRecyclerView.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this, RecyclerView.VERTICAL, false));

        // Action Bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initProducts() {
        products = new ArrayList<>();

        database.getReference().child("Products").addValueEventListener(new ValueEventListener() {

            Product product = null;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    product = snapshot1.getValue(Product.class);
                    products.add(product);
                }

                // Notify Adapter about Data Change
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchProductActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Set RecyclerView Adapter
        productAdapter = new ProductAdapter(SearchProductActivity.this,products);
        binding.productRecyclerView.setAdapter(productAdapter);
    }

    private void searchProducts(String text){
        ArrayList<Product> searchedProducts = new ArrayList<>();

        for(Product product : products){
            if(product.getName().toLowerCase().contains(text.toLowerCase())){
                searchedProducts.add(product);
            }
        }

        ProductAdapter adapter = new ProductAdapter(SearchProductActivity.this,searchedProducts);
        binding.productRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.actionbar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Product Here...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchProducts(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(SearchProductActivity.this, MainActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SearchProductActivity.this, MainActivity.class));
        finish();
    }
}