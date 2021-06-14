package com.example.miniproj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class VLoungeOrders extends AppCompatActivity {

   private FirebaseFirestore fireStore;
   private CollectionReference reference;
   private RecyclerView recyclerViewOrders;
   private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vlounge_orders);

        firebaseElement();

        Query query = reference.orderBy("priority" ,Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<OrderModel> options = new FirestoreRecyclerOptions.Builder<OrderModel>()
                .setQuery(query , OrderModel.class )
                .build();


        adapter = new OrderAdapter(options ,this);
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);
        recyclerViewOrders.setHasFixedSize(true);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }


    private void firebaseElement(){
        fireStore = FirebaseFirestore.getInstance();
        reference = fireStore.collection("VLoungeOrders");
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}