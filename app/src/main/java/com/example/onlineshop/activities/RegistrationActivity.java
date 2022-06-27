package com.example.onlineshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.onlineshop.MainActivity;
import com.example.onlineshop.LoginActivity;
import com.example.onlineshop.databinding.ActivityRegistrationBinding;
import com.example.onlineshop.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private ActivityRegistrationBinding binding;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private ProgressDialog dialogue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialogue = new ProgressDialog(this);
        dialogue.setMessage("Setting User Profile...Be patient");
        dialogue.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }


//@@@@@@@( Click Listeners )@@@@@@@

    public void onClickSignupBtn(View view){
        // When Sign Up Button clicked

        String fullName = binding.etFullName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String phoneNumber = binding.etPhoneNumber.getText().toString();
        String password = binding.etPassword.getText().toString();
        registerUser();
    }

    public void onClickAlreadyHaveAccount(View view){
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        finish();
    }

    public void registerUser(){
        // User Registration and Validation

        String fullName = binding.etFullName.getText().toString();
        String email = binding.etEmail.getText().toString();
        String phoneNumber = binding.etPhoneNumber.getText().toString();
        String password = binding.etPassword.getText().toString();

        if(fullName.isEmpty()){
            binding.etFullName.setError("Name is required");
            binding.etFullName.requestFocus();
            return;
        }

        if(email.isEmpty()){
            binding.etEmail.setError("Email is required");
            binding.etEmail.requestFocus();
            return;
        }

        if(phoneNumber.isEmpty()){
            binding.etPhoneNumber.setError("Phone number is required");
            binding.etPhoneNumber.requestFocus();
            return;
        }

        if(password.isEmpty()){
            binding.etPassword.setError("Password is required");
            binding.etPassword.requestFocus();
            return;
        }

        if(password.length()<6){
            binding.etPassword.setError("Password must contain at least 6 characters");
            binding.etPassword.requestFocus();
            return;
        }

        dialogue.show();
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegistrationActivity.this, "User registered successfully " + auth.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();

                            String id = auth.getCurrentUser().getUid();
                            String fullName = binding.etFullName.getText().toString();
                            String email = auth.getCurrentUser().getEmail();
                            String phoneNumber = binding.etPhoneNumber.getText().toString();
                            String password = binding.etPassword.getText().toString();

                            User user = new User(id,fullName,email,phoneNumber,password);

                            database.getReference().child("Users").child(id).setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                dialogue.dismiss();
                                                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public boolean isEmailAlreadyExists(){
        boolean flag = true;

        return flag;
    }
}