package com.example.smartsocket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private Handler mhandler;
    private Runnable mrunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.black));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));
        }

        mrunnable = new Runnable() {
@Override
public void run() {

        startActivity(new Intent(getApplicationContext(), UserLogin.class));
        finish();



        }
        };
        mhandler = new Handler();
        mhandler.postDelayed(mrunnable, 3000);

        }

@Override
protected void onDestroy(){
        super.onDestroy();
        if(mhandler!=null && mrunnable!=null)
        mhandler.removeCallbacks(mrunnable);
        }
        }
