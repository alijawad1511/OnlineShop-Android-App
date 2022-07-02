package com.example.onlineshop.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.AddProductActivity;
import com.example.onlineshop.AdminDashboardActivity;
import com.example.onlineshop.AllProductsActivity;
import com.example.onlineshop.databinding.ActivityEditProductBinding;
import com.example.onlineshop.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProductActivity extends AppCompatActivity {

    private ActivityEditProductBinding binding;
    private FirebaseDatabase database;
    private ProgressDialog dialog;
    // Product ID to be edited
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Database
        database = FirebaseDatabase.getInstance();

        // Get Data from Bundle in Intent
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        String name = bundle.getString("name");
        double price = bundle.getDouble("price");
        int stock = bundle.getInt("stock");
        String imageUrl = bundle.getString("imageUrl");
        double rating = bundle.getDouble("rating");

        binding.etProductName.setText(name);
        binding.etPrice.setText(String.valueOf(price));
        binding.etStock.setText(String.valueOf(stock));
        binding.etImage.setText(imageUrl);
        binding.etRating.setText(String.valueOf(rating));

        // Dialog
        dialog =  new ProgressDialog(this);
        dialog.setMessage("Saving Changes...");

        // Action Bar
        getSupportActionBar().setTitle("Edit Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(EditProductActivity.this, AllProductsActivity.class));
        finish();
        return super.onSupportNavigateUp();
    }

    public void onClickSaveChanges(View view){
        updateProduct();
    }

    public void updateProduct(){
        // After validation, add product to Firebase Database
        String productName = binding.etProductName.getText().toString();
        double price = Double.parseDouble(binding.etPrice.getText().toString());
        int stock = Integer.parseInt(binding.etStock.getText().toString());
        String imageUrl = binding.etImage.getText().toString();
        double rating = Double.parseDouble(binding.etRating.getText().toString());

        if(productName.isEmpty()){
            binding.etProductName.setError("Product name is required");
            binding.etProductName.requestFocus();
            return;
        }

        if(price==0.0){
            binding.etPrice.setError("Invalid Price! Enter price of product");
            binding.etPrice.requestFocus();
            return;
        }

        if(stock<1){
            binding.etPrice.setError("Please enter stock of product");
            binding.etPrice.requestFocus();
            return;
        }

        if(imageUrl.isEmpty()){
            binding.etImage.setError("Image URL is required");
            binding.etImage.requestFocus();
            return;
        }

        if(rating>5.0 || rating<0.0){
            binding.etRating.setError("Invalid Rating! Rating must be between 0.0 - 5.0");
            binding.etRating.requestFocus();
            return;
        }

        dialog.show();

        Product product = new Product(id,productName,price,imageUrl,rating);

        database.getReference().child("Products").child(product.getId()).updateChildren(product.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(EditProductActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditProductActivity.this, AdminDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(EditProductActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}