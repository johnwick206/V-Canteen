package com.example.canteenapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

class VerificationDialogBox {

    private AlertDialog dialogBox ;
    private Activity currentActivity ;
    private  String displayText;

    public VerificationDialogBox(Activity currentActivity ,String displayText) {

        this.displayText = displayText ;
        this.currentActivity = currentActivity;
        popDialogBox();
    }

   void popDialogBox(){

        AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
       LayoutInflater inflater = currentActivity.getLayoutInflater();
       View view =inflater.inflate(R.layout.verifydialogbox , null);
       builder.setView(view);

       TextView textView = view.findViewById(R.id.dialogMsg) ;
       textView.setText(displayText);
       dialogBox  = builder.create();
       dialogBox.show();
   }

   void closeDialogBox(){
        dialogBox.dismiss();
   }
}
