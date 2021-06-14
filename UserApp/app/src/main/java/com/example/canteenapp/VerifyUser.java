package com.example.canteenapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class VerifyUser extends AppCompatActivity {

    Button refreshButton ;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mauth ;
    FirebaseUser user ;
    String userId ,nameOfUser , phoneNumber;
    TextView  userInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_user);

        //UI instances
        userInformation = findViewById(R.id.msgToUser);
        refreshButton = findViewById(R.id.refresh);
        nameOfUser = UserData.name;
        phoneNumber = UserData.phoneNumber;

        //display first name only
        String displayName = nameOfUser;


            int index = displayName.indexOf(' ');
            if(index == 0)
            displayName = displayName.substring(0, index);
            else
                displayName = nameOfUser ;



        String userInformationToDisplay = "Hey "+ displayName +"...!!\nKindly check your E-mail to Verify ";
        userInformation.setText(userInformationToDisplay);
        userInformation.setVisibility(View.VISIBLE);
        //firebase related instances
         mauth = FirebaseAuth.getInstance();
         user = mauth.getCurrentUser();
         firebaseFirestore = FirebaseFirestore.getInstance();

    }

    //onclick refresh button
    public void verifedOrNot(View view){

        final VerificationDialogBox verificationDialogBox = new VerificationDialogBox(this , "Verifying...");
       user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
           @Override
           public void onSuccess(Void aVoid) {
               if (user.isEmailVerified()) {
                   Toast.makeText(VerifyUser.this, "Verified", Toast.LENGTH_SHORT).show();
                   sendDataToFireStore();
                   verificationDialogBox.closeDialogBox();
               } else {
                   verificationDialogBox.closeDialogBox();
                   Toast.makeText(VerifyUser.this, "Verify first", Toast.LENGTH_SHORT).show();
               }
           }
       });
    }


    //store data to fireStore
    private void sendDataToFireStore(){

            userId = user.getUid();
            DocumentReference documentReference = firebaseFirestore.collection("User").document(userId);
            Map<String, Object> userInformation = new HashMap<>();
        userInformation.put("name", nameOfUser);
        userInformation.put("emailId", user.getEmail());
        userInformation.put("phoneNumber",phoneNumber);

            //send name email id to database fireStore
            documentReference.set(userInformation).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(VerifyUser.this, "Account Verified", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(VerifyUser.this , ChooseOption.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(VerifyUser.this, "Error =" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

    }
}
