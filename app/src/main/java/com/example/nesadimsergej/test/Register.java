package com.example.nesadimsergej.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.math.BigInteger;
import java.util.concurrent.Future;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    ConstraintLayout UserLayout,TCPLayout;
    Spinner dropdown;
    Button userRegisterButton,tcpRegisterButton;
    EditText phoneUSER,phoneTCP,nameTCP;
    Web3j web3;
    String[] items = new String[]{"Клиент-покупатель","ТСП"};
    Button back_btn;
    Context context;
    Register _this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#e37222")));
        LoadAll();
        _this = this;
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
        back_btn = findViewById(R.id.back_btn);
        userRegisterButton.setOnClickListener(v -> RegisterUser());
        tcpRegisterButton.setOnClickListener(v -> RegisterTCP());
        back_btn.setOnClickListener(v -> {

            Intent intent = new Intent(getBaseContext(), Start.class);
            startActivity(intent);
        });
        context = this;
        //_this = ;
    }



    void RegisterUser(){
        String s = checkUserFields();
        if(!s.equals("")) {
            Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
            return;
        }

        userRegisterButton.setEnabled(false);
        tcpRegisterButton.setEnabled(false);
        back_btn.setEnabled(false);
        Toast.makeText(this, String.format(Utils.longLoadingMsg, "создание кошелька"), Toast.LENGTH_LONG).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Заводин новый адресс, публичный ключ и приватный ключ
                    File folder = new File(getApplicationContext().getFilesDir(),"");
                    String str = WalletUtils.generateLightNewWalletFile(" ", folder);
                    Credentials credentials = WalletUtils.loadCredentials(" ",folder.getAbsolutePath() + "/" +str);
                    // Сохраняем всю интересующую нас информацию
                    /**
                     * Загружаем смарт контракт ( все действия будут выполнены не от имени регистрируемого пользователя,
                     * а от имени владельца контракта( того, кто его залил)
                     **/


                    String phoneNumber = phoneUSER.getText().toString();
                    // Если номер это просто то 1, то текущий пользователь не регистрируется( кул хак)
                    String newFileName = str;
                    File crFile = new File(folder.getAbsolutePath() + "/" +str);

                    //if (!(phoneNumber.length() == 1 && phoneNumber.charAt(0) == '1')) {
                        Credentials bankCredentials = Credentials.create(Config.bankPrivateKey,Config.bankPublicKey);
                        Loyalty contract = Loyalty.load(
                                Config.contractAdress,
                                web3,
                                bankCredentials,
                                Loyalty.GAS_PRICE,
                                Loyalty.GAS_LIMIT);

                        // Хэш номера телефона, который мы будем отправлять в блокчейн
                        BigInteger phoneHash = new BigInteger(
                                String.valueOf(phoneNumber.hashCode())
                        );

                        if(!isPhoneUnique(web3,bankCredentials,phoneHash)){
                            _this.runOnUiThread(() -> userRegisterButton.setEnabled(true));
                            _this.runOnUiThread(() -> back_btn.setEnabled(true));
                            _this.runOnUiThread(() -> Toast.makeText(context,"Такой номер уже зарегистрирован в сети",Toast.LENGTH_SHORT).show());
                            return;
                        }

                        newFileName = phoneHash.toString()+".json";
                        Boolean s = crFile.renameTo(new File(folder.getAbsolutePath() + "/"+newFileName));

                        File crFile1 = new File(folder.getAbsolutePath() + "/" +str);
                        System.out.println(phoneNumber);
                        System.out.println(phoneHash);
                        System.out.println(phoneHash.bitLength());

                        // Регистрируем пользователя
                        RemoteCall<TransactionReceipt> c = contract.addCustomer(
                                credentials.getAddress(),
                                phoneHash
                        );

                        Future<TransactionReceipt> a = c.sendAsync();
                        System.out.println(a.toString());
                        a.get();


                   // }else{
                        //System.out.println("COOL HACK");
                    //}
                    SharedPreferences sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("NAME",newFileName);
                    editor.putString("PATH",folder.getAbsolutePath());
                    editor.putBoolean(Config.IS_TCP,false);
                    editor.apply();

                    Utils.sendNotification(context, String.format("Создание кошелька завершено, теперь вы можете войти!\n" +
                            "Номер телефона: %s\nАдрес: %s", phoneNumber, credentials.getAddress()), 2);
                    // Перезагружаемся, иначе вылетает

                    if(!(phoneNumber.length() == 1 && phoneNumber.charAt(0) == '1')) {
                        Intent i = getBaseContext().getPackageManager()
                                .getLaunchIntentForPackage(getBaseContext().getPackageName());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }else {
                        Intent intent = new Intent(getBaseContext(), Office_User.class);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    _this.runOnUiThread(() -> userRegisterButton.setEnabled(true));
                    _this.runOnUiThread(() -> ((TextView)(findViewById(R.id.textView2))).setText(e.toString()));
                    e.printStackTrace();
                }
            }
        }).start();
    }
    void RegisterTCP(){
        String s = checkTCPFields();
        if(!s.equals("")) {
            Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
            return;
        }
        userRegisterButton.setEnabled(false);
        tcpRegisterButton.setEnabled(false);
        back_btn.setEnabled(false);
        Toast.makeText(this, String.format(Utils.longLoadingMsg, "создание кошелька"), Toast.LENGTH_LONG).show();
        new Thread(() -> {
            try {
                //Заводин новый адресс, публичный ключ и приватный ключ
                File folder = new File(getApplicationContext().getFilesDir(),"");
                String str = WalletUtils.generateLightNewWalletFile(" ", folder);
                Credentials credentials = WalletUtils.loadCredentials(" ",folder.getAbsolutePath() + "/" +str);
                Credentials bankCredentials = Credentials.create(Config.bankPrivateKey, Config.bankPublicKey);
                // Сохраняем всю интересующую нас информацию


                /**
                 * Загружаем смарт контракт ( все действия будут выполнены не от имени регистрируемого пользователя,
                 * а от имени владельца контракта( того, кто его залил)
                 **/
                Loyalty contract = Loyalty.load(
                        Config.contractAdress,/*Адресс контракта (указан в конфиге) */
                        web3,/* */
                        Credentials.create(Config.bankPrivateKey, Config.bankPublicKey),/**/
                        Loyalty.GAS_PRICE,
                        Loyalty.GAS_LIMIT);

                String phoneNumber = phoneTCP.getText().toString();
                String companyName = nameTCP.getText().toString();


                String newFileName = str;
                File crFile = new File(folder.getAbsolutePath() + "/" +str);

                // Если номер это просто то 1, то текущий пользователь не регистрируется( кул хак)
                //if (!(phoneNumber.length() == 1 && phoneNumber.charAt(0) == '1')) {

                    // Хэш номера телефона, который мы будем отправлять в блокчейн
                    BigInteger phoneHash = new BigInteger(
                            String.valueOf(phoneNumber.hashCode())
                    );

                    if(!isPhoneUnique(web3,bankCredentials,phoneHash)){
                        _this.runOnUiThread(() -> tcpRegisterButton.setEnabled(true));
                        _this.runOnUiThread(() -> back_btn.setEnabled(true));
                        _this.runOnUiThread(() -> Toast.makeText(context,"Такой номер уже зарегистрирован в сети",Toast.LENGTH_SHORT).show());
                        return;
                    }
                    if(!isNameUnique(web3,bankCredentials,companyName)){
                        _this.runOnUiThread(() -> tcpRegisterButton.setEnabled(true));
                        _this.runOnUiThread(() -> back_btn.setEnabled(true));
                        _this.runOnUiThread(() -> Toast.makeText(context,"Компания с таким именем уже зарегистрирован в сети",Toast.LENGTH_SHORT).show());
                        return;
                    }
                    newFileName = phoneHash.toString()+".json";
                    Boolean s1 = crFile.renameTo(new File(folder.getAbsolutePath() + "/"+newFileName));

                    /*
                    if(crFile.delete())
                    {
                        System.out.println("File deleted successfully");
                    }
                    else
                    {
                        System.out.println("Failed to delete the file");
                    }*/

                    System.out.println(phoneNumber);
                    System.out.println(phoneHash);
                    System.out.println(phoneHash.bitLength());

                    // Регистрируем пользователя
                    contract.addCompany(
                            credentials.getAddress(),
                            companyName,
                            phoneHash
                    ).send();

                //}else{
                //    System.out.println("COOL HACK");
                //}

                SharedPreferences sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("NAME", newFileName);
                editor.putString("PATH",folder.getAbsolutePath());
                editor.putBoolean(Config.IS_TCP,true);
                editor.apply();


                Utils.AddEth1(web3,credentials);

                Utils.sendNotification(context, String.format("Создание кошелька завершено, теперь вы можете войти!\n" +
                        "Номер телефона: %s\nНазвание: %s\nАдрес: %s", phoneNumber, companyName, credentials.getAddress()), 2);
                Intent i = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                // Переходим на сцену с личным кабинетом компании
                //Intent intent = new Intent(getBaseContext(), Office_TCP.class);
                //startActivity(intent);

            }catch(Exception e){
                tcpRegisterButton.setEnabled(true);
                ((TextView)(findViewById(R.id.textView2))).setText(e.toString());
                e.printStackTrace();
            }
        }).start();

    }
    String checkUserFields(){
        String phoneNumber = phoneUSER.getText().toString();
        if(phoneNumber.length() == 0){
            return "Не введен номер телефона";
        }
        return "";
    }
    String checkTCPFields(){
        String phoneNumber = phoneTCP.getText().toString();
        if(phoneNumber.length() == 0){
            return "Не введен номер телефона";
        }

        String companyName = nameTCP.getText().toString();

        if(companyName.length() == 0){
            return "Не введено имя компании";
        }

        return "";
    }

    boolean isNameUnique(Web3j web3, Credentials bankCredentials,String name){
        Loyalty bankContract = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty
        .GAS_LIMIT);

        try {
            return bankContract.nameIsUnique(name).send();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    boolean isPhoneUnique(Web3j web3, Credentials bankCredentials,BigInteger phoneHash){
        Loyalty bankContract = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty
                .GAS_LIMIT);
        //System.out.println(bankCredentials.getEcKeyPair().getPublicKey().toString(16));
        try {
            return bankContract.phoneIsUnique(phoneHash).send();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
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

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.back_btn_msg, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);
    }
}
