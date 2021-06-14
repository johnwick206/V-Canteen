package com.example.canteenapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class FoodItemAdapter extends FirestoreRecyclerAdapter<FoodItemInfo , FoodItemAdapter.ViewAdapter> {


    private static OnClickToPopBottomSheet listener1;
    private static OnPopDialogBox listenerForDialog;
    List<FoodItemInfo> itemInfo;

    public FoodItemAdapter(@NonNull FirestoreRecyclerOptions<FoodItemInfo> options ) {
        super(options);
        itemInfo = Cart_fragment.cartList;
    }

   /* public FoodItemAdapter(List<FoodItemInfo> searchedItems) {
    }*/

    @Override
    protected void onBindViewHolder(@NonNull final ViewAdapter holder, final int position, @NonNull final FoodItemInfo model) {
        String nameAndCatagory = model.getItemName() +", " + model.getCatagory();
        String cost = "Rs.  " + model.cost;
        holder.foodItemName.setText(model.getItemName());
        holder.foodLocation.setText(model.getItemLocation());
        holder.category.setText(model.getCatagory());
        holder.costText.setText(cost);


        Picasso.get()
                .load(model.getUri())
                .into(holder.itemImage);

        for(FoodItemInfo items : Cart_fragment.cartList){
            if(items.itemName.equals(model.getItemName())){
                System.out.println("color is red");
                setButton(holder);
            }
        }


        //onClick Add button
         holder.addItemButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 //at a time user can order from only one place
                 if(itemInfo.size()>0 && !itemInfo.get(0).getItemLocation().equals(model.getItemLocation())) {
                     String dialogMsg = "You are at "+ itemInfo.get(0).getItemLocation() + "\n" +
                             "You cannot order at the same time from " + model.getItemLocation();
                     listenerForDialog.setDialogBoxMsg(dialogMsg);
                     return;
                 }
                 if (listener1 != null) {
                     listener1.addToCart(model);
                     setButton(holder);
                     return;
                 }
                 Cart_fragment.cartList.add(model);
                 setButton(holder);
             }
         });

         holder.removeButton.setOnClickListener(new View.OnClickListener() {
             @RequiresApi(api = Build.VERSION_CODES.N)
             @Override
             public void onClick(View v) {
                 if(listener1 != null){
                     listener1.removeFromCart(model);

                     holder.removeButton.setEnabled(false);
                     holder.addItemButton.setEnabled(true);
                     return;
                 }

                 Cart_fragment.cartList.removeIf(new Predicate<FoodItemInfo>() {
                     @Override
                     public boolean test(FoodItemInfo cart) {
                         return cart.getItemName().equals(model.getItemName());
                     }
                 });
                 holder.removeButton.setEnabled(false);
                 holder.addItemButton.setEnabled(true);
             }
         });

    }

    public void setButton(ViewAdapter viewAdapter){
        viewAdapter.removeButton.setEnabled(true);
        viewAdapter.addItemButton.setEnabled(false);
    }

    View view ;
    @NonNull
    @Override
    public ViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_card , parent, false);
        return new ViewAdapter(view);
    }

       static class ViewAdapter extends RecyclerView.ViewHolder {

         TextView foodItemName, foodLocation , costText , category;
         ImageView itemImage;
         Button addItemButton ,removeButton;
       public ViewAdapter(@NonNull View itemView) {
           super(itemView);
           foodItemName = itemView.findViewById(R.id.foodName);
           foodLocation = itemView.findViewById(R.id.foodLocation);
           category = itemView.findViewById(R.id.category);
           costText = itemView.findViewById(R.id.cost);
           itemImage = itemView.findViewById(R.id.imageView);
           addItemButton = itemView.findViewById(R.id.addItemButton);
           removeButton = itemView.findViewById(R.id.removeButton);
       }
   }

   // interface to communicate with cart Fragment
   public interface OnClickToPopBottomSheet{
         void addToCart(FoodItemInfo foodItemInfo);
       void removeFromCart(FoodItemInfo foodItemInfo);
   }

   public interface OnPopDialogBox{
        void setDialogBoxMsg(String text);
   }

   public static void setToPopDialogBox(OnPopDialogBox onPopDialogBox){
        listenerForDialog = onPopDialogBox;
   }

   public static void setClickListenerForBottomSheet(OnClickToPopBottomSheet listener){
       listener1 =(OnClickToPopBottomSheet) listener;
   }
}
