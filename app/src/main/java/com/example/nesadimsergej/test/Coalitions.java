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
    View coalitionSelected;
    View displayCoalitions;

    boolean showCoalition = false;
    CoalitionPage coalitionPage = null;

    public Coalitions(View _page){
        super();
        page = _page;
        SetUpScene();
    }

    @Override
    void SetUpScene() {

        super.SetUpScene();
        coalitionList = page.findViewById(R.id.coalitionList);

        coalitionSelected = page.findViewById(R.id.coalitionSelected);
        displayCoalitions = page.findViewById(R.id.displayCoalitions);
        DisplayListOfCoalitions();
    }

    @Override
    void OnSelected(){
        super.OnSelected();

        Runnable bonusUpdater = () -> LoadCompanyCoalitions();
        Thread thread = new Thread(bonusUpdater);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();



        if(!showCoalition){
            DisplayListOfCoalitions();
        }else{
            DisplaySelectedCoalition();
            coalitionPage.OnSelected();
        }

    }

    void LoadCompanyCoalitions(){
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                credentials,
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);


        BigInteger coalitionCount = BigInteger.ZERO;
        try {
            coalitionCount = contract.getCompanyCoalitionCount(credentials.getAddress()).send();

        }catch (Exception e){
            e.printStackTrace();
        }
        //System.out.println(coalitionCount);

        ArrayList<CoalitionWrapper> coalitions = new ArrayList<>();


        for(BigInteger i = BigInteger.ZERO ; i.compareTo(coalitionCount) == -1 ; i = i.add( BigInteger.ONE)) {
            try {

                String coalitionAddress = contract.getCompanyCoalition(credentials.getAddress(),i).sendAsync().get();
                Tuple2<Boolean, String> coalition = contract.coalitions(coalitionAddress).send();
                CoalitionWrapper coalitionWrapper = new CoalitionWrapper(coalition,coalitionAddress);
                coalitions.add(coalitionWrapper);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        ((Office)page.getContext()).runOnUiThread(() ->  coalitionList.removeAllViews());

        for (CoalitionWrapper coalition:coalitions
             ) {

            ((Office)page.getContext()).runOnUiThread(() ->  {
                View view = View.inflate(page.getContext(),R.layout.coalition_row,null);
                coalitionList.addView(view);
                //((TextView)view.findViewById(R.id.QueriText)).setText(text);
                AddCoalition(coalition,view);
            });

        }
    }

    CoalitionRow AddCoalition( CoalitionWrapper coalition,View view){
        CoalitionRow row = new CoalitionRow(view, coalition, new CoalitionCallback() {
            @Override
            public void func(CoalitionRow row) {
                OnClick(row);
            }
        });
        row.SetText(coalition.name);
        return row;
    }

    void DisplayListOfCoalitions(){
        coalitionSelected.setVisibility(View.INVISIBLE);
        displayCoalitions.setVisibility(View.VISIBLE);
        showCoalition = false;
    }

    void DisplaySelectedCoalition(){
        coalitionSelected.setVisibility(View.VISIBLE);
        displayCoalitions.setVisibility(View.INVISIBLE);
        showCoalition = true;

    }

    void OnClick(CoalitionRow row){
        //if(coalitionPage == null){
        coalitionPage = new CoalitionPage(page,row.wrapper.address);
        coalitionPage.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DisplayListOfCoalitions();
            }
        });
        //}
        DisplaySelectedCoalition();
        //System.out.println(row.wrapper.name);

    }

    interface CoalitionCallback {

        void func(CoalitionRow row);
    }
}



class CoalitionRow{

    View view;
    CoalitionWrapper wrapper;
    TextView text;
    Coalitions.CoalitionCallback onClick;

    public CoalitionRow(View _view, CoalitionWrapper _wrapper,Coalitions.CoalitionCallback _onClick){
        view = _view;
        wrapper = _wrapper;

        text = view.findViewById(R.id.text);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClick();
            }
        });
        onClick = _onClick;
    }

    public void SetText(String txt){
        text.setText(txt);
    }

    public void OnClick(){
        onClick.func(this);
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
