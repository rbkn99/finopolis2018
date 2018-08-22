package com.example.nesadimsergej.test;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;

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

        UpdateBonuses();

        Timer timer = new Timer();
        timer.schedule(new BonusUpdater(), 0, 15000);
    }

    class BonusUpdater extends TimerTask {
        @Override
        public void run() {
            ((Activity)page.getContext()).runOnUiThread(() -> UpdateBonuses());
        }
    }


    void UpdateBonuses(){
        RemoveAllBonusRows();

        Office of  = ((Office)page.getContext());
        Loyalty contract = Loyalty.load(Config.contractAdress,of.web3,of.credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        BigInteger companiesCount = BigInteger.ZERO;
        try {
            companiesCount = contract.companiesCount().send();
        }
        catch (Exception e){

        }

        for(BigInteger i = BigInteger.ZERO ; i.compareTo(companiesCount) == -1 ; i = i.add( BigInteger.ONE)) {
            System.out.println("here1111");
            try {
                Tuple5<Boolean, String, String, BigInteger, BigInteger> s = contract.companySet(i).sendAsync().get();
                //System.out.println(s.getValue3());
                AddRow(s.getValue3());

            }catch (Exception e){
                 System.out.println("ex1488");
                 e.printStackTrace();
            }
        }

    }

    void AddRow(String text){
        View view = View.inflate(page.getContext(),R.layout.bonus_row,null);
        bonusesTable.addView(view);
        BonusRow r = new BonusRow(view);
        r.SetText(text);
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
}


