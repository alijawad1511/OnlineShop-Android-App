package com.example.onlineshop.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineshop.R;
import com.example.onlineshop.databinding.CartItemBinding;
import com.example.onlineshop.databinding.QuantityDialogueBinding;
import com.example.onlineshop.models.Cart;
import com.example.onlineshop.models.Product;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    Context context;
    ArrayList<Cart> cartItems;

    public CartAdapter(Context context, ArrayList<Cart> cartItems){
        this.context = context;
        this.cartItems = cartItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CartItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CartItemBinding.bind(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cartItem = cartItems.get(position);

        // Set Data to View
        Glide.with(context).load(cartItem.getProductImageUrl()).into(holder.binding.ivItemImage);
        holder.binding.tvItemName.setText(cartItem.getProductName());
        holder.binding.tvItemsPrice.setText("$"+cartItem.getPrice());
        holder.binding.tvItemCount.setText(cartItem.getNumberOfItems()+" item(s)");

        // Click Listeners
        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                QuantityDialogueBinding dialogueBinding = QuantityDialogueBinding.inflate(LayoutInflater.from(context));

                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setView(dialogueBinding.getRoot())
                        .create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));

                dialogueBinding.tvProductName.setText(cartItem.getProductName());

                // Dialogue Click Listeners
                dialogueBinding.btnPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                dialogueBinding.btnMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                dialogueBinding.btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                dialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}
