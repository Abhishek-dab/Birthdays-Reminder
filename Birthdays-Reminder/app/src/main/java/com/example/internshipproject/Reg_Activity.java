package com.example.internshipproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class Reg_Activity extends AppCompatActivity {
EditText et_mail, et_pass;
Button reg;
TextView tv;
FirebaseAuth firebaseAuth;
CheckBox checkBox;
    String tex,pa;
ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_reg_);
        et_mail = findViewById(R.id.reg_mail);
        et_pass = findViewById(R.id.reg_pass);
        reg = findViewById(R.id.reg);

        progressBar=findViewById(R.id.log_progressBar);
        tv = findViewById(R.id.gotoregis);
        firebaseAuth  = FirebaseAuth.getInstance();

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = et_mail.getText().toString();
                 String f = et_pass.getText().toString();
                if (s.isEmpty()) {
                    et_mail.setError("Email is required");
                    return;
                } else if (f.length() < 6) {
                    et_pass.setError("Password must be 6 characters");
                    return;
                } else {
                    final FirebaseUser user= firebaseAuth.getCurrentUser();
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(s, f).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                if(!user.isEmailVerified()){
                                    Toast.makeText(Reg_Activity.this, "Mail Not Verified", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }else{
                                Toast.makeText(Reg_Activity.this, "Login success", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(Reg_Activity.this, MainActivity.class);
                                startActivity(in);
                                saveData();
                                finish();
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Reg_Activity.this, "Error! "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }


        });


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Reg_Activity.this, login.class);
                startActivity(intent);
                finish();
            }
        });
        loadData();
        updateViews();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("text",et_mail.getText().toString());
        editor.putString("pass",et_pass.getText().toString());
        editor.apply();
    }

    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared",MODE_PRIVATE);
        tex = sharedPreferences.getString("text","");
        pa=sharedPreferences.getString("pass","");
    }
    public void updateViews(){
        et_mail.setText(tex);
        et_pass.setText(pa);
    }

    public void forogtpass(View view) {
        final EditText resetMail = new EditText(view.getContext());
        final AlertDialog.Builder passwordreset = new AlertDialog.Builder(view.getContext());
        passwordreset.setTitle("Reset Password");
        passwordreset.setMessage("Enter Email to receive Reset Link");
        passwordreset.setView(resetMail);
        passwordreset.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String mail = resetMail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Reg_Activity.this, "Reset Link sent to your Mail", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Reg_Activity.this, "Error! Rest Link Not Sent"+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        passwordreset.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        passwordreset.create().show();
    }
}