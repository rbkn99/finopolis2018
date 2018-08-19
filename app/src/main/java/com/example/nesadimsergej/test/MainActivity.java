package com.example.nesadimsergej.test;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.NetListening;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    /*
    public static final String ADDRESS = "com.example.myfirstapp.MESSAGE";
    public static final String PRIVATE_KEY = "com.example.myfirstapp.MESSAGE";
    public static final String PUBLIC_KEY = "com.example.myfirstapp.MESSAGE";
    */
    ConstraintLayout TCPLayout;
    ConstraintLayout UserLayout;
    ConstraintLayout black;
    Spinner dropdown;
    Button userRegisterButton;
    Button tcpRegisterButton;

    String[] items = new String[]{"Клиент-покупатель","ТСП"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadAll();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        ResetScene();
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                //Object item = parent.getItemAtPosition(pos);
                switch (pos){
                    case 0:
                            ResetScene();
                            UserLayout.setVisibility(View.VISIBLE);
                            TCPLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                            ResetScene();
                            UserLayout.setVisibility(View.GONE);
                            TCPLayout.setVisibility(View.VISIBLE);
                        break;
                    default:break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });

        userRegisterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RegisterUser();
            }
        });
        tcpRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterTCP();
            }
        });

    }
    void RegisterUser(){
        userRegisterButton.setEnabled(false);
        new Handler().post(new Runnable() {
            public void run() {

                Web3j web3 = Web3jFactory.build(new HttpService(Config.web3Address));

                try {

                    File f = new File(getApplicationContext().getFilesDir(),"");

                    String str = WalletUtils.generateLightNewWalletFile(" ", f);
                    System.out.println(str);

                    // Сохраняем всю интересующую нас информацию
                    SharedPreferences sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();

                    editor.putString("NAME", str);
                    editor.putString("PATH",f.getAbsolutePath());

                    editor.putBoolean(Config.IS_TCP,false);
                    editor.apply();

                    // Переходим на следующую сцену
                    Intent intent = new Intent(getBaseContext(), Office_User.class);
                    startActivity(intent);
                    //userRegisterButton.setEnabled(true);
                }catch(Exception e){
                    userRegisterButton.setEnabled(true);
                    ((TextView)(findViewById(R.id.textView2))).setText(e.toString());
                }
            }
        });
    }

    void RegisterTCP(){
        Web3j web3 = Web3jFactory.build(new HttpService(Config.web3Address));
        try {
            NetListening connected = web3.netListening().sendAsync().get();
            if(connected.isListening()){
                // Мы подключены
                // create new private/public key pair
                // Создаем нового пользователя
                ECKeyPair keyPair = Keys.createEcKeyPair();
                BigInteger publicKey = keyPair.getPublicKey();
                String publicKeyHex = Numeric.toHexStringWithPrefix(publicKey);
                BigInteger privateKey = keyPair.getPrivateKey();
                String privateKeyHex = Numeric.toHexStringWithPrefix(privateKey);
                Credentials credentials = Credentials.create(new ECKeyPair(privateKey, publicKey));
                String address = credentials.getAddress();

                TransactionReceipt transactionReceipt =
                        Transfer.sendFunds(web3,Credentials.create(Config.secretKey1),address,
                                BigDecimal.valueOf(Config.TCP_START_BALANCE), Convert.Unit.ETHER).sendAsync().get();



                SharedPreferences sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Config.ADDRESS, address);
                editor.putString(Config.PRIVATE_KEY, privateKeyHex);
                editor.putString(Config.PUBLIC_KEY, publicKeyHex);
                editor.putBoolean(Config.IS_TCP,true);
                editor.apply();

                // Переходим на следующую сцену
                Intent intent = new Intent(this, infoAfterRegistration.class);
                startActivity(intent);

            }

        }catch (Exception e){

        }

    }


    void LoadAll(){
        dropdown = findViewById(R.id.userSelector);
        TCPLayout = findViewById(R.id.TCP);
        UserLayout = findViewById(R.id.User);
        userRegisterButton = findViewById(R.id.userRegisterButton);
        tcpRegisterButton = findViewById(R.id.tcpRegisterButton);
        black = findViewById(R.id.black);
    }

    void ResetScene(){
        TCPLayout.setVisibility(View.GONE);
        UserLayout.setVisibility(View.GONE);
    }
}


