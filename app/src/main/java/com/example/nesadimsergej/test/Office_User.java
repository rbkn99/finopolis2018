package com.example.nesadimsergej.test;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Debug;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
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
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import static org.web3j.tx.ManagedTransaction.GAS_PRICE;
import static org.web3j.tx.Transfer.GAS_LIMIT;

public class Office_User extends AppCompatActivity {

    View balanceP,transactionP,eP;
    Credentials credentials;

    Web3j web3;
    TextView money;
    Button updateBalanceBtn,addEth,infoBtn;
    SharedPreferences sharedPref;
    EditText balanceCheater;
    Button exitOfficeBtn;
    BottomNavigationView bottomNavigationView;

    //TextView privateKeyInfo,publicKeyInfo,addressInfo;
    EditText targetAddress;
    EditText targetSum;
    Button sendEth;
    Button contractTest;
    Button deployContractBtn;

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


        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoPopUP();
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
        Menu bottomNavigationMenu = bottomNavigationView.getMenu();
        bottomNavigationMenu.performIdentifierAction(R.id.Balance, 0);
        UpdateBalance();


    }
    private ClipData myClip;
    private ClipboardManager myClipboard;

    void InfoPopUP(){
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.info_pop_up);


        TextView privateKeyInfo = dialog.findViewById(R.id.PrKUO);
        TextView publicKeyInfo = dialog.findViewById(R.id.PuKUO);
        TextView addressInfo = dialog.findViewById(R.id.AdUO);

        addressInfo.setText(
                credentials.getAddress());

        ECKeyPair p = credentials.getEcKeyPair();

        publicKeyInfo.setText(
                p.getPublicKey().toString(16));
        privateKeyInfo.setText(
                p.getPrivateKey().toString(16));

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

        dialog.show();
    }

    void LoadAll(){
        deployContractBtn = findViewById(R.id.deployContractBtn);
        //privateKeyInfo = findViewById(R.id.PrKUO);
        //publicKeyInfo = findViewById(R.id.PuKUO);
        //addressInfo = findViewById(R.id.AdUO);
        contractTest = findViewById(R.id.contractBtn);
        infoBtn = findViewById(R.id.infoBtn);
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

        try {
            credentials = WalletUtils.loadCredentials(" ",
                    sharedPref.getString("PATH", "NA") + "/" + sharedPref.getString("NAME", "NA"));
        }catch (Exception e){

        }
    }


    void Contract(){
        try {

            Loyalty_sol_Loyalty contract = Loyalty_sol_Loyalty
                    .load(contractAddress.substring(2),web3,credentials,Loyalty_sol_Loyalty.GAS_PRICE,Loyalty_sol_Loyalty.GAS_LIMIT);

            try {
                contract.addCustomer(credentials.getAddress().substring(2),new BigInteger("25423")).sendAsync().get();

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
        new Handler().post(new Runnable() {
            public void run() {
                deployContractBtn.setEnabled(false);
                try {

                    Loyalty_sol_Loyalty contract = Loyalty_sol_Loyalty
                            .deploy(web3,credentials,Loyalty_sol_Loyalty.GAS_PRICE,Loyalty_sol_Loyalty.GAS_LIMIT).sendAsync().get();
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
        });
    }

    //
    void SendEth(){
        new Handler().post(new Runnable() {
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
        });
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
        new Handler().post(new Runnable() {
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
            });
    }

    // Пополнить счет
    void AddEth(){

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
        }).start();
    }


}
