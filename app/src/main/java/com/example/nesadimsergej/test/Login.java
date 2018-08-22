package com.example.nesadimsergej.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Credentials;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;

import java.math.BigInteger;

public class Login extends AppCompatActivity {

    Button loginBtn,backBtn;
    EditText numberLogin;
    Web3j web3;
    Login context;
    SharedPreferences sharedPref;
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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TryToLogin();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });

    }


    void TryToLogin(){
            String phoneNumber = numberLogin.getText().toString();
            String fileName = sharedPref.getString("NAME","PZD");
            String pathToFile = sharedPref.getString("PATH","EC");

            try {
                org.web3j.crypto.Credentials credentials = WalletUtils.loadCredentials(" ",
                        pathToFile + "/" + fileName);

                Loyalty contract = Loyalty.load(
                        Config.contractAdress,
                        web3,
                        credentials,
                        Loyalty.GAS_PRICE,
                        Loyalty.GAS_LIMIT);

                BigInteger phoneHash = new BigInteger(
                        String.valueOf(phoneNumber.hashCode())
                );
                Tuple2<Boolean, BigInteger> a = contract.customers(credentials.getAddress()).sendAsync().get();

                BigInteger targetHash = a.getValue2();

                if(phoneHash.equals(targetHash)){
                    //E BOI
                    // Это обычный пользователь
                    Intent intent = new Intent(context, Office_User.class);
                    startActivity(intent);
                }else{

                    // 1 -
                    // 2 -
                    // 3 -
                    // 4 - phoneHash
                    Tuple5<Boolean, String, String, BigInteger, BigInteger> b = contract.companies(credentials.getAddress()).sendAsync().get();
                    targetHash = b.getValue5();
                    if(phoneHash.equals(targetHash)) {
                        //E BOI
                        // Это компания
                        Intent intent = new Intent(context, Office_TCP.class);
                        startActivity(intent);
                    }else{
                        System.out.println("pizda1488");
                    }
                }

            }catch (Exception e){
                //System.out.println("pizda322");
                SharedPreferences.Editor a = sharedPref.edit();
                a.clear();
                a.apply();

                e.printStackTrace();
                Back();
            }

    }

    void Back(){
        Intent intent = new Intent(context, start.class);
        startActivity(intent);
    }
}
