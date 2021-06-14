package com.example.canteenapp;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class SearchFragment extends Fragment {

   /* public vcanteen_fragment() {
        // Required empty public constructor
    }*/

   Button loadDataBtn;
   SearchView searchView;
   FirebaseFirestore firestore;
   CollectionReference collectionReference;

  private RecyclerView recyclerView;
  private FoodItemAdapter foodItemAdapter;
  private   FirestoreRecyclerOptions<FoodItemInfo> options;
  private Query query;
  private ImageView fragmentBGImg;
  private TextView fragStateText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("All Dishes");

        View view = inflater.inflate(R.layout.search_fragment_layout, container, false);
        fragmentBGImg = view.findViewById(R.id.BGImg);
        fragStateText = view.findViewById(R.id.fragmentStateTextId);

         query = collectionReference.orderBy("itemName",Query.Direction.DESCENDING);
        options = new FirestoreRecyclerOptions.Builder<FoodItemInfo>().setQuery(query , FoodItemInfo.class).build();

//        foodItemAdapter = new FoodItemAdapter(options);

        recyclerView = view.findViewById(R.id.recyclerView_SearchItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(foodItemAdapter);
//        foodItemAdapter.notifyDataSetChanged();

        searchView = view.findViewById(R.id.searchBox);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
                searchData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return  view;
    }

    private void searchData(String searchText){
         query = collectionReference.whereGreaterThanOrEqualTo("itemName", searchText).limit(5);
         options = new FirestoreRecyclerOptions.Builder<FoodItemInfo>().setQuery(query , FoodItemInfo.class).build();
        foodItemAdapter = new FoodItemAdapter(options);
        recyclerView.setAdapter(foodItemAdapter);

        setBackground();

        foodItemAdapter.startListening();
        foodItemAdapter.notifyDataSetChanged();

    }

    private void setBackground(){

        if(query != null){
            fragmentBGImg.setVisibility(View.INVISIBLE);
            fragStateText.setVisibility(View.INVISIBLE);
        }else {// TODO: 23-09-2020 when search item is not found , manage image view
            fragmentBGImg.setBackgroundResource(R.mipmap.junk_food_img);
            fragmentBGImg.setVisibility(View.VISIBLE);
            fragStateText.setVisibility(View.VISIBLE);
            fragStateText.setText("NO Item Found");
        }
    }

   /* @Override
    public void onStart() {
        super.onStart();
        foodItemAdapter.startListening();
    }*/

    @Override
    public void onStop() {
        super.onStop();
        if(foodItemAdapter != null)
        foodItemAdapter.stopListening();
    }


}
