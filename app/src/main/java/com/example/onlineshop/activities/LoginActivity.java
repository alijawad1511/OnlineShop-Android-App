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
import com.example.onlineshop.RegistrationActivity;
import com.example.onlineshop.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private ProgressDialog dialogue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

//        if(auth.getCurrentUser()!=null){
//            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        dialogue =  new ProgressDialog(this);
        dialogue.setMessage("Signing in...");

    }

//@@@@@@@( Click Listeners )@@@@@@@

    public void onClickLoginBtn(View view){
        loginUser();
    }

    public void onClickNotHaveAccount(View view){
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        finish();
    }

    public void onClickForgotPassword(View view) {
        // Forgot Password Code Here...
    }

//@@@@@@@( Input Validations )@@@@@@@

    public void loginUser(){
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

        // Login After Complete Validation
        dialogue.show();
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            dialogue.dismiss();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }else{
                            // In case of error
                            dialogue.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

        dialogue.show();
        auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            dialogue.dismiss();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }else{
                            // In case of error
                            dialogue.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}