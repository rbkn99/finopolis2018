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
import android.widget.EditText;
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

    ConstraintLayout UserLayout,TCPLayout;
    Spinner dropdown;
    Button userRegisterButton,tcpRegisterButton;
    EditText phoneUSER,phoneTCP,nameTCP;
    Web3j web3;
    String[] items = new String[]{"Клиент-покупатель","ТСП"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadAll();

        // Настраиваем список для выбора роли
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setSelection(0);

        // Обработчик выбора роли
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                HideAllPgs();
                switch (pos){
                    case 1:
                            TCPLayout.setVisibility(View.VISIBLE);
                        break;
                    case 0:
                            UserLayout.setVisibility(View.VISIBLE);
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
        if(!checkUserFields())
            return;

        userRegisterButton.setEnabled(false);
        new Handler().post(new Runnable() {
            public void run() {
                try {
                    //Заводин новый адресс, публичный ключ и приватный ключ
                    File f = new File(getApplicationContext().getFilesDir(),"");
                    String str = WalletUtils.generateLightNewWalletFile(" ", f);
                    Credentials credentials = WalletUtils.loadCredentials(" ",f.getAbsolutePath() + "/" +str);

                    // Сохраняем всю интересующую нас информацию
                    SharedPreferences sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("NAME", str);
                    editor.putString("PATH",f.getAbsolutePath());
                    editor.putBoolean(Config.IS_TCP,false);
                    editor.apply();

                    /**
                     * Загружаем смарт контракт ( все действия будут выполнены не от имени регистрируемого пользователя,
                     * а от имени владельца контракта( того, кто его залил)
                     **/
                    Loyalty contract = Loyalty.load(
                            Config.contractAdress,/*Адресс контракта (указан в конфиге) */
                            web3,/* */
                            Credentials.create(Config.prk, Config.puk),/**/
                            Loyalty.GAS_PRICE,
                            Loyalty.GAS_LIMIT);

                    String phoneNumber = phoneUSER.getText().toString();
                    // Если номер это просто то 1, то текущий пользователь не регистрируется( кул хак)
                    if (!(phoneNumber.length() == 1 && phoneNumber.charAt(0) == '1')) {

                        // Хэш номера телефона, который мы будем отправлять в блокчейн
                        BigInteger phoneHash = new BigInteger(
                                String.valueOf(phoneNumber.hashCode())
                        );

                        // Регистрируем пользователя
                        contract.addCustomer(
                                credentials.getAddress(),
                                phoneHash
                        ).sendAsync().get();
                    }else{
                        System.out.println("COOL HACK");
                    }
                    // Переходим на сцену с личным кабинетом пользователя
                    Intent intent = new Intent(getBaseContext(), Office_User.class);
                    startActivity(intent);
                }catch(Exception e){
                    userRegisterButton.setEnabled(true);
                    ((TextView)(findViewById(R.id.textView2))).setText(e.toString());
                    e.printStackTrace();
                }
            }
        });
    }

    boolean checkUserFields(){
        String phoneNumber = phoneUSER.getText().toString();
        return phoneNumber.length() > 0;
    }
    boolean checkTCPFields(){
        return true;
    }

    void RegisterTCP(){
        if(!checkTCPFields())
            return;

        userRegisterButton.setEnabled(false);
        new Handler().post(new Runnable() {
            public void run() {
                try {
                    //Заводин новый адресс, публичный ключ и приватный ключ
                    File f = new File(getApplicationContext().getFilesDir(),"");
                    String str = WalletUtils.generateLightNewWalletFile(" ", f);
                    Credentials credentials = WalletUtils.loadCredentials(" ",f.getAbsolutePath() + "/" +str);

                    // Сохраняем всю интересующую нас информацию
                    SharedPreferences sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("NAME", str);
                    editor.putString("PATH",f.getAbsolutePath());
                    editor.putBoolean(Config.IS_TCP,false);
                    editor.apply();

                    /**
                     * Загружаем смарт контракт ( все действия будут выполнены не от имени регистрируемого пользователя,
                     * а от имени владельца контракта( того, кто его залил)
                     **/
                    Loyalty contract = Loyalty.load(
                            Config.contractAdress,/*Адресс контракта (указан в конфиге) */
                            web3,/* */
                            Credentials.create(Config.prk, Config.puk),/**/
                            Loyalty.GAS_PRICE,
                            Loyalty.GAS_LIMIT);

                    String phoneNumber = phoneTCP.getText().toString();
                    String companyName = nameTCP.getText().toString();

                    // Если номер это просто то 1, то текущий пользователь не регистрируется( кул хак)
                    if (!(phoneNumber.length() == 1 && phoneNumber.charAt(0) == '1')) {

                        // Хэш номера телефона, который мы будем отправлять в блокчейн
                        BigInteger phoneHash = new BigInteger(
                                String.valueOf(phoneNumber.hashCode())
                        );

                        System.out.println(phoneNumber);
                        System.out.println(phoneHash);
                        System.out.println(phoneHash.bitLength());

                        // Регистрируем пользователя
                        contract.addCompany(
                                credentials.getAddress(),
                                companyName,
                                phoneHash
                        ).sendAsync().get();

                    }else{
                        System.out.println("COOL HACK");
                    }
                    // Переходим на сцену с личным кабинетом компании
                    Intent intent = new Intent(getBaseContext(), Office_TCP.class);
                    startActivity(intent);

                }catch(Exception e){
                    userRegisterButton.setEnabled(true);
                    ((TextView)(findViewById(R.id.textView2))).setText(e.toString());
                    e.printStackTrace();
                }
            }
        });

    }

    // Функция для загрузки всех нужных эелементов сцены
    void LoadAll(){
        web3 = Web3jFactory.build(new HttpService(Config.web3Address));
        // Кнопки регистрации
        userRegisterButton = findViewById(R.id.userRegisterButton);
        tcpRegisterButton = findViewById(R.id.tcpRegisterButton);

        // Поля для регистрации пользователя
        phoneUSER = findViewById(R.id.phoneUSER);

        // Поля регистрации компании
        phoneTCP = findViewById(R.id.phoneTCP);
        nameTCP = findViewById(R.id.nameTCP);

        // Список для выбора кого регистрировать
        dropdown = findViewById(R.id.userSelector);

        TCPLayout = findViewById(R.id.TCP);
        UserLayout = findViewById(R.id.User);

    }

    void HideAllPgs(){
        TCPLayout.setVisibility(View.GONE);
        UserLayout.setVisibility(View.GONE);
    }
}


