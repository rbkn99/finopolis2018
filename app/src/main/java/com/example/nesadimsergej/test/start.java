package com.example.nesadimsergej.test;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class start extends AppCompatActivity {
    Button loginBtn,registerBtn;
    //Button goToRybkin;
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
        createNotificationChannel();

        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        //goToRybkin = findViewById(R.id.goToRbkn);
        sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);


        //
        if(sharedPref.contains("PATH") &&
                sharedPref.contains("NAME")){
            //Logined();
        }else{
            loginBtn.setVisibility(View.GONE);
        }

        /*goToRybkin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, OrdersViewActivity.class);
                startActivity(intent);
            }
        });*/

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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "blp";
            String description = "blockchain loyalty program";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(Utils.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
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
        Intent intent = new Intent(this, Register.class);
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
