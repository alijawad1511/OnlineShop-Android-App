package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.onlineshop.databinding.ActivityAddProductBinding;

public class AddProductActivity extends AppCompatActivity {

    ActivityAddProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch(item.getItemId())
        {
            case(R.id.cart):
                Toast.makeText(this, "Cart Opened", Toast.LENGTH_SHORT).show();
                break;
            case (R.id.logout):
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}