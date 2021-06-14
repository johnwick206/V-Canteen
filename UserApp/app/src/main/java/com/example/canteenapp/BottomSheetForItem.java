package com.example.canteenapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetForItem extends BottomSheetDialogFragment {

    View view ;
    private TextView nameTextView;
    String itemName ;

    public BottomSheetForItem(String itemName) {
                this.itemName = itemName;
    }

    public BottomSheetForItem() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.bottom_sheet_f_details , container, false);
        nameTextView = view.findViewById(R.id.c1);
        nameTextView.setText(itemName);
        return view;
    }

}
