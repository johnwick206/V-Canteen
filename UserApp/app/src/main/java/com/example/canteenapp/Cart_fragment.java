package com.example.canteenapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;


public class Cart_fragment extends Fragment implements FoodItemAdapter.OnClickToPopBottomSheet , CartAdapter.OnCountZero {

    public static List<FoodItemInfo> cartList =new ArrayList<>();
    View view;
    CartAdapter cartAdapter;
    RecyclerView cartRecyclerCiew;
    SharedPreferences sharedPreferences;
    TextView totalPriceText;
    ImageView bgImg;
    int updateVal = 0;
    VerificationDialogBox verificationDialogBox ;

    private static boolean firstTime = true;

    private Button proceedToPayBtn;


    /*public Cart_fragment() {
        // Required empty public constructor
    }*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_cart_fragment, container, false);

        totalPriceText = view.findViewById(R.id.totalAmount);
        bgImg = view.findViewById(R.id.bgImgId);
        proceedToPayBtn = view.findViewById(R.id.proceedToPayBtn);

       recyclerViewItems();


        sharedPreferences  = getContext().getSharedPreferences("Pref", Context.MODE_PRIVATE);


        FoodItemAdapter.setClickListenerForBottomSheet(this);
        CartAdapter.setItemToZero(this);

        if(cartList.size() > 0) bgImg.setVisibility(View.INVISIBLE);

        proceedToPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(cartList.size() ==0)
                   Toast.makeText(getContext(), "No item inside Cart", Toast.LENGTH_SHORT).show();
                  else payBill();
            }
        });
        return view;
    }


    private void payBill(){
        //Loading box during payment
         verificationDialogBox = new VerificationDialogBox(getActivity() , "Please Wait");

        // TODO: 09-12-2020 Gpay code : remove comment
       /* String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
        int GOOGLE_PAY_REQUEST_CODE = 123;

        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa", "omaxbullet@oksbi")
                        .appendQueryParameter("pn", "Canteen Admin")
                       // .appendQueryParameter("mc", "your-merchant-code")
                       // .appendQueryParameter("tr", "your-transaction-ref-id")
                        .appendQueryParameter("tn", "Canteen Payment")
                        .appendQueryParameter("am", "2")
                        .appendQueryParameter("cu", "INR")
                     //   .appendQueryParameter("url", "your-transaction-url")
                        .build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);*/
        storeInDatabase();
    }

    int i; //local variable for database storage
    private void storeInDatabase() {
        CollectionReference  firestoreColRef  = FirebaseFirestore.getInstance().
                                                collection(cartList.get(0).getItemLocation()+"Orders"); //place to Vlounge orders or Vcanteen order
        //referencing document
        DocumentReference document = firestoreColRef.document(generateId());


        Map<String , Object> element = new HashMap<>();

        element.put("time", getCurrentTime());
        element.put("priority" , System.currentTimeMillis());
        element.put("location", cartList.get(0).getItemLocation());
        element.put("total", totalPriceText.getText().toString());
        element.put("phoneNo" , UserData.phoneNumber);
        element.put("userName", UserData.name);

        StringBuilder itemNameAndQty = new StringBuilder();

        for ( i = 0 ; i < cartList.size() ; i ++)
            itemNameAndQty.append(cartList.get(i).getItemName()).append(": ").append(cartList.get(i).getItemQty() +"\n");


        element.put("userCartItem" , itemNameAndQty.toString());

        document.set(element).addOnCompleteListener(new OnCompleteListener<Void>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Order placed" , Toast.LENGTH_SHORT).show();
                    for ( i = cartList.size() -1 ; i >= 0 ; i --)
                        removeFromCart(cartList.get(i));

                    totalPriceText.setText("0");
                }
                else  Toast.makeText(getContext(), "try again", Toast.LENGTH_SHORT).show();

                verificationDialogBox.closeDialogBox();
            }
        });
    }

    private void recyclerViewItems(){
        cartRecyclerCiew = view.findViewById(R.id.cartRecyclerView);
        cartRecyclerCiew.setHasFixedSize(true);
        cartAdapter = new CartAdapter(cartList , getContext());
        cartRecyclerCiew.setLayoutManager(new LinearLayoutManager(getContext()));
        cartRecyclerCiew.setAdapter(cartAdapter);
    }


    private String generateId(){
        long milliSec = System.currentTimeMillis();
        Calendar c = Calendar.getInstance();
        String date = DateFormat.getDateInstance().format(c.getTime());
        String id = UserData.name + milliSec + date ;

        return id;
    }

    private String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        String time = DateFormat.getTimeInstance().format(c.getTime());
        return time;
    }

    @Override
    public void addToCart(FoodItemInfo foodItemInfo) {
        cartList.add(foodItemInfo);
        cartAdapter.notifyDataSetChanged();

    }


    //Removes item from List
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void removeFromCart(final FoodItemInfo foodItemInfo) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(foodItemInfo.getItemName());
        editor.apply();


        cartList.removeIf(new Predicate<FoodItemInfo>() {
            @Override
            public boolean test(FoodItemInfo cart) {
                return cart.getItemName().equals(foodItemInfo.getItemName());
            }
        });
        updateVal = 0;
        cartAdapter.notifyDataSetChanged(); // calling all price values when we made changes to item

        if(cartList.size() == 0) setBackgrund();
    }


    //remove from cart when count = 0
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void itemCountZero(FoodItemInfo foodItemInfo) {
        removeFromCart(foodItemInfo);
    }


    //set empty cart background image
    private void setBackgrund() {
        bgImg.setVisibility(View.VISIBLE);
    }

    @Override
    public void updatePrice(int price) {
        updateVal += price;
        totalPriceText.setText(String.valueOf(updateVal));
    }
}
