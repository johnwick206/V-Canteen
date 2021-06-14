package com.example.miniproj;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

	private Button clickButton;
	private EditText userNameText;
	private EditText userEmailIdText;
	private EditText userPasswordText;
	FirebaseAuth mAuth ;
	private String userName;
	private String userEmailId;
	private String userPassword;


	@Override
	protected void onStart() {
		super.onStart();
		if (mAuth.getCurrentUser() != null) moveToNextActivity();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		mAuth = FirebaseAuth.getInstance();
		userNameText= findViewById(R.id.nameText);
		userEmailIdText= findViewById(R.id.emailText);
		userPasswordText= findViewById(R.id.passwordText);

		clickButton = (Button) findViewById(R.id.registerButton);

		clickButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				userName = userNameText.getText().toString();
				userEmailId = userEmailIdText.getText().toString();
				userPassword = userPasswordText.getText().toString();

				if(TextUtils.isEmpty(userName))
				{
					userNameText.setError("Enter your Name");
					return;
				}
				if(TextUtils.isEmpty(userEmailId))
				{
					userEmailIdText.setError("Enter Email Id");
					return;
				}
				if(TextUtils.isEmpty(userPassword))
				{
					userPasswordText.setError("Enter your password");
					return;
				}
				if(userPassword.length() < 6)
				{
					userNameText.setError("Password should contain at least 6 characters");
					return;
				}
				mAuth.createUserWithEmailAndPassword(userEmailId , userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
					@Override
					public void onSuccess(AuthResult authResult) {
						Toast.makeText(MainActivity.this, "Sign Up Complete", Toast.LENGTH_SHORT).show();
						moveToNextActivity();
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						Toast.makeText(MainActivity.this, "Connection Issue Maybe", Toast.LENGTH_SHORT).show();
					}
				});

				/*Intent intent = new Intent(MainActivity.this, MainActivity2.class);
				startActivity(intent);*/

			}
		});

	}
	void moveToNextActivity()
	{
		Intent intent = new Intent(MainActivity.this, MainActivity2.class);
		startActivity(intent);
		finish();
	}

}
