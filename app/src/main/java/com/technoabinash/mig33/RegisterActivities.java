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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivities extends AppCompatActivity {
    //variable in layout file
    private EditText username, useremail, fullname, userpassword;
    //for firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDataRef;
    //for dialogue box
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activities);
        //call method for initialize variable
        initVariables();

    }

    private void initVariables() {

        //laout initializa
        username = findViewById(R.id.etx_sign_username);
        useremail = findViewById(R.id.etx_sign_email);
        fullname = findViewById(R.id.etx_sign_phone);
        userpassword = findViewById(R.id.etx_sign_password);
        //fire base
        mAuth = FirebaseAuth.getInstance();
        mDataRef = FirebaseDatabase.getInstance().getReference();
        //dialogue
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("we're Creating your account");
    }

    //this methd is onclick method on button
    public void goFirebaseRegisterAccount(View view) {
        //getting value in string form
        String uname, email, fname, password;
        email = useremail.getText().toString();
        password = userpassword.getText().toString();
        uname = username.getText().toString();
        fname = fullname.getText().toString();
        //checking all field has some value
        if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email) || TextUtils.isEmpty(fname)) {
            Toast.makeText(this, "All fields are required...", Toast.LENGTH_SHORT).show();
        }
        //checking password is large then 6 character
        else if (password.length() < 6) {
            Toast.makeText(this, "password too short", Toast.LENGTH_SHORT).show();
        } else {
            //register being proceed
            registerinStore(uname, email, fname, password);
            //   registerInFireStore(name,email,phone,password);


        }
    }

    //    private void registerInFireStore(String name, String email, String phone, String password) {
//        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//            }
//        });
//    }
    // processing register
    private void registerinStore(String userName, String email, String fullName, String password) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("username", userName);
                map.put("email", email);
                map.put("fullName", fullName);
                map.put("password", password);
                map.put("id", mAuth.getCurrentUser().getUid());
                map.put("Bio","");
                map.put("ImageUrl","default");
                //store data in database
                mDataRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            //redirect user to main page
                            Toast.makeText(RegisterActivities.this, "Update profile for better experience", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivities.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivities.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void gofromRegtoLogInActivities(View view) {
        startActivity(new Intent(getApplicationContext(), LogInActivities.class));
    }


}