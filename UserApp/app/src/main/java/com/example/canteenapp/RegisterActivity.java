package com.example.canteenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{


   private EditText nameText , emailText , passwordText , phoneNoText;
     Button registerButton ;
     String emailId ,password, phoneNo;
    private  String name ;
    public static FirebaseAuth mAuth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        passwordText= findViewById(R.id.passwordText);
        registerButton = findViewById(R.id.registerButton);
        phoneNoText = findViewById(R.id.phoneNo);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            //if we click on register button
            case R.id.registerButton:

                //store entered values inside string
                name = nameText.getText().toString();
                emailId = emailText.getText().toString().trim();
                password = passwordText.getText().toString().trim();
                phoneNo = phoneNoText.getText().toString().trim();

                //enter name
                if (TextUtils.isEmpty(name)) {
                    nameText.setError("enter your name");
                    return;
                }

                //enter phone number
                if (TextUtils.isEmpty(phoneNo)) {
                    phoneNoText.setError("enter phone Number");
                    return;
                }

                if(phoneNo.length() > 10){
                    phoneNoText.setError("maximum character length is 10");
                    return;
                }

                // enter valid mail id
                if (TextUtils.isEmpty(emailId)) {
                    emailText.setError("enter your id");
                    return;
                }

                //is password is incomplete
                if (TextUtils.isEmpty(password)) {
                    passwordText.setError("enter password");
                    return;
                }

                //password length should be more than 8
                if (password.length() < 8) {
                    passwordText.setError("password should  contain least 8 characters");
                    return;
                }

                //register new client
                mAuth.createUserWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();

                            assert user != null;
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    new UserData(name , phoneNo , emailId);

                                    Intent intent =  new Intent(RegisterActivity.this, PhoneVerification.class);
                                    intent.putExtra("Phone Number" , phoneNo );
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                    }
                }); break;

            default: break ;

        }
    }
}
