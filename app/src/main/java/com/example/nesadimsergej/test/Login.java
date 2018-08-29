package com.example.nesadimsergej.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple8;

import java.math.BigInteger;

public class Login extends AppCompatActivity {

    Button loginBtn, backBtn;
    EditText numberLogin;
    Web3j web3;
    Login context;
    SharedPreferences sharedPref;

    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        web3 = Web3jFactory.build(new HttpService(Config.web3Address));

        loginBtn = findViewById(R.id.loginBtn);
        numberLogin = findViewById(R.id.numberLogin);
        backBtn = findViewById(R.id.backBtn);

        ctx = this;

        loginBtn.setOnClickListener(v -> {
            try {
                TryToLogin();
            }catch (Exception e){
                context.runOnUiThread(()-> Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show());
            }
        });
        backBtn.setOnClickListener(v -> Back());

    }


    void TryToLogin() {
        String phoneNumber = numberLogin.getText().toString();

        String pathToFile = sharedPref.getString("PATH", "EC");

        new Thread(() -> {
            try {
                BigInteger phoneHash = new BigInteger(
                        String.valueOf(phoneNumber.hashCode())
                );

                String fileName = phoneHash.toString() + ".json";
                Credentials credentials;
                context.runOnUiThread(() -> Toast.makeText(context, "Подождите, выполняется вход...", Toast.LENGTH_LONG).show());
                try {
                    credentials = WalletUtils.loadCredentials(" ",
                            pathToFile + "/" + fileName);
                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(context, "Неверный номер телефона", Toast.LENGTH_SHORT).show();
                    });
                    e.printStackTrace();
                    return;
                }

                Loyalty contract = Loyalty.load(
                        Config.contractAdress,
                        web3,
                        credentials,
                        Loyalty.GAS_PRICE,
                        Loyalty.GAS_LIMIT);

                Tuple2<Boolean, BigInteger> a = contract.customers(credentials.getAddress()).sendAsync().get();

                BigInteger targetHash = a.getValue2();

                SharedPreferences sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("NAME", fileName);


                if (phoneHash.equals(targetHash)) {
                    editor.putBoolean(Config.IS_TCP,false);
                    editor.apply();
                    // Это обычный пользователь
                    Intent intent = new Intent(context, Office_User.class);
                    startActivity(intent);
                } else {
                    Company cmp = Utils.getCompany(web3,credentials,credentials.getAddress());// new Company(contract.companies(credentials.getAddress()).sendAsync().get());
                    System.out.println(cmp);
                    targetHash = cmp.phoneNumber;

                    if (phoneHash.equals(targetHash)) {
                        editor.putBoolean(Config.IS_TCP,true);
                        editor.apply();
                        // Это компания
                        Intent intent = new Intent(context, Office_TCP.class);
                        startActivity(intent);
                    } else {
                        context.runOnUiThread(()-> Toast.makeText(context, "Такой номер не зарегистрирован в системе", Toast.LENGTH_SHORT).show());
                        Utils.notificationManager.cancel(3);
                    }
                }
            } catch (Exception e) {
                //SharedPreferences.Editor a = sharedPref.edit();
                //a.clear();
                //a.apply();

                //e.printStackTrace();
                context.runOnUiThread(()-> Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show());
                Back();
            }
        }).start();
    }

    void Back() {
        Intent intent = new Intent(context, Start.class);
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
