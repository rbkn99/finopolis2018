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

    String f(String s){
        if(s.equals(""))
            return "0";
        return s;

    }
    void CreateToken(){
        String name = tokenName.getText().toString();
        BigInteger in_prince = new BigInteger(f(purchasePrise.getText().toString()));
        BigInteger out_price = new BigInteger(f(price_when_using.getText().toString()));
        BigInteger swap_price = new BigInteger(f(swapPrice.getText().toString()));

        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        System.out.println(credentials.getAddress());
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        RemoteCall<TransactionReceipt> s =contract.setToken(name,in_prince,out_price,swap_price);

        try {
            s.send();
            Toast.makeText(page.getContext(), "Что-то произошло",
                    Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(page.getContext(), "Недостаточно средств для создания",
                            Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        //System.out.println("here");
        //Toast.makeText(page.getContext(), "Жду Рыбкина",
        //        Toast.LENGTH_SHORT).show();
    }

    void PayForToken(){
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        System.out.println(credentials.getAddress());
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        try {
            contract.addEther(new BigInteger("1000000000000000000")).send();
        }catch (Exception e){
            System.out.println("Ну охуеть теперь");
            e.printStackTrace();
        }
    }

}
