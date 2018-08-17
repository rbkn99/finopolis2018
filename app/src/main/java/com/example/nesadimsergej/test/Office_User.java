package com.example.nesadimsergej.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class Office_User extends AppCompatActivity {

    View balanceP,transactionP,eP;

    Web3j web3;
    TextView money;
    Button updateBalanceBtn,addEth;
    SharedPreferences sharedPref;
    EditText balanceCheater;
    Button exitOfficeBtn;
    BottomNavigationView bottomNavigationView;



    EditText targetAddress;
    EditText targetSum;
    Button sendEth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_user);

        LoadAll();

        updateBalanceBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UpdateBalance();
            }
        });
        addEth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AddEth();
            }
        });
        sendEth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SendEth();
            }
        });

        // тут все круто
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        HideAllPgs();
                        switch (item.getItemId()) {

                            case R.id.Balance:
                                balanceP.setVisibility(View.VISIBLE);
                                break;
                            case R.id.Transaction:
                                transactionP.setVisibility(View.VISIBLE);
                                break;
                            case R.id.YE:
                                eP.setVisibility(View.VISIBLE);
                                break;
                        }
                        return true;
                    }
                });

        // Обработчик для кнопки выхода
        // Стирает всю информацию о пользователе и переходит на стартовую сцену
        exitOfficeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor e = sharedPref.edit();
                e.clear();
                e.apply();
                Intent intent = new Intent(v.getContext(), start.class);
                startActivity(intent);
            }
        });

        // Выбираем вкладку с балансом
        Menu bottomNavigationMenu = bottomNavigationView.getMenu();
        bottomNavigationMenu.performIdentifierAction(R.id.Balance, 0);
        UpdateBalance();


    }

    void LoadAll(){
        targetAddress = findViewById(R.id.targetAddress);
        targetSum = findViewById(R.id.targetSum);
        sendEth = findViewById(R.id.sendEth);
        exitOfficeBtn = findViewById(R.id.exitOfficeBtn);
        balanceP = findViewById(R.id.balanceP);
        transactionP = findViewById(R.id.transactionP);
        eP = findViewById(R.id.eP);
        money = findViewById(R.id.ethInfo);
        updateBalanceBtn = findViewById(R.id.updateBalanceBtn);
        balanceCheater = findViewById(R.id.balanceCheater);
        addEth = findViewById(R.id.addEth);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        web3 = Web3jFactory.build(new HttpService(Config.web3Address));
        sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
    }
    //
    void SendEth(){
        String address = targetAddress.getText().toString();
        float v = Float.parseFloat(targetSum.getText().toString());
        try {
            TransactionReceipt transactionReceipt =
                    Transfer.sendFunds(web3, Credentials.create(sharedPref.getString(Config.PRIVATE_KEY, "IDI")), address,
                            BigDecimal.valueOf(v), Convert.Unit.ETHER).sendAsync().get();

        }catch (Exception e){

        }
        UpdateBalance();
    }

    void HideAllPgs(){
        balanceP.setVisibility(View.GONE);
        transactionP.setVisibility(View.GONE);
        eP.setVisibility(View.GONE);
    }

    int max(int a, int b){
        if(a>= b)
            return a;
        return b;
    }


    // Функция обновляющая баланс пользователя
    void UpdateBalance(){
        try {
            // Что-то делаем
            EthGetBalance ethGetBalance = web3
                    .ethGetBalance(sharedPref.getString(Config.ADDRESS, "IDI"), DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get();
            // Еще что-то делаем
            BigInteger wei = ethGetBalance.getBalance();
            //System.out.println(wei);
            String result = wei.toString();
            String a = result.substring(max( result.length() - 18,0));
            String b = result.substring(0,max( result.length() - 18,0));
            if( b.equals( "") || b.equals(" "))
                b = "0";
            result = b+"."+a;

            money.setText(result);

        }catch (Exception e){
            // blet
            e.printStackTrace();
            System.out.println("blet");
        }
    }

    // Пополнить счет
    void AddEth(){


        try {

            float v = Float.parseFloat(balanceCheater.getText().toString());

            TransactionReceipt transactionReceipt =
                    Transfer.sendFunds(web3, Credentials.create(Config.secretKey1), sharedPref.getString(Config.ADDRESS, "IDI"),
                            BigDecimal.valueOf(v), Convert.Unit.ETHER).sendAsync().get();
            /*}else{
                TransactionReceipt transactionReceipt =
                        Transfer.sendFunds(web3, Credentials.create(sharedPref.getString("PRIVATE_KEY", "IDI")), address1,
                                BigDecimal.valueOf(-v), Convert.Unit.ETHER).sendAsync().get();
            }*/
            UpdateBalance();

        }catch (Exception e){
            System.out.println(e);
        }
    }

}
