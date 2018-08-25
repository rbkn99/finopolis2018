package com.example.nesadimsergej.test;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.ArrayList;

public class Exchange_bonuses extends SceneController {

    Spinner bonus1,bonus2;

    TextView balance1,balance2;

    EditText exchangeCount;
    TextView resultBonus;
    Button changeInCoalition;

    public Exchange_bonuses(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene(){
        super.SetUpScene();

        bonus1 = page.findViewById(R.id.bonus1);
        bonus2 = page.findViewById(R.id.bonus2);
        balance1 = page.findViewById(R.id.balance1);
        balance2 = page.findViewById(R.id.balance2);
        exchangeCount = page.findViewById(R.id.exchangeCount);
        resultBonus = page.findViewById(R.id.resultBonus);
        changeInCoalition = page.findViewById(R.id.changeInCoalition);
        //((Office)page.getContext()).AddCompanyUpdatedListener(new CompanyListUpdatedListener() {
        //    @Override
        //    public void f(Office office) {
        //        UpdateBonuses1(office);
        //    }
        //});

        changeInCoalition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change();
            }
        });

        bonus1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                TokenWrapperWithBalance bonus =(TokenWrapperWithBalance) bonus1.getItemAtPosition(pos);
                balance1.setText(bonus.balance.toString());
                UpdateBonuses2();
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });

        bonus2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                TokenWrapperWithBalance bonus =(TokenWrapperWithBalance) bonus2.getItemAtPosition(pos);
                balance2.setText(bonus.balance.toString());
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });

    }

    @Override
    void OnSelected(){
        System.out.println("UPDATEBONUSES1");
        UpdateBonuses1();
    }

    void UpdateBonuses1(){

        Office office = ((Office)page.getContext());

        Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;
        Loyalty loyalty = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        ArrayList<TokenWrapperWithBalance> tokens = new ArrayList<>();

        for (Company c:
                office.companies
                )
        {
            System.out.println("UPDATEBONUSES2");
            try {
                Company normalCompany = new Company(loyalty.companies(c._address).send());
                if(!normalCompany.hasToken){
                    continue;
                }
                Token tokenContract = Token.load(normalCompany.token,web3,credentials,Token.GAS_PRICE,Token.GAS_LIMIT);
                String tokenName = "ERROR";
                try{
                    tokenName = tokenContract.name().send();
                }catch (Exception e){
                    e.printStackTrace();
                }
                String nominalOwner = "ERROR";
                try{
                    nominalOwner = tokenContract.nominal_owner().send();
                }catch (Exception e){
                    e.printStackTrace();
                }
                BigInteger balance = BigInteger.ZERO;
                try{
                    balance = tokenContract.balanceOf(credentials.getAddress()).send().divide(
                            new BigInteger("1000000000000000000"));
                }catch (Exception e){
                    e.printStackTrace();
                }

                TokenWrapperWithBalance token = new TokenWrapperWithBalance(normalCompany.token,tokenName,balance,normalCompany._address,nominalOwner);
                tokens.add(token);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        int selectedItem1 = bonus1.getSelectedItemPosition();

        TokenWrapperWithBalance selectedCompanyName1 = null;
        try {
            if (selectedItem1 >= 0)
                selectedCompanyName1 = (TokenWrapperWithBalance) bonus1.getItemAtPosition(selectedItem1);
        }catch (Exception e){
            //no variants
        }
        /*
        int selectedItem2 = bonus1.getSelectedItemPosition();
        TokenWrapperWithBalance selectedCompanyName2 = null;

        try {
        if(selectedItem2 >=0)
            selectedCompanyName2 =(TokenWrapperWithBalance) bonus1.getItemAtPosition(selectedItem2);
        }catch (Exception e){
            //no variants
        }*/
        ArrayAdapter<TokenWrapperWithBalance> adapter = new ArrayAdapter<>(office, android.R.layout.simple_spinner_dropdown_item,tokens );
        bonus1.setAdapter(adapter);

        //bonus2.setAdapter(adapter);



        int newSelectedItem = 0;
        if(selectedCompanyName1!=null)
            newSelectedItem = tokens.indexOf(selectedCompanyName1);
        bonus1.setSelection(newSelectedItem);

        /*newSelectedItem = 0;
        if(selectedCompanyName2!=null)
            newSelectedItem = tokens.indexOf(selectedCompanyName2);
        bonus2.setSelection(newSelectedItem);*/
    }

    void UpdateBonuses2(){
        Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;

        Loyalty loyaltyContract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        TokenWrapperWithBalance bonus =(TokenWrapperWithBalance) bonus1.getItemAtPosition(bonus1.getSelectedItemPosition());
        System.out.println(bonus.wrapper.name);
        System.out.println(bonus.balance);
        String startCompany = bonus.wrapper.ownerAddress;
        Company company = null;
        try{
            company = new Company(loyaltyContract.companies(startCompany).send());
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        System.out.println(company.toString());
        ArrayList<TokenWrapper> s = Pay_bonuses.CalculatePossibleTokens(web3,credentials,company);

        ArrayList<TokenWrapperWithBalance> tokens= new ArrayList<>();

        for (TokenWrapper token:s) {
            Token currentToken = Token.load(token.address,web3,credentials,Token.GAS_PRICE,Token.GAS_LIMIT);
            try {
                BigInteger balance = currentToken.balanceOf(credentials.getAddress()).send().divide(
                        new BigInteger("1000000000000000000")
                );
                tokens.add(new TokenWrapperWithBalance(token.address,token.name,balance,token.ownerAddress,token.nominalOwner));
            }catch (Exception e){

            }
        }

        //System.out.println(s);
        ArrayAdapter<TokenWrapperWithBalance> adapter = new ArrayAdapter<>(page.getContext(), android.R.layout.simple_spinner_dropdown_item, tokens);
        bonus2.setAdapter(adapter);
        bonus2.setSelection(0);

    }

    void Change(){

        TokenWrapperWithBalance token1 =(TokenWrapperWithBalance) bonus1.getSelectedItem();
        TokenWrapperWithBalance token2 =(TokenWrapperWithBalance) bonus2.getSelectedItem();
        Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;
        Credentials bankCredentials = Credentials.create(Config.prk,Config.puk);
        Loyalty loyalty = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        BigInteger exchangeToken = new BigInteger(exchangeCount.getText().toString());


        String debug = "";
        debug+="Название первого токена: "+token1.wrapper.name+"\n";
        debug+="Название второго токена: "+token2.wrapper.name+"\n";
        debug+="Количество первого токена: "+exchangeToken+"\n";
        debug+="Адрес первого токена: "+token1.wrapper.address+"\n";
        debug+="Адрес второго токена: "+token2.wrapper.address+"\n";
        debug+=": "+token1.wrapper.ownerAddress+"\n";
        debug+=": "+token2.wrapper.ownerAddress+"\n";
        debug+="Владелец первого токена: "+token1.wrapper.nominalOwner+"\n";
        debug+="Владелец второго токена: "+token2.wrapper.nominalOwner+"\n";


        System.out.println(debug);

        BigInteger exchangeTokenR = exchangeToken.multiply(new BigInteger("1000000000000000000"));

        try{
            String tokenOwner1 = token1.wrapper.nominalOwner;
            String tokenOwner2 = token2.wrapper.nominalOwner;

            try {
                System.out.println(loyalty.companies(tokenOwner1).send());

            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                System.out.println(loyalty.companies(tokenOwner2).send());

            }catch (Exception e){
                e.printStackTrace();
            }

            loyalty.exchangeToken(credentials.getAddress(),tokenOwner1,tokenOwner2,exchangeTokenR).send();
        }catch (Exception e){
            e.printStackTrace();
        }

    }



}
