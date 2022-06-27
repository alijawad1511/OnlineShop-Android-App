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
        getSupportActionBar().setTitle("Add Product");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onClickAddProductBtn(View view) {
        addProduct();
    }

    private void addProduct() {
        // After validation, add product to Firebase Database
        String id;
        String productName = binding.etProductName.getText().toString();
        String price = binding.etPrice.getText().toString();
        String imageUrl = binding.etImage.getText().toString();
        String rating = binding.etRating.getText().toString();

        if(productName.isEmpty()){
            binding.etProductName.setError("Product name is required");
            binding.etProductName.requestFocus();
            return;
        }

        if(price.isEmpty()){
            binding.etProductName.setError("Product name is required");
            binding.etProductName.requestFocus();
            return;
        }

        if(imageUrl.isEmpty()){
            binding.etProductName.setError("Product name is required");
            binding.etProductName.requestFocus();
            return;
        }

        if(rating.isEmpty()){
            binding.etProductName.setError("Product name is required");
            binding.etProductName.requestFocus();
            return;
        }

        dialogue.show();
        id = database.getReference().push().getKey();

        Product product = new Product(id,productName,price,imageUrl,rating);

        database.getReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                database.getReference().child("Products").child(id).setValue(product)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialogue.dismiss();
                            Intent intent = new Intent(AddProductActivity.this, com.example.onlineshop.AdminDashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(AddProductActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}