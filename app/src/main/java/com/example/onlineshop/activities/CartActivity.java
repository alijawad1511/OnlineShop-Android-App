package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.example.onlineshop.MainActivity;
import com.example.onlineshop.adapters.CartAdapter;
import com.example.onlineshop.databinding.ActivityCartBinding;
import com.example.onlineshop.models.Cart;
import com.example.onlineshop.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private CartAdapter cartAdapter;
    private ArrayList<Cart> cartItems;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private float subTotal = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding XML+Java
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Database
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        // Cart Adapter
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(this,cartItems);


        // Get Cart Data from Firebase
        getCartItems();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this,layoutManager.getOrientation());
        binding.cartRecyclerView.setLayoutManager(layoutManager);
        binding.cartRecyclerView.setAdapter(cartAdapter);
        binding.cartRecyclerView.addItemDecoration(divider);


        // Action Bar
        getSupportActionBar().setTitle("Shopping Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(CartActivity.this, MainActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }

    public void getCartItems(){
        database.getReference().child("Cart").child(auth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    Cart cartItem = null;
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        cartItems.clear();

                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            cartItem = snapshot1.getValue(Cart.class);
                            subTotal+=cartItem.getPrice();
                            cartItems.add(cartItem);
                        }
                        // Set Total Price of Cart Items
                        binding.tvTotalPrice.setText(subTotal+"");

                        cartAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CartActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}