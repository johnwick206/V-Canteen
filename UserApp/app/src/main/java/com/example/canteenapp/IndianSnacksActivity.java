package com.example.canteenapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class IndianSnacksActivity extends AppCompatActivity {

    FoodItemAdapter foodItemAdapter ;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indian_snacks);

        FirebaseFirestore fb = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = fb.collection("Category: Indian Snacks");

        //retrieved data from firebase
        Query query = collectionReference.orderBy("itemName",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<FoodItemInfo> options = new FirestoreRecyclerOptions.Builder<FoodItemInfo>().setQuery(query , FoodItemInfo.class).build();
        foodItemAdapter = new FoodItemAdapter(options);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foodItemAdapter);
        foodItemAdapter.notifyDataSetChanged();

        FoodItemAdapter.setToPopDialogBox(new FoodItemAdapter.OnPopDialogBox() {
            @Override
            public void setDialogBoxMsg(String text) {
                CustomDialogBox dialogBox = new CustomDialogBox(text);
                dialogBox.show(getSupportFragmentManager(), "well");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        foodItemAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        foodItemAdapter.stopListening();
    }
}