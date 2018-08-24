package com.example.nesadimsergej.test;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;
import java.util.ArrayList;

public class Coalitions extends SceneController {

    LinearLayout coalitionList;

    public Coalitions(View _page){
        super();
        page = _page;
        SetUpScene();
    }

    @Override
    void SetUpScene() {

        super.SetUpScene();
        coalitionList = page.findViewById(R.id.coalitionList);
    }

    @Override
    void OnSelected(){
        super.OnSelected();
        LoadCompanyCoalitions();

    }

    void LoadCompanyCoalitions(){
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                credentials,
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);


        BigInteger coalitionCount = BigInteger.ZERO;
        try {
            coalitionCount = contract.getCompanyCoalitionCount().send();

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(coalitionCount);

        ArrayList<CoalitionWrapper> coalitions = new ArrayList<>();


        for(BigInteger i = BigInteger.ZERO ; i.compareTo(coalitionCount) == -1 ; i = i.add( BigInteger.ONE)) {
            try {

                String coalitionAddress = contract.getCompanyCoalition(i).sendAsync().get();
                Tuple2<Boolean, String> coalition = contract.coalitions(coalitionAddress).send();
                CoalitionWrapper coalitionWrapper = new CoalitionWrapper(coalition,coalitionAddress);
                coalitions.add(coalitionWrapper);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        coalitionList.removeAllViews();
        for (CoalitionWrapper coalition:coalitions
             ) {

            View view = View.inflate(page.getContext(),R.layout.coalition_row,null);

            coalitionList.addView(view);
            //((TextView)view.findViewById(R.id.QueriText)).setText(text);

            AddCoalition(coalition,view);
        }
    }

    CoalitionRow AddCoalition( CoalitionWrapper coalition,View view){
        CoalitionRow row = new CoalitionRow(view,coalition);
        row.SetText(coalition.name);
        return row;
    }
}



class CoalitionRow{

    View view;
    CoalitionWrapper wrapper;

    TextView text;

    public CoalitionRow(View _view, CoalitionWrapper _wrapper){
        view = _view;
        wrapper = _wrapper;

        text = view.findViewById(R.id.text);
    }

    public void SetText(String txt){
        text.setText(txt);
    }
}

class CoalitionWrapper{

    String address;

    boolean exists;
    String name;

    public CoalitionWrapper(Tuple2<Boolean, String> _coalition, String _address){
        address = _address;
        exists = _coalition.getValue1();
        name = _coalition.getValue2();
    }
}
