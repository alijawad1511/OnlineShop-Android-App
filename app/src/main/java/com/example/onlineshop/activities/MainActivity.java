package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.R;
import com.example.onlineshop.adapters.CategoryAdapter;
import com.example.onlineshop.adapters.ProductAdapter;
import com.example.onlineshop.databinding.ActivityMainBinding;
import com.example.onlineshop.models.Category;
import com.example.onlineshop.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Category> categories;
    private ArrayList<Product> products;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private FirebaseFirestore firestoreDB;
    private FirebaseDatabase database;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.categoryRecyclerView.setHasFixedSize(true);
        binding.productRecyclerView.setHasFixedSize(true);

        // Loading Dialog
        dialog = new ProgressDialog(this);
        dialog.setTitle("Welcome to OnlineShop");
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();


        // Database
        firestoreDB = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();


        // Init Data
        initCategories();
        initProducts();
        initSlider();

        binding.tvSeeAll1.setPaintFlags(binding.tvSeeAll1.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        binding.tvSeeAll2.setPaintFlags(binding.tvSeeAll2.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
    }

    private void initSlider() {
        binding.carousel.addData(new CarouselItem("https://wowslider.com/sliders/demo-18/data1/images/shanghai.jpg","Image Caption 1"));
        binding.carousel.addData(new CarouselItem("https://t4.ftcdn.net/jpg/02/61/01/87/360_F_261018762_f15Hmze7A0oL58Uwe7SrDKNS4fZIjLiF.jpg","Image Caption 2"));
    }

    public void initCategories(){
        // Setting LayoutManager of Category RecyclerView
        binding.categoryRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));


        // Initializing Categories List
        categories = new ArrayList<>();

        // Retrieve Data from FirebaseFirestore
        firestoreDB.collection("Categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){

                            // Get Data in Category Document in Firebase DB
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                // Add to ArrayList
                                categories.add(category);
                                categoryAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }

                        }else{

                        }
                    }
                });

        // Set RecyclerView Adapter
        categoryAdapter = new CategoryAdapter(MainActivity.this,categories);
        binding.categoryRecyclerView.setAdapter(categoryAdapter);
    }

    public void initProducts(){
        // Use Density Class to get Device Screen Size and make app responsive by using if else
        // Setting LayoutManager of Category RecyclerView
        binding.productRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));


        products = new ArrayList<>();

        // Fetch Data from Firebase
        database.getReference().child("Products").addValueEventListener(new ValueEventListener() {

            Product product = null;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                products.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    product = snapshot1.getValue(Product.class);
                    products.add(product);
                }

                // Notify Adapter about Data Change
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Set RecyclerView Adapter
        productAdapter = new ProductAdapter(MainActivity.this,products);
        binding.productRecyclerView.setAdapter(productAdapter);
    }

    public void onClickSeeAllProducts(View view) {
        // Code Here...
        Toast.makeText(this, "See All Products", Toast.LENGTH_SHORT).show();
    }
}