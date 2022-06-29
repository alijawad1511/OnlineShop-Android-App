package com.example.onlineshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.AdminDashboardActivity;
import com.example.onlineshop.ProductDetailActivity;
import com.example.onlineshop.R;
import com.example.onlineshop.activities.EditProductActivity;
import com.example.onlineshop.databinding.ProductItemAdminBinding;
import com.example.onlineshop.databinding.ProductItemBinding;
import com.example.onlineshop.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder>{
    Context context;
    ArrayList<Product> products;
    private boolean addedToCart = false;
    private boolean addedToFavourite = false;

    public AdminProductAdapter(Context context, ArrayList<Product> products){
        this.context = context;
        this.products = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ProductItemAdminBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProductItemAdminBinding.bind(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item_admin,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminProductAdapter.ViewHolder holder, int position) {
        Product product = products.get(position);

        // Set Data in View(XML)
        holder.binding.tvProductName.setText(product.getName());
        holder.binding.tvPrice.setText(String.valueOf(product.getPrice()));

        // Set Image URL in ImageView
        Glide.with(context).load(product.getImageUrl()).into(holder.binding.ivImage);

        // @@@( Click Listeners )@@@
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetailActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("id",product.getId());
                bundle.putString("name",product.getName());
                bundle.putString("price",product.getPrice());
                bundle.putString("imageUrl",product.getImageUrl());
                bundle.putString("rating",product.getRating());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.binding.btnEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProductActivity.class);

                // Put Data in Bunlde
                Bundle bundle = new Bundle();
                bundle.putString("id",product.getId());
                bundle.putString("name",product.getName());
                bundle.putString("price",product.getPrice());
                bundle.putString("imageUrl",product.getImageUrl());
                bundle.putString("rating", product.getRating());

                // Put Data Bundle in Intent
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.binding.btnDeleteProduct.setOnClickListener(new View.OnClickListener() {

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            @Override
            public void onClick(View view) {
                // Delete Product Code
                database.getReference().child("Products").child(product.getId()).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    Toast.makeText(context, "Product deleted", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, AdminDashboardActivity.class);
                                    context.startActivity(intent);

                                }else{
                                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
