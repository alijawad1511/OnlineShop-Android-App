package com.example.onlineshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.onlineshop.adapters.CartAdapter;
import com.example.onlineshop.databinding.ActivityCartBinding;
import com.example.onlineshop.models.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartAdapter cartAdapter;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding XML+Java
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cart Adapter
        products = new ArrayList<>();
        products.add(new Product("1","Modern Headphone","44.99","https://images.unsplash.com/photo-1505740420928-5e560c06d30e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8aGVhZHBob25lc3xlbnwwfHwwfHw%3D&w=1000&q=80","5"));
        products.add(new Product("4", "DSLR Camera", "130", "https://www.prophotostudio.net/wp-content/uploads/2021/02/best-camera-photos1-scaled.jpeg","4"));
        products.add(new Product("2","Sofa","79.99","https://media.istockphoto.com/photos/turquoise-arm-chair-isolated-on-white-background-front-view-of-picture-id1199428736?k=20&m=1199428736&s=612x612&w=0&h=vRS-zg2d6tF7jqQ8lI3oYFs_JC3fXdPCZhkvlEhHJkc=","4"));
        products.add(new Product("3", "DSLR Camera", "129.99", "https://www.prophotostudio.net/wp-content/uploads/2021/02/best-camera-photos1-scaled.jpeg","5"));
        cartAdapter = new CartAdapter(this,products);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration divider = new DividerItemDecoration(this,layoutManager.getOrientation());
        binding.cartRecyclerView.setLayoutManager(layoutManager);
        binding.cartRecyclerView.setAdapter(cartAdapter);
        binding.cartRecyclerView.addItemDecoration(divider);

        // Action Bar
        getSupportActionBar().setTitle("Shopping Cart");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}