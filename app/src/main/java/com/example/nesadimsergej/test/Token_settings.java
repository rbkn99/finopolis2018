package com.example.nesadimsergej.test;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyStore;

import static android.support.v4.content.ContextCompat.getSystemService;

public class Token_settings extends SceneController {


    EditText tokenName;
    EditText purchasePrise;
    EditText price_when_using;
    EditText swapPrice;
    Button createTokenBtn,payForToken,helpButton;

    public Token_settings(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene() {
        super.SetUpScene();
        tokenName = page.findViewById(R.id.tokenName);
        purchasePrise = page.findViewById(R.id.purchasePrise);
        price_when_using = page.findViewById(R.id.price_when_using);
        swapPrice = page.findViewById(R.id.swapPrice);
        createTokenBtn = page.findViewById(R.id.createTokenBtn);
        createTokenBtn.setOnClickListener(v -> CreateToken());
        helpButton = page.findViewById(R.id.helpButton);
        payForToken = page.findViewById(R.id.payForToken);
        payForToken.setOnClickListener(v -> {
            Runnable bonusUpdater = () -> PayForToken();
            Thread thread = new Thread(bonusUpdater);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        });
        helpButton.setOnClickListener(v -> InfoPopUP());
    }
    Company currentCompany = null;
    boolean hasToken = false;
    @Override
    void OnSelected() {
        super.OnSelected();
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        //Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        try {
            currentCompany = Utils.getCompany(web3,credentials,credentials.getAddress()); //new Company(contract.companies(credentials.getAddress()).send());

            if(currentCompany.hasToken){
                TokenWrapper companyToken = Utils.getToken(web3,credentials,currentCompany.token);
                hasToken = true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @SuppressLint("SetTextI18n")
    protected  void InfoPopUP(){
        Dialog dialog = new Dialog(page.getContext());
        dialog.setContentView(R.layout.help_token_settings);


        dialog.show();

    }
    @SuppressLint("SetTextI18n")
    protected  void QuestionPopUp(Runnable onYes,Runnable onNo){
        AlertDialog.Builder builder = new AlertDialog.Builder(page.getContext());
        builder.setMessage("Предупреждение о том что токен сотрется")
                .setPositiveButton("Создать", (dialog
                        , id) -> {
                    onYes.run();
                })
                .setNegativeButton("Подумать", (dialog, id) -> {
                    onNo.run();
                });
        builder.create().show();
    }

    String f(String s){
        if(s.equals(""))
            return "0";
        return s;

    }
    void CreateToken(){

        String name = tokenName.getText().toString();
        String in_price_string = purchasePrise.getText().toString();
        String out_price_string = price_when_using.getText().toString();
        String swap_price_string = swapPrice.getText().toString();

        if(name.equals("")){
            Toast.makeText(page.getContext(),"Введите название бонусной валюты",Toast.LENGTH_SHORT).show();
            return;
        }
        if(in_price_string.equals("")){
            Toast.makeText(page.getContext(),"Введите цену при покупке",Toast.LENGTH_SHORT).show();
            return;
        }
        if(out_price_string.equals("")){
            Toast.makeText(page.getContext(),"Введите цену при использовании",Toast.LENGTH_SHORT).show();
            return;
        }
        if(swap_price_string.equals("")){
            Toast.makeText(page.getContext(),"Введите цену при обмене",Toast.LENGTH_SHORT).show();
            return;
        }

        double in_price_float = Double.valueOf(in_price_string);
        double out_price_float =Double.valueOf(out_price_string);
        double swap_price_float =Double.valueOf(swap_price_string);



        BigInteger in_price = (new BigDecimal(in_price_float).multiply(Config.tene18_decimal)).toBigInteger();//new BigInteger(in_price_string);
        BigInteger out_price = (new BigDecimal(out_price_float).multiply(Config.tene18_decimal)).toBigInteger();
        BigInteger swap_price = (new BigDecimal(swap_price_float).multiply(Config.tene18_decimal)).toBigInteger();

        System.out.println(in_price);
        System.out.println(out_price);
        System.out.println(swap_price);


        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        RemoteCall<TransactionReceipt> s =contract.setToken(name,in_price,out_price,swap_price);

        Runnable bonusUpdater = () -> {
            try {
                ((Office)page.getContext()).runOnUiThread(() -> Toast.makeText(page.getContext(),
                        "Отправляем запрос",
                        Toast.LENGTH_SHORT).show());

                s.send();

                ((Office)page.getContext()).runOnUiThread(() -> Toast.makeText(page.getContext(),
                        "Бонусная валюта добавлена",
                        Toast.LENGTH_SHORT).show());
            }catch (Exception e){
                ((Office)page.getContext()).runOnUiThread(() -> Toast.makeText(page.getContext(),
                        "Недостаточно средств для создания",
                        Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        };
        Runnable createToken = ()-> {
            Thread thread = new Thread(bonusUpdater);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        };
        if(hasToken){
            QuestionPopUp(() -> createToken.run(), () -> { });
        }else{
            createToken.run();
        }



    }

    void PayForToken(){
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        //System.out.println(credentials.getAddress());
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        try {
            contract.addEther(Config.AddToToken).send();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}