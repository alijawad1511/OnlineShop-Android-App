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
import com.example.onlineshop.ProductDetailActivity;
import com.example.onlineshop.R;
import com.example.onlineshop.databinding.ProductItemBinding;
import com.example.onlineshop.models.Product;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> products;
    private boolean addedToCart = false;
    private boolean addedToFavourite = false;

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
                 bundle.putDouble("price",product.getPrice());
                 bundle.putInt("stock",product.getStock());
                 bundle.putString("imageUrl",product.getImageUrl());
                 bundle.putDouble("rating",product.getRating());
                 intent.putExtras(bundle);
                 context.startActivity(intent);
            }
        });

        holder.binding.ivAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!addedToCart){
                    addedToCart = true;
                    holder.binding.ivAddToCart.setImageResource(R.drawable.ic_cart_filled);
                    Toast.makeText(context, "Add to Cart", Toast.LENGTH_SHORT).show();
                }else{
                    addedToCart = false;
                    holder.binding.ivAddToCart.setImageResource(R.drawable.ic_cart_outline);
                    Toast.makeText(context, "Removed from Cart", Toast.LENGTH_SHORT).show();
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

        holder.binding.ivStar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "1-Star Ratting", Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.ivStar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "2-Star Ratting", Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.ivStar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "3-Star Ratting", Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.ivStar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "4-Star Ratting", Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.ivStar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "5-Star Ratting", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return products.size();
    }
}
