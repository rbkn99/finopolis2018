package com.example.nesadimsergej.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Credentials;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;

public class start extends AppCompatActivity {
    Button loginBtn,registerBtn;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPref;
    start context;
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


        //
        if(sharedPref.contains("PATH") &&
                sharedPref.contains("NAME")){
            //Logined();
        }else{
            loginBtn.setVisibility(View.GONE);
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
        Intent intent = new Intent(context, Login.class);
        startActivity(intent);
        //Logined();
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                Web3j web3 = Web3jFactory.build(new HttpService(Config.web3Address));
                org.web3j.crypto.Credentials credentials = null;

                try {
                    credentials = WalletUtils.loadCredentials(" ", sharedPref.getString("PATH", "as") +
                            "/" + sharedPref.getString("NAME", "132"));
                }catch (Exception e){

                }

                Loyalty contract = Loyalty.load(
                        Config.contractAdress,
                        web3,
                        credentials,
                        Loyalty.GAS_PRICE,
                        Loyalty.GAS_LIMIT);

                String phoneNumber = "+9".toString();//System.out.println(contract.isValid());

                BigInteger phoneHash = new BigInteger(
                        String.valueOf(phoneNumber.hashCode())
                );

                System.out.println(credentials.getAddress());
                System.out.println(phoneHash);
                System.out.println(phoneHash.bitLength());




                try {

                    //contract.getLoggedInEvents();

                    //contract.logIn(phoneHash).send()
                    Tuple2<Boolean, BigInteger> a = contract.customers(credentials.getAddress()).sendAsync().get();
                    //a.getValue2();
                    //TransactionReceipt a = contract.logIn(phoneHash).sendAsync().get();
                    Toast.makeText(getApplicationContext(), a.getValue2().toString(),
                            Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    System.out.println("pizda");
                    e.printStackTrace();
                }
            }
        }).run();
*/

    }
    void Register(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    void Logined(){
        Intent intent;

        //f(sharedPref.getBoolean(Config.IS_TCP,false)){
        //intent = new Intent(this, tst1.class);
        //}else{
        //    intent = new Intent(this, Office_User.class);
        //}

        //startActivity(intent);
    }


}
