package com.example.nesadimsergej.test;

import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple8;

import java.math.BigInteger;

public class User_bonuses extends SceneController {

    TableLayout bonusesTable;

    public User_bonuses(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene(){
        super.SetUpScene();

        bonusesTable = page.findViewById(R.id.bonusesTable);

        //UpdateBonuses();

        //Timer timer = new Timer();
        //timer.schedule(new BonusUpdater(), 0, 15000);
        ((Office)page.getContext()).AddCompanyUpdatedListener(new CompanyListUpdatedListener() {
            @Override
            public void f(Office office) {
                UpdateBonuses(office);
            }
        });
    }

    public String tene18 = "1000000000000000000";

    // Функция обновляет список компанием с указанием количества бонусов
    void UpdateBonuses(Office office){
        // Стираем все старое
        RemoveAllBonusRows();
        Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;
        Loyalty loyalty = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        // Перебираем все коспании
        for (Company c:
                office.companies
             ) {
            // Адрес компании, которую мы рассматриваем в текущий момент
            // С помощью него мы будем получать сначала адрес токена этой компании(если он есть)
            // а потом и баланс пользователя
            String companyAddress = c._address;

            try {
                Tuple8<Boolean, BigInteger, String, String, Boolean, String, BigInteger, BigInteger> s = loyalty.companies(companyAddress).send();
                c = new Company(s);
            }catch (Exception e){
                // В случае если что-то пошло не так мы просто переходим к рассмотрению следующей компании
                e.printStackTrace();
                continue;
            }
            // В случае если компания пока что не завела свой токен мы просто переходим к следующей компании
            if(!c.hasToken){
                continue;
            }
            // Добавляем строчку в список бонусов
            BonusRow r = AddRow(c.companyName);
            String tokeAddress = c.token;

            // Сейчас мы попробуем узнать баланс пользователя
            Token contract = Token.load(tokeAddress,web3,credentials,
                    Token.GAS_PRICE,Token.GAS_LIMIT);
            try {

                BigInteger userBalance = contract.balanceOf(credentials.getAddress()).send();
                // Делим userBalance на 10^18 так как в solidity только целые числа и что бы передать туда вещ число
                // нужно домножить его на 10^18
                userBalance = userBalance.divide(new BigInteger(tene18));
                // Записываем это число в нашу строчку
                r.SetNumber1(userBalance.toString());

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    // Функция которая добавляют на сцену bonus_row.xml
    // Устанавливает название компании равное text
    BonusRow AddRow(String text){
        View view = View.inflate(page.getContext(),R.layout.bonus_row,null);
        bonusesTable.addView(view);
        BonusRow r = new BonusRow(view);
        r.SetText(text);
        return r;
    }

    void RemoveAllBonusRows(){

        bonusesTable.removeAllViews();

    }


}

class BonusRow{

    TextView bonusCompanyName;
    TextView bonusNumber1;
    TextView bonusNumber2;

    public void SetText(String text){
        bonusCompanyName.setText(text);
    }

    public BonusRow(View row){

        bonusCompanyName = row.findViewById(R.id.bonusCompanyName);
        bonusNumber1 = row.findViewById(R.id.bonusNumber1);
        bonusNumber2 = row.findViewById(R.id.bonusNumber2);
    }

    public void SetNumber1(String s){
        bonusNumber1.setText(s);
    }

}


