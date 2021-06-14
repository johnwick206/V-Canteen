package com.example.canteenapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ItemHolder> {

    public static List<FoodItemInfo> foodItemInfos1 = new ArrayList<>();
    private Context context;
    int singleItemCost;
    String i;


    public CartAdapter(List<FoodItemInfo> foodItemInfos , Context context) {
      //foodItemInfos1 = foodItemInfos ;
        foodItemInfos1 = foodItemInfos;
        this.context = context;
    }


    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item , parent , false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, final int position) {


        holder.name.setText(foodItemInfos1.get(position).getItemName());
        holder.price.setText(foodItemInfos1.get(position).getCost());
        Picasso.get()
                .load(foodItemInfos1.get(position)
                .getUri()).into(holder.cartItemImg);


        foodItemInfos1.get(position).setContext(context); //set context required for sharePrefs
        //Load data
        loadSharedPref(holder, position);

         singleItemCost = Integer.parseInt(foodItemInfos1.get(position).getCost());
        int totalCost =  singleItemCost *Integer.parseInt(foodItemInfos1.get(position).getItemQty());
        listener.updatePrice(totalCost);

       holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
           @Override
           public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                foodItemInfos1.get(position).setItemQty(Integer.toString(newValue)); //store each item quantity

               singleItemCost = Integer.parseInt(foodItemInfos1.get(position).getCost()); // individual item total pricing

               listener.updatePrice((newValue - oldValue)*singleItemCost);
               //if count of single item is zero then remove it from list
               if(holder.elegantNumberButton.getNumber().equals("0")){
                   listener.itemCountZero(foodItemInfos1.get(position));
               }
           }
       });

    }

    @Override
    public int getItemCount() {
        return foodItemInfos1.size();
    }

    private void loadSharedPref(ItemHolder holder , int position) {
        holder.elegantNumberButton.setNumber(foodItemInfos1.get(position).getItemQty());
    }


    public static class ItemHolder extends RecyclerView.ViewHolder{

        TextView name , price;
        ImageView cartItemImg;
        ElegantNumberButton elegantNumberButton ;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cartT1);
            price = itemView.findViewById(R.id.cartT2);
            cartItemImg = itemView.findViewById(R.id.itemImgCart);
            elegantNumberButton = itemView.findViewById(R.id.itemQtyBtn);
        }
    }

   static OnCountZero listener;
   public interface OnCountZero{
        void itemCountZero(FoodItemInfo foodItemInfo);
        void updatePrice(int price);
    }


    public static void setItemToZero(OnCountZero onCountZero ){
        listener = onCountZero;
    }
}
