package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.SearchView;

import com.example.onlineshop.AdminDashboardActivity;
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
    private ProgressDialog dialogue;


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


        // Loading Dialog
        dialogue =  new ProgressDialog(this);
        dialogue.setMessage("Loading...");
        dialogue.show();


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
                dialogue.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AllProductsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        // Action Bar
        getSupportActionBar().setTitle("All Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AllProductsActivity.this,AdminDashboardActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.actionbar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Product Here...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchKeyword) {
                // Called on when user enter a word and press enter

                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchKeyword) {
                // Call on each character change in Search View

                adminProductAdapter.getFilter().filter(searchKeyword);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(AllProductsActivity.this, AdminDashboardActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }
}