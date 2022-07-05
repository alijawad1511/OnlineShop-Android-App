package com.example.onlineshop.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.ProductDetailActivity;
import com.example.onlineshop.R;
import com.example.onlineshop.databinding.ProductItemBinding;
import com.example.onlineshop.models.Cart;
import com.example.onlineshop.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Product> products;
    private boolean addedToFavourite = false;
    private ArrayList<Cart> cart;

    public ProductAdapter(Context context, ArrayList<Product> products){
        this.context = context;
        this.products = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ProductItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProductItemBinding.bind(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        // Get Cart Items
        getCartItems();

        // Set Data in View(XML)
        holder.binding.tvProductName.setText(product.getName());
        holder.binding.tvPrice.setText(String.format("Rs. %.2f",product.getPrice()));

        // Set Image URL in ImageView
        Glide.with(context).load(product.getImageUrl()).into(holder.binding.ivImage);

        if(product.isAddedToCart()){
            holder.binding.ivAddToCart.setImageResource(R.drawable.ic_cart_filled);
        }

        // @@@( Click Listeners )@@@
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(context, ProductDetailActivity.class);

                 Bundle bundle = new Bundle();
                 bundle.putString("id",product.getId());
                 bundle.putString("name",product.getName());
                 bundle.putDouble("price",product.getPrice());
                 bundle.putInt("stock",product.getStock());
                 bundle.putString("imageUrl",product.getImageUrl());
                 bundle.putDouble("rating",product.getRating());
                 bundle.putBoolean("addedToCart",product.isAddedToCart());
                 intent.putExtras(bundle);
                 context.startActivity(intent);
            }
        });

        holder.binding.ivAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                FirebaseAuth auth = FirebaseAuth.getInstance();

                String userId = auth.getCurrentUser().getUid();
                
                if(!product.isAddedToCart()){

                    // Code for Add to Cart
                    Cart cartItem = new Cart(product.getId(),product.getName(),product.getImageUrl(),1,product.getPrice());

                    database.getReference().child("Cart").child(userId).child(product.getId()).setValue(cartItem)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
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
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    // Fill Cart Icon on Product Card
                    holder.binding.ivAddToCart.setImageResource(R.drawable.ic_cart_filled);

                }else{

                    // Code to Remove Product from Cart
                    database.getReference().child("Cart").child(userId).child(product.getId()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "Removed from Cart", Toast.LENGTH_SHORT).show();
                                        holder.binding.ivAddToCart.setImageResource(R.drawable.ic_cart_outline);
                                    }else{
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                    // Update Product key addedToCart to false
                    database.getReference().child("Products").child(product.getId()).child("addedToCart")
                            .setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Log.d("CART_PRODUCT","Product value updated");

                            }else{
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }

            }
        });

        holder.binding.ivAddToFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!addedToFavourite){
                    addedToFavourite = true;
                    holder.binding.ivAddToFavourite.setImageResource(R.drawable.ic_favourite_filled);
                    Toast.makeText(context, "Add to Cart", Toast.LENGTH_SHORT).show();
                }else{
                    addedToFavourite = false;
                    holder.binding.ivAddToFavourite.setImageResource(R.drawable.ic_favourite_outline);
                    Toast.makeText(context, "Removed from Cart", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getCartItems() {
        cart = new ArrayList<>();
        // Get Cart Items
        FirebaseDatabase.getInstance().getReference().child("Cart")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    Cart cartItem = null;

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            cartItem = snapshot1.getValue(Cart.class);
                            cart.add(cartItem);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public int getItemCount() {
        return products.size();
    }
}
