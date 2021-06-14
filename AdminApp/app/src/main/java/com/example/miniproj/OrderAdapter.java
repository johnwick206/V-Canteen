package com.example.miniproj;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class OrderAdapter extends FirestoreRecyclerAdapter<OrderModel , OrderAdapter.OrderHolder> {

    Activity activity;

    public OrderAdapter(@NonNull FirestoreRecyclerOptions<OrderModel> options , Activity activity) {
        super(options);
        this.activity = activity ;
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull final OrderModel model) {
        holder.orderData.setText(model.getUserCartItem());
        holder.totalCost.setText(model.getTotal());
        holder.userName.setText(model.getUserName());
        holder.orderTime.setText(model.getTime());
        holder.messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission(Manifest.permission.SEND_SMS)){
                    //send msg to user
                    sendMsg(model);
                }else{
                    ActivityCompat.requestPermissions(activity ,new String[] {Manifest.permission.SEND_SMS }, 1);
                }
            }
        });

    }

    private boolean checkPermission(String permission){
        int check = ContextCompat.checkSelfPermission( activity, permission);
        return check == PackageManager.PERMISSION_GRANTED ;
    }

    private void sendMsg(OrderModel model){
        SmsManager smsManager = SmsManager.getDefault();
        String msgText = messageFormat(model);
        smsManager.sendTextMessage("+91"+ model.getPhoneNo() , null ,msgText , null ,null );
        Toast.makeText(activity, "Message Sent", Toast.LENGTH_SHORT).show();
    }

    private String messageFormat(OrderModel model){

        String message ;

        message = "Hello " + model.getUserName() +"...!!!\n" +
                  "Your order is :\n" + model.getUserCartItem()+"\n" +
                "Your order is ready\n" +
                "Come and collect your order from "+ model.getLocation()+"\n" +
                "Thank You...";

        return message;
    }


    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_itemcard , parent , false);
        return new OrderHolder(view);
    }

    public static class OrderHolder extends RecyclerView.ViewHolder{

        TextView orderData , userName , totalCost , orderTime;
        Button messageButton;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            orderData = itemView.findViewById(R.id.orderDetails);
            userName = itemView.findViewById(R.id.userName);
            totalCost = itemView.findViewById(R.id.totalCost);
            orderTime = itemView.findViewById(R.id.orderedTime);
            messageButton = itemView.findViewById(R.id.messageBtn);
        }
    }
}
