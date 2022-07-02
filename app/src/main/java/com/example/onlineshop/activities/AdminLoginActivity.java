package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.onlineshop.AdminDashboardActivity;
import com.example.onlineshop.databinding.ActivityAdminLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminLoginActivity extends AppCompatActivity {

    ActivityAdminLoginBinding binding;
    private FirebaseAuth auth;
    private ProgressDialog dialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Database
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser()!=null && auth.getCurrentUser().getEmail().contains("@onlineshop.com")){
            Intent intent = new Intent(AdminLoginActivity.this, AdminDashboardActivity.class);
            startActivity(intent);
            finish();
        }

        // Dialog
        dialogue =  new ProgressDialog(this);
        dialogue.setMessage("Signing in...");
    }

    public void onClickNotAdmin(View view) {
        startActivity(new Intent(AdminLoginActivity.this, com.example.onlineshop.LoginActivity.class));
        finish();
    }

    public void onClickLoginBtn(View view) {
        loginAdmin();
    }

    public void onClickForgotPassword(View view) {
        // Forgot Password Code Here...
    }

    public void loginAdmin(){
        String email = binding.etEmail.getText().toString();
        String password = binding.etPassword.getText().toString();

        if(email.isEmpty()){
            binding.etEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            binding.etPassword.setError("Password is required");
            binding.etPassword.requestFocus();
            return;
        }

        if(!email.equals("admin@onlineshop.com")){
            Toast.makeText(this, "Invalid credentials! You are not an admin.", Toast.LENGTH_SHORT).show();
            return;
        }

        dialogue.show();
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            dialogue.dismiss();
                            startActivity(new Intent(AdminLoginActivity.this, AdminDashboardActivity.class));
                            finish();
                        }else{
                            // In case of error
                            dialogue.dismiss();
                            Toast.makeText(AdminLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}