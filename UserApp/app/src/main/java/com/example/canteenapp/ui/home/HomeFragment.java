package com.example.canteenapp.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.canteenapp.BreakFastActivity;
import com.example.canteenapp.ChineseFoodActivity;
import com.example.canteenapp.DrinksActivity;
import com.example.canteenapp.IndianSnacksActivity;
import com.example.canteenapp.LunchActivity;
import com.example.canteenapp.R;

public class HomeFragment extends Fragment implements View.OnClickListener{

    View breakFastCardView, indianSnacksCardView, chineseCardView , lunchCardView , drinksCardView ;

   // private TextView name ;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
      //  final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
        //        textView.setText(s);
            }
        });

        breakFastCardView = root.findViewById(R.id.breakFastOption);
        indianSnacksCardView = root.findViewById(R.id.indianSnacksOption);
        chineseCardView = root.findViewById(R.id.chineseOption);
        lunchCardView = root.findViewById(R.id.lunchOption);
        drinksCardView = root.findViewById(R.id.drinksOption);

        breakFastCardView.setOnClickListener(this);
        indianSnacksCardView.setOnClickListener(this);
        chineseCardView.setOnClickListener(this);
        lunchCardView.setOnClickListener(this);
        drinksCardView.setOnClickListener(this);


        return root;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.breakFastOption :  startActivity(new Intent(getActivity(), BreakFastActivity.class)); break;
            case R.id.indianSnacksOption :  startActivity(new Intent(getActivity(), IndianSnacksActivity.class)); break;
            case R.id.chineseOption :  startActivity(new Intent(getActivity(), ChineseFoodActivity.class)); break;
            case R.id.lunchOption :  startActivity(new Intent(getActivity(), LunchActivity.class)); break;
            case R.id.drinksOption :  startActivity(new Intent(getActivity(), DrinksActivity.class)); break;

        }

    }
}
