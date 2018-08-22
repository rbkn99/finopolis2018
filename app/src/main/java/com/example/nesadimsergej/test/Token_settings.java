package com.example.nesadimsergej.test;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Token_settings extends SceneController {


    EditText tokenName;
    EditText purchasePrise;
    EditText price_when_using;
    EditText swapPrice;
    Button createTokenBtn;

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
    }

    String f(String s){
        if(s.equals(""))
            return "0";
        return s;

    }
    void CreateToken(){
        String name = tokenName.getText().toString();
        Integer price = Integer.valueOf(f(purchasePrise.getText().toString()));
        Integer using_price = Integer.valueOf(f(price_when_using.getText().toString()));
        Integer swap_price = Integer.valueOf(f(swapPrice.getText().toString()));

        System.out.println("here");
        Toast.makeText(page.getContext(), "Жду Рыбкина",
                Toast.LENGTH_SHORT).show();
    }

}
