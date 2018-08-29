package com.example.nesadimsergej.test;

import android.hardware.SensorManager;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple8;

import java.math.BigInteger;

import static android.content.Context.SENSOR_SERVICE;

public class User_bonuses extends SceneController{

    TableLayout bonusesTable;

    TextView ifEmpty;

    public User_bonuses(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene(){
        super.SetUpScene();

        bonusesTable = page.findViewById(R.id.bonusesTable);
        ifEmpty = page.findViewById(R.id.ifEmpty);
        ((Office)page.getContext()).AddCompanyUpdatedListener(office -> {

            Runnable bonusUpdater = () -> UpdateBonuses();
            Thread thread = new Thread(bonusUpdater);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();

        });
    }


    // Функция обновляет список компанием с указанием количества бонусов
    void UpdateBonuses(){
        Office office = (Office)page.getContext();
        // Стираем все старое
        RemoveAllBonusRows();
        Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;
        Loyalty loyalty = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        int addCount = 0;
        // Перебираем все коспании
        for (Company c:
                office.companies
             ) {
            // Адрес компании, которую мы рассматриваем в текущий момент
            // С помощью него мы будем получать сначала адрес токена этой компании(если он есть)
            // а потом и баланс пользователя
            String companyAddress = c._address;

            try {
                //Tuple8<Boolean, BigInteger, String, String, Boolean, String, BigInteger, BigInteger> s = loyalty.companies(companyAddress).send();
                c =Utils.getCompany(web3,credentials,companyAddress); //new Company(s);
            }catch (Exception e){
                // В случае если что-то пошло не так мы просто переходим к рассмотрению следующей компании
                e.printStackTrace();
                continue;
            }
            // В случае если компания пока что не завела свой токен мы просто переходим к следующей компании
            if(c==null){
                System.out.println("NULL");
                continue;
            }
            if(!c.hasToken){
                continue;
            }

            String tokeAddress = c.token;

            // Сейчас мы попробуем узнать баланс пользователя
            Token contract = Token.load(tokeAddress,web3,credentials,
                    Token.GAS_PRICE,Token.GAS_LIMIT);
            try {

                BigInteger userBalance = contract.balanceOf(credentials.getAddress()).send();
                // Делим userBalance на 10^18 так как в solidity только целые числа и что бы передать туда вещ число
                // нужно домножить его на 10^18
                userBalance = userBalance.divide(Config.tene18);
                System.out.println(userBalance);

                if (userBalance.compareTo(BigInteger.ZERO) < 1)
                    continue;

                // Добавляем строчку в список бонусов
                addCount ++;
                BonusRow r = AddRow(c.companyName + " (" + contract.name().send() + ")  ");
                // Записываем это число в нашу строчку
                final BigInteger uB = userBalance;

                ((Office)page.getContext()).runOnUiThread(() -> r.SetNumber1(uB.toString()));

            }catch (Exception e){
                e.printStackTrace();
            }
        }
        int _addCount = addCount;
        ((Office)page.getContext()).runOnUiThread(() -> {

            if(_addCount == 0){
                ifEmpty.setVisibility(View.VISIBLE);
            }else{
                ifEmpty.setVisibility(View.INVISIBLE);
            }

        });

    }

    // Функция которая добавляют на сцену bonus_row.xml
    // Устанавливает название компании равное text
    BonusRow AddRow(String text){
        View view = View.inflate(page.getContext(),R.layout.bonus_row,null);


        ((Office)page.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bonusesTable.addView(view);
                // Stuff that updates the UI

            }
        });

        BonusRow r = new BonusRow(view);
        ((Office)page.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                r.SetText(text);
                // Stuff that updates the UI
            }
        });

        return r;
    }

    void RemoveAllBonusRows(){

        ((Office)page.getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bonusesTable.removeAllViews();
                // Stuff that updates the UI

            }
        });
    }

}

class BonusRow {

    TextView bonusCompanyName;
    TextView bonusNumber1;

    public void SetText(String text){
        bonusCompanyName.setText(text);
    }

    public BonusRow(View row){

        bonusCompanyName = row.findViewById(R.id.bonusCompanyName);
        bonusNumber1 = row.findViewById(R.id.bonusNumber1);
    }

    public void SetNumber1(String s){
        bonusNumber1.setText(s);
    }

}


