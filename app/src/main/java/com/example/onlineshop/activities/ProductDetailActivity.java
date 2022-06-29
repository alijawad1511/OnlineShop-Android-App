package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlineshop.CartActivity;
import com.example.onlineshop.databinding.ActivityProductDetailBinding;
import com.example.onlineshop.models.Cart;
import com.example.onlineshop.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
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
        String price = b.getString("price");
        String imageUrl = b.getString("imageUrl");
        String rating = b.getString("rating");

        product = new Product(id,name,price,imageUrl,rating);

        Glide.with(this).load(imageUrl).into(binding.ivItemImage);

        // Action Bar
        getSupportActionBar().setTitle(name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.cart){
            startActivity(new Intent(this,CartActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public void onClickAddToCart(View view) {

        String userId = auth.getCurrentUser().getUid();
        String cartItemId = database.getReference().push().getKey();

        Cart cartItem = new Cart(cartItemId,product.getId(),product.getName(),product.getImageUrl(),"1",product.getPrice());

        database.getReference().child("Cart").child(userId).child(cartItemId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                database.getReference().child("Cart").child(userId).child(cartItemId).setValue(cartItem)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            binding.btnAddToCart.setText("Added to Card");
                            binding.btnAddToCart.setClickable(false);

                            Toast.makeText(ProductDetailActivity.this, "Product Added to Cart", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProductDetailActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}