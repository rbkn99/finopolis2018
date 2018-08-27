package com.example.nesadimsergej.test;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Pay_bonuses extends SceneController {

    Spinner companySelector,tokenSelector;
    EditText paySum, bonusesSum;
    Button payBtn;
    //TextView tokenName;

    public Pay_bonuses(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene(){
        super.SetUpScene();
        companySelector = page.findViewById(R.id.companySelector);
        paySum = page.findViewById(R.id.paySum);
        bonusesSum = page.findViewById(R.id.bonusesSum);
        payBtn = page.findViewById(R.id.payBtn);
        tokenSelector = page.findViewById(R.id.tokenSelector);

        companySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                Runnable bonusUpdater = () -> CompanySelected(parent,view,pos,id);;
                Thread thread = new Thread(bonusUpdater);
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.start();
            }

            public void onNothingSelected(AdapterView<?> parent)
            {
            }

        });

        payBtn.setOnClickListener(v -> Pay());
        //dropdown.setSelection(0);
        ((Office)page.getContext()).AddCompanyUpdatedListener(office -> {
            Runnable bonusUpdater = () -> UpdateCompaniesDropdowns();
            Thread thread = new Thread(bonusUpdater);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();
        });

    }
    ArrayList<Company> companies;

    void UpdateCompaniesDropdowns(){
        Office office = (Office)page.getContext();
        companies = office.companies;
        if(office.companies.isEmpty())
            return;

        ArrayList<String> companyNames = new ArrayList<>();
        for (Company c:office.companies
             ) {
            companyNames.add(c.companyName);
        }

        int selectedItem = companySelector.getSelectedItemPosition();
        String selectedCompanyName = "";
        if(selectedItem >=0)
            selectedCompanyName =(String) companySelector.getItemAtPosition(selectedItem);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(office, android.R.layout.simple_spinner_dropdown_item, companyNames);
        ((Office)page.getContext()).runOnUiThread(() -> companySelector.setAdapter(adapter));




        int newSelectedItem = 0;
        if(!selectedCompanyName.equals(""))
            newSelectedItem = companyNames.indexOf(selectedCompanyName);
        int newSI = newSelectedItem;
        ((Office)page.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                companySelector.setSelection(newSI);
                // Stuff that updates the UI

            }
        });

    }

    void CompanySelected(AdapterView<?> parent, View view, int position, long id){
        Company selectedCompany = companies.get(position);

        Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;

        Loyalty loyaltyContract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        try {


            Company normalCompany = new Company(loyaltyContract.companies(selectedCompany._address).send());
            if(normalCompany.hasToken){

                ArrayList<TokenWrapper> tokens = CalculatePossibleTokens(web3,credentials,normalCompany);

                TokenWrapper selected =(TokenWrapper) tokenSelector.getSelectedItem();

                int i = tokens.indexOf(selected);

                ArrayAdapter<TokenWrapper> adapter = new ArrayAdapter<>(page.getContext(), android.R.layout.simple_spinner_dropdown_item, tokens);

                ((Office)page.getContext()).runOnUiThread(() -> {
                    tokenSelector.setAdapter(adapter);
                    tokenSelector.setSelection(i);
                });
            }else{

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static ArrayList<TokenWrapper> CalculatePossibleTokens(Web3j web3,Credentials credentials,Company company){
        Set<TokenWrapper> tokens = new HashSet<>();
        tokens.add(getToken(web3,credentials,company.token));
        String startCompany = company._address;
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        ArrayList<String> coalitionAddresses = GetCoalitions(credentials,web3,contract,startCompany);

        for (String currentCoalition:coalitionAddresses)
        {
            ArrayList<String> coalitionMembers = GetCoalitionMember(credentials,web3,contract,currentCoalition);

            for (String currentCompanyAddress: coalitionMembers) {

                try {
                    Company currentCompany = new Company(contract.companies(currentCompanyAddress).send());
                    if(currentCompany.hasToken) {
                        tokens.add(getToken(web3, credentials, currentCompany.token));
                    }
                }catch (Exception e){

                }
            }
        }



        //Set<TokenWrapper> uniqueGas = new HashSet<TokenWrapper>(tokens);
        //System.out.println();
        ArrayList<TokenWrapper> result = new ArrayList<>();
        for (TokenWrapper token: tokens) {
            result.add(token);
        }

        return result;
    }

    public static ArrayList<String> GetCoalitionMember(Credentials credentials,Web3j web3,Loyalty contract, String coalitionAddress){
        BigInteger coalitionSize = BigInteger.ZERO;
        try {
            coalitionSize = contract.getCoalitionSize(coalitionAddress).send();
        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<String> coalitionMembers = new ArrayList<>();
        for(BigInteger i = BigInteger.ZERO ; i.compareTo(coalitionSize) == -1 ; i = i.add( BigInteger.ONE)) {
            try {
                String s = contract.getCoalitionMember(coalitionAddress,i).send();
                coalitionMembers.add(s);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return coalitionMembers;
    }
    public static ArrayList<String> GetCoalitions(Credentials credentials,Web3j web3,Loyalty contract, String companyAddress){

        BigInteger coalitionCount = BigInteger.ZERO;
        try {
            coalitionCount = contract.getCompanyCoalitionCount(companyAddress).send();

        }catch (Exception e){
            e.printStackTrace();
        }

        ArrayList<String> coalitions = new ArrayList<>();


        for(BigInteger i = BigInteger.ZERO ; i.compareTo(coalitionCount) == -1 ; i = i.add( BigInteger.ONE)) {
            try {
                String coalitionAddress = contract.getCompanyCoalition(companyAddress,i).sendAsync().get();
                Tuple2<Boolean, String> coalition = contract.coalitions(coalitionAddress).send();
                CoalitionWrapper coalitionWrapper = new CoalitionWrapper(coalition,coalitionAddress);
                coalitions.add(coalitionWrapper.address);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return coalitions;
    }


    public static Map<String, TokenWrapper> tokens = new HashMap<>();

    public static TokenWrapper getToken(Web3j web3,Credentials credential,String address){

        if(tokens.containsKey(address))
            return tokens.get(address);

        Token tokenContract = Token.load(address,web3,credential,Token.GAS_PRICE,Token.GAS_LIMIT);
        String tokenName = "ERROR";
        String owner = "ERROR";
        String nominal_owner = "ERROR";
        boolean had_error = false;

        BigInteger inPrice = BigInteger.ZERO;
        BigInteger outPrice = BigInteger.ZERO;
        BigInteger exchangePrice = BigInteger.ZERO;

        try{
            tokenName = tokenContract.name().send();
            owner = tokenContract.owner().send();
            nominal_owner = tokenContract.nominal_owner().send();
            inPrice = tokenContract.inPrice().send();
            outPrice = tokenContract.outPrice().send();
            exchangePrice = tokenContract.exchangePrice().send();
        }catch (Exception e){
            had_error = true;
            e.printStackTrace();
        }


        TokenWrapper token  = new TokenWrapper(address,tokenName,owner,nominal_owner,inPrice,outPrice,exchangePrice);
        if(!had_error){
            tokens.put(address,token);
        }
        return token;
    }


    void Pay(){
        Company selectedCompany = companies.get(companySelector.getSelectedItemPosition());

        TokenWrapper selectedToken =(TokenWrapper) tokenSelector.getSelectedItem();

        Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;
        Credentials bankCredentials = Credentials.create(Config.bankPrivateKey,Config.bankPublicKey);
        System.out.println("Start address: "+bankCredentials.getAddress());

        Loyalty loyaltyContractBank = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        try {
            selectedCompany = new Company(loyaltyContractBank.companies(selectedCompany._address).send());

        }catch (Exception e){
            e.printStackTrace();
        }

        String paySumStr = paySum.getText().toString();
        String bonusSumStr = bonusesSum.getText().toString();

        BigInteger bonusSum = BigInteger.ZERO;
        if(!bonusSumStr.equals("")){
            bonusSum = new BigInteger(bonusSumStr);
        }

        if(paySumStr.isEmpty()){
            Toast.makeText(page.getContext(),"Введите сумму в рублях",Toast.LENGTH_SHORT).show();
            return;
        }

        String tokenAddress = selectedToken.tokenAddress;
        Token tokenContract = Token.load(tokenAddress,web3,credentials, Token.GAS_PRICE, Token.GAS_LIMIT);

        BigInteger bonusCount = BigInteger.ZERO;
        String tokenOwner = "0x0000000000000000000000000000000000000000";
        try {
            bonusCount = tokenContract.balanceOf(credentials.getAddress()).send().divide(Config.tene18);
            tokenOwner = tokenContract.nominal_owner().send();
        }catch (Exception e){
            Toast.makeText(page.getContext(),"Error!",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if(bonusCount.compareTo(bonusSum) == -1){
            Toast.makeText(page.getContext(),"Недостаточно бонусов на счету",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            BigInteger sumR = (new BigInteger(paySumStr)).multiply(Config.tene18);
            BigInteger bonusSumR = bonusSum.multiply(Config.tene18);
            System.out.println("Сумма в рублях: "+sumR);
            System.out.println("Сумма в бонусах: "+bonusSumR);
            System.out.println("Адрес компании: "+selectedCompany._address);
            System.out.println("Имя компании: "+selectedCompany.companyName);

            if(bonusSumR.equals(BigInteger.ZERO))
                tokenOwner = "0x0000000000000000000000000000000000000000";
            System.out.println("Адрес владельца токена: "+tokenOwner);

            try {
                System.out.println(loyaltyContractBank.companies(selectedCompany._address).send());

            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                System.out.println(loyaltyContractBank.customers(credentials.getAddress()).send());

            }catch (Exception e){
                e.printStackTrace();
            }
            Company sC = selectedCompany;
            String tO = tokenOwner;

            Runnable bonusUpdater = () -> {
                try {

                    loyaltyContractBank.transferBonuses(sC._address, credentials.getAddress(),
                            sumR, bonusSumR,
                            tO).send();
                    ((Office)page.getContext()).runOnUiThread(() ->
                            Toast.makeText(page.getContext(),"Оплата прошла успешно",Toast.LENGTH_SHORT).show());

                }catch (Exception e){
                    ((Office)page.getContext()).runOnUiThread(() ->
                            Toast.makeText(page.getContext(),"Во время проведения оплаты произошла ошибка",Toast.LENGTH_SHORT).show());

                    e.printStackTrace();
                }
            };
            Thread thread = new Thread(bonusUpdater);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();


        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
