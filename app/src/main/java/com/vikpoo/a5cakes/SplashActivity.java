package com.vikpoo.a5cakes;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // to change color of status bar
            getWindow().setStatusBarColor(getResources().getColor(R.color.statusBarColor));

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            // user exists
            String id = user.getUid();
            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra("USER_ID",id);
                    startActivity(intent);
                    finish();
                }
            },2000);
        }
        else
        {
            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashActivity.this, VerifyUserActivity.class);
                    startActivity(intent);
                    finish();
                }
            },2000);
        }
    }
}
