package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;

import com.example.onlineshop.R;
import com.example.onlineshop.adapters.CategoryAdapter;
import com.example.onlineshop.adapters.ProductAdapter;
import com.example.onlineshop.databinding.ActivityMainBinding;
import com.example.onlineshop.models.Category;
import com.example.onlineshop.models.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private FirebaseFirestore database;
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


        database = FirebaseFirestore.getInstance();

        initCategories();
        initProducts();
        initSlider();
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
        database.collection("Categories")
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
        products.add(new Product("1","Modern Headphone","44.99","https://images.unsplash.com/photo-1505740420928-5e560c06d30e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8aGVhZHBob25lc3xlbnwwfHwwfHw%3D&w=1000&q=80","5"));
        products.add(new Product("2", "DSLR Camera", "130", "https://www.prophotostudio.net/wp-content/uploads/2021/02/best-camera-photos1-scaled.jpeg","5"));
        products.add(new Product("3","Sofa","79.99","https://media.istockphoto.com/photos/turquoise-arm-chair-isolated-on-white-background-front-view-of-picture-id1199428736?k=20&m=1199428736&s=612x612&w=0&h=vRS-zg2d6tF7jqQ8lI3oYFs_JC3fXdPCZhkvlEhHJkc=","4"));
        products.add(new Product("4", "DSLR Camera", "129.99", "https://www.prophotostudio.net/wp-content/uploads/2021/02/best-camera-photos1-scaled.jpeg","3"));
        products.add(new Product("5","Sofa","79.99","https://media.istockphoto.com/photos/turquoise-arm-chair-isolated-on-white-background-front-view-of-picture-id1199428736?k=20&m=1199428736&s=612x612&w=0&h=vRS-zg2d6tF7jqQ8lI3oYFs_JC3fXdPCZhkvlEhHJkc=","5"));
        products.add(new Product("6","Modern Headphone","44.99","https://images.unsplash.com/photo-1505740420928-5e560c06d30e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8aGVhZHBob25lc3xlbnwwfHwwfHw%3D&w=1000&q=80","4"));
        products.add(new Product("7", "DSLR Camera", "129.99", "https://www.prophotostudio.net/wp-content/uploads/2021/02/best-camera-photos1-scaled.jpeg","4"));
        products.add(new Product("8","Modern Headphone","44.99","https://images.unsplash.com/photo-1505740420928-5e560c06d30e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8aGVhZHBob25lc3xlbnwwfHwwfHw%3D&w=1000&q=80","3"));


        // Set RecyclerView Adapter
        productAdapter = new ProductAdapter(MainActivity.this,products);
        binding.productRecyclerView.setAdapter(productAdapter);
    }

}