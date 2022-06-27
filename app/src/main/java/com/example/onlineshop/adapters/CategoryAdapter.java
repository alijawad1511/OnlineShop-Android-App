package com.example.onlineshop.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.R;
import com.example.onlineshop.databinding.CategoryItemBinding;
import com.example.onlineshop.databinding.CategoryItemBinding;
import com.example.onlineshop.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    ArrayList<Category> categories;
    Context context;

    // Context tells on which activity/class adapter is used
    public CategoryAdapter(Context context, ArrayList<Category> categories)
    {
        this.context = context;
        this.categories = categories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CategoryItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CategoryItemBinding.bind(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.binding.tvCategoryName.setText(category.getName());
        Glide.with(context).load(category.getImageUrl()).into(holder.binding.ivCategoryImage);

        // Glide used when we have to load images from Internet we use two libraries:
        // -> Glide
        // -> Picasso

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
