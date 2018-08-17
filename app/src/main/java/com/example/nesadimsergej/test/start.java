package com.example.nesadimsergej.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class start extends AppCompatActivity {
    Button loginBtn,registerBtn;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);

        sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);

        //
        if(sharedPref.contains(Config.ADDRESS) &&
                sharedPref.contains(Config.PRIVATE_KEY) &&
                sharedPref.contains(Config.PUBLIC_KEY) &&
                sharedPref.contains(Config.IS_TCP)){
            Logined();
        }



        registerBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Register();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Login();
            }
        });

    }


    void Login(){

    }
    void Register(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void Logined(){
        Intent intent;

        if(sharedPref.getBoolean(Config.IS_TCP,false)){
            intent = new Intent(this, Office_TCP.class);
        }else{
            intent = new Intent(this, Office_User.class);
        }

        startActivity(intent);
    }


}
