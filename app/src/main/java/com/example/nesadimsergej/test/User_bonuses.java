package com.example.nesadimsergej.test;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple10;
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tuples.generated.Tuple8;

import java.math.BigInteger;
import java.util.Timer;
import java.util.TimerTask;

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

    /*class BonusUpdater extends TimerTask {
        @Override
        public void run() {
            ((Activity)page.getContext()).runOnUiThread(() -> UpdateBonuses());
        }
    }*/
    public String tene18 = "1000000000000000000";

    void UpdateBonuses(Office office){
        RemoveAllBonusRows();Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;
        Loyalty loyalty = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        for (Company c:
                office.companies
             ) {


            String companyAddress = c._address;


            try {
                Tuple8<Boolean, BigInteger, String, String, Boolean, String, BigInteger, BigInteger> s = loyalty.companies(companyAddress).send();
                c = new Company(s);
                System.out.println(s);
            }catch (Exception e){
                System.out.println("GayBar");
                e.printStackTrace();
            }
            if(!c.hasToken){
                continue;
            }
            BonusRow r = AddRow(c.companyName);
            String tokeAddress = c.token;

            //Token.load(tokeAddress,)

            Token contract = Token.load(tokeAddress,web3,credentials,
                    Token.GAS_PRICE,Token.GAS_LIMIT);
            try {
                BigInteger bi = contract.balanceOf(credentials.getAddress()).send();
                bi = bi.divide(new BigInteger(tene18));
                r.SetNumber1(bi.toString());

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

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


