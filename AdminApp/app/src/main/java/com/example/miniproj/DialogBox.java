package com.example.miniproj;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.zip.Inflater;

public class DialogBox extends DialogFragment {
    DialogListener dialogListener;
    public interface DialogListener{
       void clickedItem(String currentText);

    }

    Activity activity;
    String[] canteenOptions;
    String[] category;
    String[] collectionArray ;
    String dialogTitle;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater =  activity.getLayoutInflater();
       // View view = inflater.inflate(R.layout.dialogbox ,null);

        //array elements
        canteenOptions = activity.getResources().getStringArray(R.array.VcOrVl);
        category = activity.getResources().getStringArray(R.array.Category);


        //setting dialog box
        //builder.setView(view);
        builder.setTitle(dialogTitle);
        builder.setSingleChoiceItems(collectionArray, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogListener.clickedItem(collectionArray[i]);
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        return builder.create();
    }


    //accept layout activity //constructor
    public DialogBox(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogListener) context;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassCastException(e.getMessage());
        }

    }


    void acceptArrayAndTitle(String[] collectionArray , String dialogTitle){
        this.collectionArray = collectionArray ;
        this.dialogTitle = dialogTitle;
    }

//create 3 functions to display two different options 1.canteen/lounge
    // 2. 4 options to selelct between junk food /break fast/ lunch/ fast food
}
