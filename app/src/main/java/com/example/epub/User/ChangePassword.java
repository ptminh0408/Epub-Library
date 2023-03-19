package com.example.epub.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.epub.Display.Display1;
import com.example.epub.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    Button btnChangePassword;
    EditText txtNewPassword;
    ProgressBar progressBar;

    FirebaseUser user;
    FirebaseAuth fAuth;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        txtNewPassword = (EditText) findViewById(R.id.txtNewPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePassword();
            }
        });

    }

    private void ChangePassword(){
        closeKeyboard();
        progressBar.setVisibility(View.VISIBLE);
        String newPassword = txtNewPassword.getText().toString().trim();

        if(newPassword.isEmpty()){
            txtNewPassword.requestFocus();
            txtNewPassword.setError("Please enter your new password");
            return;
        }

        if(newPassword.length() < 6){
            txtNewPassword.requestFocus();
            txtNewPassword.setError("Your password must be bigger than 6 characters");
            return;
        }

        user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChangePassword.this, "Your password has changed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ChangePassword.this, Display1.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChangePassword.this, "Change password failed", Toast.LENGTH_SHORT).show();
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