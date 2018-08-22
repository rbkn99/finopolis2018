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

    View exchange_bonusesP,pay_bonusesP,user_bonusesP;

    User_bonuses user_bonuses;
    Exchange_bonuses exchange_bonuses;
    Pay_bonuses pay_bonuses;



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
        //LoadAllCompanies();
    }



    @Override
    protected void LoadAll(){
        super.LoadAll();
        exchange_bonusesP = findViewById(R.id.exchange_bonusesP);
        pay_bonusesP = findViewById(R.id.pay_bonusesP);
        user_bonusesP = findViewById(R.id.user_bonusesP);

        user_bonuses = new User_bonuses(user_bonusesP);
        exchange_bonuses = new Exchange_bonuses(exchange_bonusesP);
        pay_bonuses = new Pay_bonuses(pay_bonusesP);


        pages.add(exchange_bonusesP);
        pages.add(pay_bonusesP);
        pages.add(user_bonusesP);
        pages.add(balanceP);
        pages.add(transactionP);
        pages.add(eP);

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