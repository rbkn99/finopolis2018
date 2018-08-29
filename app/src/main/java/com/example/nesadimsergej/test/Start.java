package com.example.nesadimsergej.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Start extends AppCompatActivity {
    Button loginBtn,registerBtn;
    SharedPreferences sharedPref;
    Start context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);

        Utils.createNotificationChannel(this);
        if(sharedPref.contains("PATH") &&
                sharedPref.contains("NAME")){
        }else{
            //loginBtn.setVisibility(View.GONE);
        }

        registerBtn.setOnClickListener(v -> Register());

        loginBtn.setOnClickListener(v -> Login());
    }
    void Login(){
        Intent intent = new Intent(context, Login.class);
        startActivity(intent);
    }
    void Register(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}

