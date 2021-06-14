package com.example.canteenapp;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.Runnables;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {

        pairs = new Pair[2];
        pairs[0] = new Pair<View ,String>(signInButton , "signInTransition");
        pairs[1] = new Pair<View ,String>(signUpButton , "signUpTransition");
        Intent intent;
        switch (v.getId()){

            case R.id.signUpButton :

                 intent = new Intent(MainActivity.this , RegisterActivity.class);
                Toast.makeText(this, "sign up", Toast.LENGTH_SHORT).show();
                 break ;
            case R.id.signInButton :

                Toast.makeText(this, "sign in", Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this , LoginAccount.class);
                 break;
            default: return;
        }
        changeActivity(intent);  // animation while changing activity
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void changeActivity(Intent intent){

        ActivityOptions options =  ActivityOptions.makeSceneTransitionAnimation(this , pairs);
        startActivity(intent,options.toBundle());
    }

    Button signUpButton, signInButton;
    Animation leftAnimation , animationFromTop, animationFromLeft, fadeAnimation;
    TextView canteenText , tagLineText;
    Pair[] pairs;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signUpButton = findViewById(R.id.signUpButton);
        signInButton = findViewById(R.id.signInButton);
        canteenText = findViewById(R.id.canteenText);
        tagLineText = findViewById(R.id.tagLineText);


        leftAnimation = AnimationUtils.loadAnimation(this, R.anim.scaleanimation);
        animationFromTop = AnimationUtils.loadAnimation(this, R.anim.animation_from_top);
        animationFromLeft =AnimationUtils.loadAnimation(this, R.anim.animation_from_left);

        fadeAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_anim);



        signUpButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);



        canteenText.setAnimation(animationFromTop);
        tagLineText.setAnimation(animationFromLeft);

        signUpButton.setAnimation(fadeAnimation);
        signInButton.setAnimation(fadeAnimation);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}


