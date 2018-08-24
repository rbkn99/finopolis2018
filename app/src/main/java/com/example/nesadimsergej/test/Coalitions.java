package com.example.nesadimsergej.test;

import android.view.View;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;

public class Coalitions extends SceneController {


    public Coalitions(View _page){
        super();
        page = _page;
        SetUpScene();
    }

    @Override
    void SetUpScene() {

        super.SetUpScene();
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

        for(BigInteger i = BigInteger.ZERO ; i.compareTo(coalitionCount) == -1 ; i = i.add( BigInteger.ONE)) {
            try {

                String coalitionAddress = contract.getCompanyCoalition(i).sendAsync().get();


                Tuple2<Boolean, String> coalition = contract.coalitions(coalitionAddress).send();
                CoalitionWrapper coalitionWrapper = new CoalitionWrapper(coalition,coalitionAddress);

            }catch (Exception e){
                e.printStackTrace();
            }
        }


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
