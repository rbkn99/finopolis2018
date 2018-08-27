package com.example.nesadimsergej.test;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigInteger;
import java.security.KeyStore;

public class Token_settings extends SceneController {


    EditText tokenName;
    EditText purchasePrise;
    EditText price_when_using;
    EditText swapPrice;
    Button createTokenBtn,payForToken;

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
        payForToken = page.findViewById(R.id.payForToken);
        payForToken.setOnClickListener(v -> PayForToken());
    }
    Company currentCompany = null;
    @Override
    void OnSelected() {
        super.OnSelected();
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        try {
            currentCompany = Utils.getCompany(web3,credentials,credentials.getAddress()); //new Company(contract.companies(credentials.getAddress()).send());

            if(currentCompany.hasToken){
                TokenWrapper companyToken = Utils.getToken(web3,credentials,currentCompany.token);



            }

        }catch (Exception e){
            e.printStackTrace();
        }

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
        BigInteger in_price = new BigInteger(in_price_string);
        BigInteger out_price = new BigInteger(out_price_string);
        BigInteger swap_price = new BigInteger(swap_price_string);

        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        RemoteCall<TransactionReceipt> s =contract.setToken(name,in_price,out_price,swap_price);

        Runnable bonusUpdater = () -> {
            try {
                s.send();
                ((Office)page.getContext()).runOnUiThread(() -> Toast.makeText(page.getContext(),
                        "Что-то произошло",
                        Toast.LENGTH_SHORT).show());
            }catch (Exception e){
                ((Office)page.getContext()).runOnUiThread(() -> Toast.makeText(page.getContext(),
                        "Недостаточно средств для создания",
                        Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(bonusUpdater);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

    }

    void PayForToken(){
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        System.out.println(credentials.getAddress());
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        try {
            contract.addEther(Config.AddToToken).send();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
