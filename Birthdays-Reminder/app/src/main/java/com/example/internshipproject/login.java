package com.example.internshipproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    EditText username,password;
    TextView register;
    FirebaseAuth auth;
    Button save;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        username=findViewById(R.id.emaill);
        password=findViewById(R.id.password);
        register=findViewById(R.id.register);
        save=findViewById(R.id.login);
        auth=FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String pass = password.getText().toString();
                if (email.isEmpty()) {
                    username.setError("Email is required");
                    return;
                } else if (pass.length() < 6) {
                    password.setError("Password must be 6 characters");
                    return;
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = auth.getCurrentUser();
                                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(login.this, "Verification Email has been sent. Please Verify your account", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });


                                Toast.makeText(login.this, "New User Created", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(login.this, Reg_Activity.class);
                                startActivity(in);
                                finish();

                            } else {
                                Toast.makeText(login.this, "Error! "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                            }

                        }
                    });
                }
            }
        });
register.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

            Intent in = new Intent(login.this, Reg_Activity.class);
            startActivity(in);
            finish();

    }
});

    }


}
