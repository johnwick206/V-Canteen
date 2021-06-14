package com.example.canteenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginAccount extends AppCompatActivity {

    Button loginButton;
    EditText emailId ;
    EditText password ;
    String emailIdText , passwordText ;
    FirebaseAuth mauth ;
    FirebaseUser user ;
    FirebaseFirestore fireStore;
    DocumentReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);
        emailId = findViewById(R.id.loginId);
        password = findViewById(R.id.loginPassword);
        mauth = FirebaseAuth.getInstance() ;
        user = mauth.getCurrentUser();
        fireStore = FirebaseFirestore.getInstance();

    }

    public void moveToSignUpPage(View v){
        Toast.makeText(this, "Sign Up here", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginAccount.this , MainActivity.class));
        finish();
    }

    public void loginProcess(View view) {

        emailIdText = emailId.getText().toString();
        passwordText = password.getText().toString();

        if(TextUtils.isEmpty(emailIdText)) {
            emailId.setError("enter your id");
            return;
        }
        if(TextUtils.isEmpty(passwordText)) {
            password.setError("enter your id");
            return;
        }
        // pop dialogBox
          final VerificationDialogBox verificationDialogBox = new VerificationDialogBox(this , "Please Wait");

            mauth.signInWithEmailAndPassword(emailIdText ,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    /*if(!user.isEmailVerified()){
                        Toast.makeText(LoginAccount.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
                        verificationDialogBox.closeDialogBox();
                        return;
                    }*/

                    if (task.isSuccessful() ) {
                        Toast.makeText(LoginAccount.this, "login Successful...", Toast.LENGTH_SHORT).show();
                        getuserData();
                        startActivity(new Intent(LoginAccount.this, ChooseOption.class));
                        verificationDialogBox.closeDialogBox();
                        finish();
                    } else{
                        Toast.makeText(LoginAccount.this, "Check password/email", Toast.LENGTH_SHORT).show();
                        verificationDialogBox.closeDialogBox();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println(e.getMessage());
                }
            });

    }

    private void getuserData(){

        reference = fireStore.collection("User").document(mauth.getCurrentUser().getUid());

        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    UserData.name = documentSnapshot.getString("name");
                    UserData.emailId = documentSnapshot.getString("emailId");
                    UserData.phoneNumber = documentSnapshot.getString("phoneNumber");
                }else Toast.makeText(LoginAccount.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginAccount.this, "unsucessful", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
