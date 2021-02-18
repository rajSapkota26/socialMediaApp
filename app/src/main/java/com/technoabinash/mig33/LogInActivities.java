package com.technoabinash.mig33;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivities extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText userEmail, userPassword;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_activities);
        mAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.etx_log_email);
        userPassword = findViewById(R.id.etx_log_password);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Log in Account");
        progressDialog.setMessage("we're logging your account...");
    }

    public void gofromLogtoRegInActivities(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivities.class));
    }

    public void loggedIn(View view) {
        progressDialog.show();
        String email, password;
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(email)) {
            progressDialog.dismiss();
            Toast.makeText(this, "All fields are required...", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                progressDialog.dismiss();

                                Toast.makeText(LogInActivities.this, "Update profile for better experience", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LogInActivities.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(LogInActivities.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}