package com.example.nesadimsergej.test;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.ArrayList;

public class Exchange_bonuses extends SceneController {

    Spinner bonus1,bonus2;

    TextView balance1,balance2;

    EditText exchangeCount1,exchangeCount2;
    TextView resultBonus;
    Button changeInCoalition;

    Button makeOffer_button, viewOffers_button;

    Switch tradeSwitch;

    View exchange_window,offers;

    ViewOffers viewOffers;

    public Exchange_bonuses(View _page){
        super();
        page = _page;

        SetUpScene();
    }


    boolean viewingOffers = false;

    @Override
    void SetUpScene(){
        super.SetUpScene();

        exchange_window = page.findViewById(R.id.exchange_window);
        offers = page.findViewById(R.id.offers);

        tradeSwitch = page.findViewById(R.id.tradeSwitch);
        bonus1 = page.findViewById(R.id.bonus1);
        bonus2 = page.findViewById(R.id.bonus2);
        balance1 = page.findViewById(R.id.balance1);
        balance2 = page.findViewById(R.id.balance2);
        exchangeCount1 = page.findViewById(R.id.exchangeCount1);
        exchangeCount2 = page.findViewById(R.id.exchangeCount2);

        viewOffers_button = page.findViewById(R.id.viewOffers);
        makeOffer_button = page.findViewById(R.id.makeOffer);
        resultBonus = page.findViewById(R.id.resultBonus);
        changeInCoalition = page.findViewById(R.id.changeInCoalition);
        makeOffer_button.setOnClickListener(v -> MakeOffer());
        TradeInCoalition();
        tradeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // в зависимости от значения isChecked выводим нужное сообщение
                if (isChecked) {
                    TradeInStockExchange();
                } else {
                    TradeInCoalition();
                }
            }
        });

        changeInCoalition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Change();
            }
        });

        bonus1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @SuppressLint("SetTextI18n")
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
            @SuppressLint("SetTextI18n")
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                TokenWrapperWithBalance bonus =(TokenWrapperWithBalance) bonus2.getItemAtPosition(pos);
                balance2.setText(bonus.balance.toString());
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });

        viewOffers_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayOfferWindow();
            }
        });

        viewOffers = new ViewOffers(offers);
    }

    void TradeInCoalition(){
        changeInCoalition.setVisibility(View.VISIBLE);
        resultBonus.setVisibility(View.VISIBLE);
        viewOffers_button.setVisibility(View.INVISIBLE);
        makeOffer_button.setVisibility(View.INVISIBLE);
        exchangeCount2.setVisibility(View.INVISIBLE);
    }
    void TradeInStockExchange(){
        changeInCoalition.setVisibility(View.INVISIBLE);
        resultBonus.setVisibility(View.INVISIBLE);
        viewOffers_button.setVisibility(View.VISIBLE);
        makeOffer_button.setVisibility(View.VISIBLE);
        exchangeCount2.setVisibility(View.VISIBLE);
    }


    @Override
    void OnSelected(){
        if(viewingOffers){
            exchange_window.setVisibility(View.INVISIBLE);
            offers.setVisibility(View.VISIBLE);

        }else{
            exchange_window.setVisibility(View.VISIBLE);
            offers.setVisibility(View.INVISIBLE);

            Runnable bonusUpdater = new Runnable() {
                @Override
                public void run() {
                    UpdateBonuses1();
                }
            };
            Thread thread = new Thread(bonusUpdater);
            thread.setPriority(Thread.NORM_PRIORITY);
            thread.start();

        }
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
            try {
                Company normalCompany = new Company(loyalty.companies(c._address).send());
                if(!normalCompany.hasToken){
                    continue;
                }
                Token tokenContract = Token.load(normalCompany.token,web3,credentials,Token.GAS_PRICE,Token.GAS_LIMIT);

                TokenWrapper token = Pay_bonuses.getToken(web3,credentials,normalCompany.token);

                String nominalOwner = "ERROR";
                try{
                    nominalOwner = tokenContract.nominal_owner().send();
                }catch (Exception e){
                    e.printStackTrace();
                }
                BigInteger balance = BigInteger.ZERO;
                try{
                    balance = tokenContract.balanceOf(credentials.getAddress()).send().divide(
                            Config.tene18);
                }catch (Exception e){
                    e.printStackTrace();
                }

                TokenWrapperWithBalance tokenWithBalance = new TokenWrapperWithBalance(token.tokenAddress, token.name,balance,normalCompany._address,nominalOwner);
                tokens.add(tokenWithBalance);
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
            e.printStackTrace();
        }

        ArrayAdapter<TokenWrapperWithBalance> adapter = new ArrayAdapter<>(office, android.R.layout.simple_spinner_dropdown_item,tokens );

        ((Office)page.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bonus1.setAdapter(adapter);
                // Stuff that updates the UI

            }
        });




        int newSelectedItem = 0;
        if(selectedCompanyName1!=null)
            newSelectedItem = tokens.indexOf(selectedCompanyName1);

        final int newSI = newSelectedItem;
        ((Office)page.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                bonus1.setSelection(newSI);
                // Stuff that updates the UI

            }
        });


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
            Token currentToken = Token.load(token.tokenAddress,web3,credentials,Token.GAS_PRICE,Token.GAS_LIMIT);
            try {
                BigInteger balance = currentToken.balanceOf(credentials.getAddress()).send().divide(
                        Config.tene18
                );
                tokens.add(new TokenWrapperWithBalance(token.tokenAddress,token.name,balance,token.ownerAddress,token.nominalOwner));
            }catch (Exception e){

            }
        }

        ArrayAdapter<TokenWrapperWithBalance> adapter = new ArrayAdapter<>(page.getContext(), android.R.layout.simple_spinner_dropdown_item, tokens);
        bonus2.setAdapter(adapter);
        bonus2.setSelection(0);

    }

    void Change(){

        TokenWrapperWithBalance token1 =(TokenWrapperWithBalance) bonus1.getSelectedItem();
        TokenWrapperWithBalance token2 =(TokenWrapperWithBalance) bonus2.getSelectedItem();
        if(token1.wrapper.name.equals(token2.wrapper.name)){
            Toast.makeText(page.getContext(),"Нельзя обменивать одинаковые бонусы",Toast.LENGTH_SHORT);
            return;
        }

        String count1_string = exchangeCount1.getText().toString();
        if(count1_string.isEmpty()){
            Toast.makeText(page.getContext(),
                    "Введите количество бонусов, которые вы хотите обменять", Toast.LENGTH_SHORT).show();
            return;
        }

        BigInteger count = new BigInteger(count1_string);
        BigInteger count1_18 = count.multiply(Config.tene18);


        String debug = "";
        debug+="Название первого токена: "+token1.wrapper.name+"\n";
        debug+="Название второго токена: "+token2.wrapper.name+"\n";
        debug+="Количество первого токена: "+count+"\n";
        debug+="Адрес первого токена: "+token1.wrapper.tokenAddress+"\n";
        debug+="Адрес второго токена: "+token2.wrapper.tokenAddress+"\n";
        debug+=": "+token1.wrapper.ownerAddress+"\n";
        debug+=": "+token2.wrapper.ownerAddress+"\n";
        debug+="Владелец первого токена: "+token1.wrapper.nominalOwner+"\n";
        debug+="Владелец второго токена: "+token2.wrapper.nominalOwner+"\n";
        System.out.println(debug);

        Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;
        Credentials bankCredentials = Credentials.create(Config.bankPrivateKey,Config.bankPublicKey);
        Loyalty bankLoyalty = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);



        Runnable bonusUpdater = new Runnable() {
            @Override
            public void run() {
                try{
                    String tokenOwner1 = token1.wrapper.nominalOwner;
                    String tokenOwner2 = token2.wrapper.nominalOwner;
                    bankLoyalty.exchangeToken(credentials.getAddress(),tokenOwner1,tokenOwner2,count1_18).send();

                    Toast(() -> Toast.makeText(page.getContext(),"Обмен прошел успешно!",Toast.LENGTH_SHORT).show());

                }catch (Exception e){
                    Toast(() -> {
                        Toast.makeText(page.getContext(),"Ошибка!",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    });

                }
            }
        };
        Thread thread = new Thread(bonusUpdater);
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.start();



    }

    void Toast(Runnable runnable){
        ((Office)page.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        });
    }

    void MakeOffer(){
        // Собираем всю интересующую нас информацию для публикации предложения
        TokenWrapperWithBalance token1 =(TokenWrapperWithBalance) bonus1.getSelectedItem();
        TokenWrapperWithBalance token2 =(TokenWrapperWithBalance) bonus2.getSelectedItem();
        if(token1.wrapper.name.equals(token2.wrapper.name)){

            Toast.makeText(page.getContext(),"Нельзя обменивать одинаковые бонусы",Toast.LENGTH_SHORT);
            return;
        }
        String count1_string,count2_string;
        count1_string = exchangeCount1.getText().toString();
        count2_string = exchangeCount2.getText().toString();
        if(count1_string.isEmpty() || count2_string.isEmpty()){
            Toast.makeText(page.getContext(),"Заполните оба поля",Toast.LENGTH_SHORT).show();
            return;
        }

        BigInteger count1,count2;
        count1 = new BigInteger(count1_string);
        count2 = new BigInteger(count2_string);


        // Проверяем есть ли у пользователя введенная сумма
        // Для второго откена такого нет, т.к. он их не отдает
        if(token1.balance.compareTo(count1) == -1){
            Toast.makeText(page.getContext(),"Количество первого бонуса не может превышать его текущий баланс",Toast.LENGTH_SHORT).show();
            return;
        }


        BigInteger count1_18,count2_18;
        count1_18 = count1.multiply(Config.tene18);
        count2_18 = count2.multiply(Config.tene18);


        Credentials bankCredentials = Credentials.create(Config.bankPrivateKey,Config.bankPublicKey);
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty bankContract = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        // И тут должен отпарвляться запрос

    }


    void DisplayOfferWindow(){
        viewingOffers = true;
        OnSelected();
    }
    void DisplayExchangeWindow(){
        viewingOffers = false;
        OnSelected();
    }
}
