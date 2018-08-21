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

    //private DrawerLayout mDrawerLayout;

    //ArrayList<View> pages= new ArrayList<>();

    //Map<Integer, Integer> map = new HashMap<>();

    View balanceP,transactionP,eP,coalitionsP,create_coalitionP,queriesP,token_settingsP;

    Create_coalition create_coalition;
    Coalitions coalitions;
    Queries queries;
    Token_settings token_settings;

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
                //System.out.println("called1488");
                Contract();
            }
        });
        deployContractBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadContract();
            }
        });

        // Выбираем вкладку с балансом
        //Menu bottomNavigationMenu = bottomNavigationView.getMenu();
        //bottomNavigationMenu.performIdentifierAction(R.id.Balance, 0);
        UpdateBalance();

        //mDrawerLayout.getHea
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
        coalitionsP = findViewById(R.id.coalitionsP);
        create_coalitionP = findViewById(R.id.create_coalitionP);
        queriesP = findViewById(R.id.queriesP);
        token_settingsP = findViewById(R.id.token_settingsP);
        balanceP = findViewById(R.id.balanceP);
        transactionP = findViewById(R.id.transactionP);
        eP = findViewById(R.id.eP);

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


    }

    /*
    void Contract(){

        try {

            Loyalty contract = Loyalty
                    .load(Config.contractAdress,web3,Credentials.create(Config.prk
                            ,Config.puk),Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

            //contract.
            System.out.println(contract.getContractAddress());
            try {
                BigInteger i = new BigInteger("12345");
                System.out.println(i.bitCount());

                TransactionReceipt a = contract.addCompany(credentials.getAddress(),"PidorasCo",i).sendAsync().get();

                Toast.makeText(getApplicationContext(),  a.getBlockNumber().toString(),
                        Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Pizdec!",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error1");
        }

    }
    String contractAddress = "";
    void UploadContract(){
        new Thread(new Runnable() {
            public void run() {
                deployContractBtn.setEnabled(false);
                try {

                    Loyalty contract = Loyalty
                            .deploy(web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT).sendAsync().get();

                    contractAddress = contract.getContractAddress();
                    deployContractBtn.setEnabled(true);
                    System.out.println(contractAddress);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error!",
                            Toast.LENGTH_SHORT).show();
                    deployContractBtn.setEnabled(true);
                }
            }
        }).run();
    }

    //
    void SendEth(){
        new Thread(new Runnable() {
            public void run() {
                String address = targetAddress.getText().toString();
                float v = Float.parseFloat(targetSum.getText().toString());

                try {
                    TransactionReceipt transactionReceipt =
                            Transfer.sendFunds(web3, credentials, address,
                                    BigDecimal.valueOf(v), Convert.Unit.ETHER).sendAsync().get(20, TimeUnit.SECONDS);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error!",
                            Toast.LENGTH_SHORT).show();
                }
                UpdateBalance();
            }
        }).run();
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




    //boolean opened = false;
   /* void SetUpDrawerLayout(){
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.Balance);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        HideAllPgs();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        UnHidePage(map.get(item.getItemId()));
                        return true;
                    }
                });

        navigationView.getMenu().performIdentifierAction(R.id.Balance,0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                        opened = true;
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                        opened = false;
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
        //mDrawerLayout

    }*/


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