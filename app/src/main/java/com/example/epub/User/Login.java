package com.example.epub.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epub.R;
import com.example.epub.Display.Display1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity implements View.OnClickListener{

    //Declare variable
    private EditText txtEmail;
    private EditText txtPassword;
    private CheckBox cbRememberMe;
    private Button btnSignIn, btnSignUp;
    private TextView txtForgotPassword;
    private ProgressBar progressBar;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Register view
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);

        txtForgotPassword = (TextView) findViewById(R.id.txtForgot);
        txtForgotPassword.setOnClickListener(this);

        //Remember me
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox = preferences.getString("remember", "");

        if(checkbox.equals("true")){
            startActivity(new Intent(Login.this, Display1.class));
            finish();
        }
        else if(checkbox.equals("false")){

        }

        cbRememberMe = (CheckBox) findViewById(R.id.cbRememberMe);
        cbRememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(compoundButton.isChecked()){

                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                }
                else if(!compoundButton.isChecked()){

                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                }
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        fAuth =  FirebaseAuth.getInstance();
    }

    //set on click event
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btnSignUp:
                startActivity(new Intent(Login.this, SignUp.class));
                break;
            case R.id.btnSignIn:
                userLogin();
                break;
            case R.id.txtForgot:
                startActivity(new Intent(Login.this, ForgotPassword.class));
                break;
        }
    }

    //Login
    private void userLogin() {
        String email = txtEmail.getText().toString().trim();
        String password = txtPassword.getText().toString().trim();

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

        progressBar.setVisibility(View.VISIBLE);
        closeKeyboard();

        fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified()){
                        progressBar.setVisibility(View.GONE);
                        startActivity(new Intent(Login.this, Display1.class));
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Login.this, "Your account hasn't been verify yet", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(Login.this, "Wrong Email or Password! Please try again.", Toast.LENGTH_SHORT).show();
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

    //close keyboard when click
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}