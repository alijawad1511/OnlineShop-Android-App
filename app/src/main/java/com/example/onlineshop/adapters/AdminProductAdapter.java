package com.example.onlineshop.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.AdminDashboardActivity;
import com.example.onlineshop.ProductDetailActivity;
import com.example.onlineshop.R;
import com.example.onlineshop.EditProductActivity;
import com.example.onlineshop.databinding.ProductItemAdminBinding;
import com.example.onlineshop.databinding.ProductItemBinding;
import com.example.onlineshop.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<AdminProductAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<Product> products;
    private ArrayList<Product> allProducts;
    private boolean addedToCart = false;
    private boolean addedToFavourite = false;

    public AdminProductAdapter(Context context, ArrayList<Product> products){
        this.context = context;
        this.products = products;
        this.allProducts = new ArrayList<>();
        this.allProducts.addAll(products);
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
        holder.binding.tvPrice.setText(String.format("Rs. %.2f",product.getPrice()));

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
                bundle.putDouble("price", product.getPrice());
                bundle.putInt("stock",product.getStock());
                bundle.putString("imageUrl",product.getImageUrl());
                bundle.putDouble("rating",product.getRating());
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
                bundle.putDouble("price",product.getPrice());
                bundle.putInt("stock",product.getStock());
                bundle.putString("imageUrl",product.getImageUrl());
                bundle.putDouble("rating", product.getRating());

                // Put Data Bundle in Intent
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });

        holder.binding.btnDeleteProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Delete Product Code

                // Alert on Deletion
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Are you sure?");
                alert.setMessage("Do you want to delete product?");
                alert.setCancelable(false);

                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // After Admin Confirm
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        database.getReference().child("Products").child(product.getId()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){

                                            Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(context, AdminDashboardActivity.class);
//                                            context.startActivity(intent);

                                        }else{
                                            Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                alert.create().show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            // This method run as a thread in background automatically
            List<Product> filteredProducts = new ArrayList<>();

            // Filter Products on the base of keyword searched
            if(charSequence.toString().isEmpty()){
                filteredProducts.addAll(allProducts);
            }else{

                for(Product product : allProducts){
                    if(product.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredProducts.add(product);
                    }
                }

            }

            FilterResults filteredResults = new FilterResults();
            filteredResults.values = filteredProducts;


            // Return results in publicResults method below
            return filteredResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            products.clear();
            products.addAll((Collection<? extends Product>) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
