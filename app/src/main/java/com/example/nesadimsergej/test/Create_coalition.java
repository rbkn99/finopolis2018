package com.example.nesadimsergej.test;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

public class Create_coalition extends SceneController {

    EditText coalitionName;
    Button createCoalitionBtn;

    ConstraintLayout createCoalitionPart,coalitionCreated;

    CoalitionPage coalitionPage = null;

    public Create_coalition(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene(){
        super.SetUpScene();
        createCoalitionPart = page.findViewById(R.id.createCoalitionPart);
        coalitionCreated = page.findViewById(R.id.coalitionCreated);

        coalitionName = createCoalitionPart.findViewById(R.id.coalitionName);
        createCoalitionBtn = createCoalitionPart.findViewById(R.id.createCoalitionBtn);


        CoalitionNotExists();
        createCoalitionBtn.setOnClickListener(v -> {


            Runnable bonusUpdater = () -> CreateCoalition();
            Thread thread = new Thread(bonusUpdater);
            thread.setPriority(Thread.MIN_PRIORITY);
            thread.start();

        });

    }

    @Override
    void OnSelected() {
        super.OnSelected();
        if(isCoalitionExists()){
            CoaltionExists();
            coalitionPage.OnSelected();
        }else{
            CoalitionNotExists();
        }
    }

    void CoalitionNotExists(){
        createCoalitionPart.setVisibility(View.VISIBLE);
        coalitionCreated.setVisibility(View.INVISIBLE);
    }

    void CoaltionExists(){
        createCoalitionPart.setVisibility(View.INVISIBLE);
        coalitionCreated.setVisibility(View.VISIBLE);
        if(coalitionPage == null) {
            coalitionPage = new CoalitionPage(coalitionCreated, ((Office) page.getContext()).credentials.getAddress());
            coalitionPage.back.setVisibility(View.INVISIBLE);
        }
    }

    Boolean isCoalitionExists(){
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                credentials,
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        String address = credentials.getAddress();
        try {
            //Company s =new Company( contract.companies(credentials.getAddress()).send());
            CoalitionWrapper coalition = new CoalitionWrapper(contract.coalitions(address).send(),address);
            //System.out.println(s);
            return coalition.exists;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    void CreateCoalition(){

        String cName = coalitionName.getText().toString();
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                Credentials.create(Config.bankPrivateKey),
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        System.out.println(cName);
       try {
           ((Office)page.getContext()).runOnUiThread(() -> Toast.makeText(page.getContext(), "Запрос отправлен на обработку",
                   Toast.LENGTH_SHORT).show());

           contract.addCoalition(credentials.getAddress(), cName).send();
           ((Office)page.getContext()).runOnUiThread(() -> Toast.makeText(page.getContext(), "Коалиция создана",
                   Toast.LENGTH_SHORT).show());

           ((Office)page.getContext()).runOnUiThread(() ->  OnSelected());
        }catch (Exception e){
           ((Office)page.getContext()).runOnUiThread(() -> Toast.makeText(page.getContext(), "Во время создании коалиции произошла ошибка",
                   Toast.LENGTH_SHORT).show());
            e.printStackTrace();
        }

    }


}

