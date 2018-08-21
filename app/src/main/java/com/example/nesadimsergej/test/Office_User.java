package com.example.nesadimsergej.test;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.abi.datatypes.Int;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Office_User extends Office {

    //private DrawerLayout mDrawerLayout;

    //ArrayList<View> pages= new ArrayList<>();
    //Map<Integer, Integer> map = new HashMap<>();

    View balanceP,transactionP,eP,exchange_bonusesP,pay_bonusesP,user_bonusesP;


    //public Credentials credentials;
    //public Web3j web3;

    /*
    TextView money;
    Button updateBalanceBtn,addEth,infoBtn;
    SharedPreferences sharedPref;
    EditText balanceCheater;
    Button exitOfficeBtn;
    BottomNavigationView bottomNavigationView;
    */
    //TextView privateKeyInfo,publicKeyInfo,addressInfo;
    /*
    EditText targetAddress;
    EditText targetSum;
    Button sendEth;
    Button contractTest;
    Button deployContractBtn;
    */

    //private ClipData myClip;
    //private ClipboardManager myClipboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_user);

        map.put(R.id.Balance,R.id.balanceP);
        map.put(R.id.Transaction,R.id.transactionP);
        map.put(R.id.YE,R.id.eP);
        map.put(R.id.user_bonuses,R.id.user_bonusesP);
        map.put(R.id.exchange_bonuses,R.id.exchange_bonusesP);
        map.put(R.id.pay_bonuses,R.id.pay_bonusesP);

        LoadAll();
        HideAllPgs();
        SetUpDrawerLayout();

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


        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoPopUP();
            }
        });

        // Обработчик для кнопки выхода
        // Стирает всю информацию о пользователе и переходит на стартовую сцену
        exitOfficeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor e = sharedPref.edit();
                //e.clear();
                //e.remove("PATH");
                //e.remove("NAME");
                e.apply();
                Intent intent = new Intent(v.getContext(), start.class);
                startActivity(intent);
            }
        });
        contractTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contract();
            }
        });
        deployContractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadContract();
            }
        });

        UpdateBalance();

    }


    void InfoPopUP(){
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.info_pop_up);


        TextView privateKeyInfo = dialog.findViewById(R.id.PrKUO);
        TextView publicKeyInfo = dialog.findViewById(R.id.PuKUO);
        TextView addressInfo = dialog.findViewById(R.id.AdUO);
        TextView pathTV = dialog.findViewById(R.id.pathTV);
        TextView nameTV = dialog.findViewById(R.id.nameTV);


        addressInfo.setText(
                credentials.getAddress());

        ECKeyPair p = credentials.getEcKeyPair();

        publicKeyInfo.setText(
                p.getPublicKey().toString(16));
        privateKeyInfo.setText(
                p.getPrivateKey().toString(16));

        pathTV.setText(sharedPref.getString("PATH", "NA"));
        nameTV.setText(sharedPref.getString("NAME", "NA"));

        View.OnClickListener o = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ((TextView)v).getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
            }
        };

        addressInfo.setOnClickListener(o);
        publicKeyInfo.setOnClickListener(o);
        privateKeyInfo.setOnClickListener(o);
        pathTV.setOnClickListener(o);
        nameTV.setOnClickListener(o);
        dialog.show();
    }

    @Override
    protected void LoadAll(){
        super.LoadAll();
        //exchange_bonusesP,pay_bonusesP,user_bonusesP
        exchange_bonusesP = findViewById(R.id.exchange_bonusesP);
        pay_bonusesP = findViewById(R.id.pay_bonusesP);
        user_bonusesP = findViewById(R.id.user_bonusesP);
        balanceP = findViewById(R.id.balanceP);
        transactionP = findViewById(R.id.transactionP);
        eP = findViewById(R.id.eP);

        pages.add(exchange_bonusesP);
        pages.add(pay_bonusesP);
        pages.add(user_bonusesP);
        pages.add(balanceP);
        pages.add(transactionP);
        pages.add(eP);



    }



/*
    int max(int a, int b){
        if(a>= b)
            return a;
        return b;
    }
*/
    // Функция обновляющая баланс пользователя
    /*void UpdateBalance(){
        new Thread(new Runnable() {
            public void run() {
                updateBalanceBtn.setEnabled(false);
                try {
                    EthGetBalance ethGetBalance = web3
                            .ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                            .sendAsync().get(20, TimeUnit.SECONDS);
                    // Еще что-то делаем
                    BigInteger wei = ethGetBalance.getBalance();
                    //System.out.println(wei);
                    String result = wei.toString();

                    int l = result.length();
                    for(int i = l; i<18;i++)
                        result = "0"+result;

                    String a = result.substring(max( result.length() - 18,0));
                    String b = result.substring(0,max( result.length() - 18,0));
                    if( b.equals( "") || b.equals(" "))
                        b = "0";
                    result = b+"."+a;

                    money.setText(result);
                    updateBalanceBtn.setEnabled(true);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error!",
                            Toast.LENGTH_SHORT).show();
                    updateBalanceBtn.setEnabled(true);
                }
            }
        }).run();
    }
    */

    /*void AddEth(){

        new Thread(new Runnable() {
            public void run() {
                try {

                    float v = Float.parseFloat(balanceCheater.getText().toString());

                    TransactionReceipt transactionReceipt =
                            Transfer.sendFunds(web3, Credentials.create(Config.secretKey1), credentials.getAddress(),
                                    BigDecimal.valueOf(v), Convert.Unit.ETHER).sendAsync().get(20, TimeUnit.SECONDS);
                    UpdateBalance();

                } catch (Exception e){
                    System.out.println(e);
                }
            }
        }).run();
    }*/

    /*void HideAllPgs(){
        for (View v:pages
             ) {
            v.setVisibility(View.GONE);
        }
        //balanceP.setVisibility(View.GONE);
        //transactionP.setVisibility(View.GONE);
        //eP.setVisibility(View.GONE);
    }
    void UnHidePage(int id){
        for (View v:pages
                ) {
            if(v.getId() == id)
                v.setVisibility(View.VISIBLE);
        }
    }*/


    //boolean opened = false;



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(!opened) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }else{
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}