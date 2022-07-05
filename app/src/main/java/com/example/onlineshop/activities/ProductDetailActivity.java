package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlineshop.CartActivity;
import com.example.onlineshop.MainActivity;
import com.example.onlineshop.databinding.ActivityProductDetailBinding;
import com.example.onlineshop.models.Cart;
import com.example.onlineshop.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailActivity extends AppCompatActivity {

    private ActivityProductDetailBinding binding;
    private Product product;
    private FirebaseDatabase database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Database
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        // Get Data from Bundle in Intent
        Bundle b = getIntent().getExtras();
        String id = b.getString("id");
        String name = b.getString("name");
        double price = b.getDouble("price");
        int stock = b.getInt("stock");
        String imageUrl = b.getString("imageUrl");
        double rating = b.getDouble("rating");
        boolean addedToCart = b.getBoolean("addedToCart");

        product = new Product(id,name,price,stock,imageUrl,rating,addedToCart);

        Glide.with(this).load(imageUrl).into(binding.ivItemImage);
        binding.tvPrice.setText(String.format("Rs. %.2f",product.getPrice()));
        binding.tvRating.setText(String.valueOf(product.getRating()));
        binding.tvProductName.setText(product.getName());


        if(product.isAddedToCart()){
            binding.btnAddToCart.setText("Added to Cart");
            binding.btnAddToCart.setClickable(false);
        }

        // Action Bar
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void checkInCart(String productId){
        ArrayList<Cart> cart = new ArrayList<>();

        Query query = database.getReference().child("Product").orderByChild("addedToCart")
                .equalTo(true);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cart.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Cart cartItem = snapshot1.getValue(Cart.class);
                    cart.add(cartItem);
                }

                Log.d("CART_PRODUCT",cart.size()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProductDetailActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void onClickAddToCart(View view) {


        Cart cartItem = new Cart(product.getId(),product.getName(),product.getImageUrl(),1,product.getPrice());

        database.getReference().child("Cart").child(auth.getCurrentUser().getUid()).child(product.getId())
                .setValue(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    binding.btnAddToCart.setText("Added to Cart");
                    binding.btnAddToCart.setClickable(false);
                    Toast.makeText(ProductDetailActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Update Product key addedToCart to true
        database.getReference().child("Products").child(product.getId()).child("addedToCart")
                .setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("CART_PRODUCT","Product value updated");

                }else{
                    Toast.makeText(ProductDetailActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}