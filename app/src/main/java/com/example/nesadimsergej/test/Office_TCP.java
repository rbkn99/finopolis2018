package com.example.nesadimsergej.test;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Office_TCP extends Office {


    View coalitionsP,create_coalitionP,queriesP,token_settingsP;

    Create_coalition create_coalition;
    Coalitions coalitions;
    Queries queries;
    Token_settings token_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_tcp);

        map.put(R.id.Balance,R.id.balanceP);
        map.put(R.id.Transaction,R.id.transactionP);
        map.put(R.id.YE,R.id.eP);
        map.put(R.id.coalitions,R.id.coalitionsP);
        map.put(R.id.create_coalition,R.id.create_coalitionP);
        map.put(R.id.queries,R.id.queriesP);
        map.put(R.id.token_settings,R.id.token_settingsP);

        LoadAll();
        HideAllPgs();
        SetUpDrawerLayout();

        //Loyalty c;

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
        // тут все круто

        // Обработчик для кнопки выхода
        // Стирает всю информацию о пользователе и переходит на стартовую сцену
        exitOfficeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exit();
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


    @Override
    protected void LoadAll(){
        super.LoadAll();
        coalitionsP = findViewById(R.id.coalitionsP);
        create_coalitionP = findViewById(R.id.create_coalitionP);
        queriesP = findViewById(R.id.queriesP);
        token_settingsP = findViewById(R.id.token_settingsP);

        pages.add(coalitionsP);
        pages.add(create_coalitionP);
        pages.add(queriesP);
        pages.add(token_settingsP);
        pages.add(balanceP);
        pages.add(transactionP);
        pages.add(eP);

        create_coalition = new Create_coalition(create_coalitionP);
        coalitions = new Coalitions(coalitionsP);
        queries = new Queries(queriesP);
        token_settings = new Token_settings(token_settingsP);

        idToScene.put(R.id.coalitionsP,coalitions);
        idToScene.put(R.id.create_coalitionP,create_coalition);
        idToScene.put(R.id.queriesP,queries);
        idToScene.put(R.id.token_settingsP,token_settings);
        AddEth1();

    }


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