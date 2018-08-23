package com.example.nesadimsergej.test;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.web3j.tuples.generated.Tuple4;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tuples.generated.Tuple6;

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


    void UpdateBonuses(Office office){
        RemoveAllBonusRows();
        for (Company c:
                office.companies
             ) {
            AddRow(c.companyName);
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


