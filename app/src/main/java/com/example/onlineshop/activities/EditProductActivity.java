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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Database
        database = FirebaseDatabase.getInstance();

        // Get Data from Bundle in Intent
        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        String name = bundle.getString("name");
        String price = bundle.getString("price");
        String imageUrl = bundle.getString("imageUrl");
        String rating = bundle.getString("rating");

        binding.etProductName.setText(name);
        binding.etPrice.setText(price);
        binding.etImage.setText(imageUrl);
        binding.etRating.setText(rating);

        // Dialog
        dialog =  new ProgressDialog(this);
        dialog.setMessage("Saving Changes...");
    }

    public void onClickSaveChanges(View view){
        updateProduct();
    }

    public void updateProduct(){
        // After validation, add product to Firebase Database
        String productId;
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
            binding.etPrice.setError("Price is required");
            binding.etPrice.requestFocus();
            return;
        }

        if(imageUrl.isEmpty()){
            binding.etImage.setError("Image URL is required");
            binding.etImage.requestFocus();
            return;
        }

        if(rating.isEmpty()){
            binding.etRating.setError("Rating is required");
            binding.etRating.requestFocus();
            return;
        }

        dialog.show();
        productId = database.getReference().push().getKey();

        Product product = new Product(productId,productName,price,imageUrl,rating);

        database.getReference().child("Products").child(product.getId()).updateChildren(product.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(EditProductActivity.this, "Product updated successfully", Toast.LENGTH_SHORT).show();
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