package com.example.miniproj;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Addnewproduct extends AppCompatActivity implements DialogBox.DialogListener , View.OnClickListener {
	private String Categoryname ,cost, downloadedUri ,currentItemName ,itemLocation ,foodCategory;
	private TextView optionText , categoryText;
	EditText itemName , itemCost;
	Button addItemButton;
	private String[] listOnDialogBox ;
	private int localInt ;
	FirebaseFirestore firestore;
	StorageReference parentdoc;
	ImageView itemImage;
	Uri imageUri;

	ProgressDialog dialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addnewproduct);

		optionText = findViewById(R.id.optionText);
		categoryText= findViewById(R.id.catagory);
		itemName = findViewById(R.id.itemName);
		itemCost = findViewById(R.id.itemCost);
		addItemButton = findViewById(R.id.addItemButton);
		itemImage = findViewById(R.id.image);


		firestore = FirebaseFirestore.getInstance();
		parentdoc = FirebaseStorage.getInstance().getReference("imagesFolder");

		optionText.setOnClickListener(this);
		categoryText.setOnClickListener(this);
		addItemButton.setOnClickListener(this);
		itemImage.setOnClickListener(this);
		/*FloatingActionButton floatingActionButton = findViewById(R.id.float_addnew);
		already_uploaded = (TextView) findViewById(R.id.uploaded);*/
		/*dialogBox.setCancelable(true);
		dialogBox.show(getSupportFragmentManager(),"Choose from here");*/


		Categoryname = getIntent().getExtras().get("category").toString();
		Toast.makeText(this,Categoryname, Toast.LENGTH_SHORT).show();
	}


	@Override
	public void clickedItem(String currentText) {
		switch(localInt){
			case 1: optionText.setText(currentText); break;
			case 2: categoryText.setText(currentText); break;
			default: break;
		}
	}

	//update UI
	@Override
	public void onClick(View view) {

		DialogBox dialogBox = new DialogBox(Addnewproduct.this);
		switch (view.getId()){
			case R.id.optionText : localInt = 1; setOptionText(dialogBox); break;

			case R.id.catagory : localInt = 2; setCategoryText(dialogBox); break;
			
			case R.id.image : uploadImage(); break;

			case R.id.addItemButton : addItemToFirestore(); break;
			default: break;
		}
	}


	private void uploadImage() {
		Intent galleryIntent =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
		startActivityForResult(galleryIntent, 1000);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && requestCode ==1000 && data != null){
			imageUri = data.getData();
			itemImage.setImageURI(imageUri);
		}
	}

	//select options from dialogBox // Vcanteen or VLounge
	void setOptionText(DialogBox dialogBox)
	{
		listOnDialogBox = getResources().getStringArray(R.array.VcOrVl);
		dialogBox.acceptArrayAndTitle(listOnDialogBox , "Location");
		dialogBox.setCancelable(true);
		dialogBox.show(getSupportFragmentManager() , "Dialog to Display");

	}
	void setCategoryText(DialogBox dialogBox)
	{
		listOnDialogBox = getResources().getStringArray(R.array.Category);
		dialogBox.acceptArrayAndTitle(listOnDialogBox , "Category");
		dialogBox.setCancelable(true);
		dialogBox.show(getSupportFragmentManager() , "Dialog to Display");
	}

	//adding item to firestore
	 void addItemToFirestore(){
		 currentItemName =  itemName.getText().toString();
		 itemLocation = optionText.getText().toString();
		 foodCategory = categoryText.getText().toString();
		 cost = itemCost.getText().toString();

		if(imageUri == null){
			Toast.makeText(this, "Kindly add Item Image", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(currentItemName)){
			itemName.setError("Give name to your Item");
			return;
		}
		if(TextUtils.isEmpty(cost) || Integer.parseInt(cost) <=0){
			itemCost.setError("Enter Valid ammount in Rs.");
			return;
		}
		if(TextUtils.isEmpty(itemLocation)){
			optionText.setError("choose Option");
			return;
		}
		 if(TextUtils.isEmpty(foodCategory)){
			 categoryText.setError("choose Option");
			 return;
		 }

		 dialog = new ProgressDialog(Addnewproduct.this);
		 dialog.setTitle("Add");
		 dialog.setMessage("Adding, please wait");
		 dialog.setCanceledOnTouchOutside(false);
		 dialog.show();

		  final StorageReference childImagedoc = parentdoc.child(currentItemName+".png");
		 final UploadTask uploadedImage = childImagedoc.putFile(imageUri);
		 uploadedImage.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
			 @Override
			 public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
				if(!task.isSuccessful()) {
					dialog.dismiss();
					throw task.getException();
				}
			 	return childImagedoc.getDownloadUrl();
			 }
		 }).addOnCompleteListener(new OnCompleteListener<Uri>() {
			 @Override
			 public void onComplete(@NonNull Task<Uri> task) {
				 if(task.isSuccessful()){
				 	Uri uploadUri = task.getResult();
				 	downloadedUri = uploadUri.toString();



					 final Date date = Calendar.getInstance().getTime();
					 HashMap<String , Object> singleItemInfo = new HashMap<>();
					 singleItemInfo.put("itemName" , currentItemName.toLowerCase());
					 singleItemInfo.put("itemLocation" , itemLocation);
					 singleItemInfo.put("catagory", foodCategory);
					 singleItemInfo.put("uri",downloadedUri);
					 singleItemInfo.put("cost", cost);
					 if(uploadedImage.isInProgress()) return;																 //date.toString()
					 DocumentReference documentReference = firestore.collection("Category: "+ foodCategory).document(currentItemName.toLowerCase());

					 //store document to fireStore
					 documentReference.set(singleItemInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
						 @Override
						 public void onSuccess(Void aVoid) {
							 //added
						 }
					 }).addOnFailureListener(new OnFailureListener() {
						 @Override
						 public void onFailure(@NonNull Exception e) {
							//failed
							 dialog.dismiss();
						 }
					 });

					 //common list for all dishes
					 DocumentReference documentReference1 = firestore.collection("All Dishes").document(currentItemName.toLowerCase());
					 documentReference1.set(singleItemInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
						 @Override
						 public void onSuccess(Void aVoid) {
							 Toast.makeText(Addnewproduct.this, "Added Successfully", Toast.LENGTH_SHORT).show();
						 }
					 }).addOnFailureListener(new OnFailureListener() {
						 @Override
						 public void onFailure(@NonNull Exception e) {
							 Toast.makeText(Addnewproduct.this, "error caused", Toast.LENGTH_SHORT).show();
						 }
					 });

				 }
				 else {
					 Toast.makeText(Addnewproduct.this, "In progress", Toast.LENGTH_SHORT).show();
				 }

				 dialog.dismiss();
			 }
		 });

	 }
}