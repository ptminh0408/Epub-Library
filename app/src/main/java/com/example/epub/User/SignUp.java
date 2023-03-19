package com.example.epub.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.epub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText txtFullname, txtPassword, txtPasswordAgain, txtEmail;
    private Button btnSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth fAuth;
    private String userID;
    private FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtFullname = findViewById(R.id.txtFullname);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtPasswordAgain = findViewById(R.id.PasswordAgain);

        btnSignUp = findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSignUp:
                registerUser();
                break;
        }
    }

    private void registerUser() {

        String fullname = txtFullname.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String passwordAgain = txtPasswordAgain.getText().toString().trim();

        if(fullname.isEmpty()){
            txtFullname.setError("Full name is required");
            txtFullname.requestFocus();
            return;
        }

        if(email.isEmpty()){
            txtEmail.setError("Email is required");
            txtEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtEmail.setError("Email invalid");
            txtEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            txtPassword.setError("Password is required");
            txtPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            txtPassword.setError("Password must be bigger than 6 characters");
            txtPassword.requestFocus();
            return;
        }

        if(!passwordAgain.equals(password)){
            txtPasswordAgain.setError("Password not match");
            txtPasswordAgain.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        closeKeyboard();

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(task.isSuccessful()){
                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("Users").document(userID);

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("fullname", fullname);
                    userMap.put("email", email);
                    documentReference.set(userMap);
                    progressBar.setVisibility(View.GONE);


//                    HashMap<String, String> userMap = new HashMap<>();
//
//                    userMap.put("name", fullname);
//                    userMap.put("email", email);
//
//                    root.push().setValue(userMap);

                    if(user.isEmailVerified()){
                        Toast.makeText(SignUp.this, "Email has been registerd", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    else{
                        user.sendEmailVerification();
                        startActivity(new Intent(SignUp.this, Login.class));
                        Toast.makeText(SignUp.this, "Email has been sent. Please check your email to verify your account", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    Toast.makeText(SignUp.this, "Register Failed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}