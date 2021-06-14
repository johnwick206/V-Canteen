package com.example.canteenapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canteenapp.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class ChooseOption extends AppCompatActivity {

   private NavigationView navigationView ;

    private AppBarConfiguration mAppBarConfiguration;
    private ImageView imageView ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option);


       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/


        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
       //  navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_yourOrders,R.id.logout,R.id.SearchFragmentID,R.id.cart_fragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
     //   NavigationUI.setupWithNavController(navigationView, navController);


        // TODO: 13-09-2020 bottom nav menu
        //Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        //User name on navigation drawer header
       // nameOnNavHeader();

        //reset cart item number
        resetCartItemNumber();


         //Function for logout Button inside navigationDrawer
        /*navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                *//*if(destination.getId() == R.id.logout) {
                    Toast.makeText(ChooseOption.this, "logout", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChooseOption.this , MainActivity.class));
                    finish();
                }*//*

                //set action bar visiblity
                if(destination.getId() == R.id.SearchFragmentID){
                    Objects.requireNonNull(getSupportActionBar()).hide();
                }else
                    Objects.requireNonNull(getSupportActionBar()).show();

            }
        });*/

        // TODO: 08-10-2020 move dialog box to corresponding ativity with interface obviously


    }

    /*private void nameOnNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView textView = headerView.findViewById(R.id.textView);
        imageView = headerView.findViewById(R.id.imageView); //no need of it
        textView.setText(UserData.name);
    }*/

    //on start application reset item count inside cart fragment
    private void resetCartItemNumber() {
        SharedPreferences resetCartItemCount  = getSharedPreferences("Pref", Context.MODE_PRIVATE);
        resetCartItemCount.edit().clear().apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choose_option, menu);
        return true;
    }

    /*@Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
*/

}
