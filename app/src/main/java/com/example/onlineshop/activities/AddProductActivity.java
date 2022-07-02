package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityAddProductBinding;
import com.example.onlineshop.models.Product;
import com.example.onlineshop.AdminDashboardActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddProductActivity extends AppCompatActivity {

    private ActivityAddProductBinding binding;
    private FirebaseDatabase database;
    private ProgressDialog dialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Database
        database = FirebaseDatabase.getInstance();

        // Dialog
        dialogue =  new ProgressDialog(this);
        dialogue.setMessage("Adding Product...");

        // Top Toolbar
//        getSupportActionBar().setTitle("Add Product");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClickAddProductBtn(View view) {
        addProduct();
    }

    private void addProduct() {
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

        dialogue.show();
        String productId = database.getReference().push().getKey();

        Product product = new Product(productId,productName,price,stock,imageUrl,rating);

        database.getReference().child("Products").child(productId)
                .setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    dialogue.dismiss();
                    Intent intent = new Intent(AddProductActivity.this, AdminDashboardActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(AddProductActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}