package com.example.nesadimsergej.test;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple8;

import java.math.BigInteger;
import java.util.ArrayList;

public class CoalitionPage extends SceneController {

    TextView coalitionNameTV;
    TextView coalitionAddressTV;


    String coalitionAddress;
    LinearLayout coalitionMembers;

    Button inviteButton;
    public Button back;
    EditText inviteAddress;


    CoaltionInfo coaltionInfo = new CoaltionInfo(new Tuple2<>(false,"ERROR"));

    public CoalitionPage(View _page, String _coalitionAddress){
        super();
        page = _page;
        coalitionAddress = _coalitionAddress;

        SetUpScene();

    }

    @Override
    void SetUpScene(){
        super.SetUpScene();
        coalitionNameTV = page.findViewById(R.id.coalitionNameText);
        coalitionAddressTV = page.findViewById(R.id.coaltionAddressText);
        inviteButton = page.findViewById(R.id.inviteButton);
        inviteAddress = page.findViewById(R.id.inviteAddress);
        coalitionMembers = page.findViewById(R.id.coalitionMembers);
        back = page.findViewById(R.id.back);

        coalitionMembers.removeAllViews();

        LoadCoalitionInfo();
        DisplayCoalitionInfo();

        if(coalitionAddress.equals(((Office)page.getContext()).credentials.getAddress())) {
            inviteButton.setVisibility(View.VISIBLE);
            inviteAddress.setVisibility(View.VISIBLE);
            inviteButton.setOnClickListener(v -> {

                Runnable bonusUpdater = () -> Invite();
                Thread thread = new Thread(bonusUpdater);
                thread.setPriority(Thread.MIN_PRIORITY);
                thread.start();

            });

        }else{
            inviteButton.setVisibility(View.INVISIBLE);
            inviteAddress.setVisibility(View.INVISIBLE);
        }
        //OnSelected();
    }

    @Override
    void OnSelected() {
        super.OnSelected();
        Runnable bonusUpdater = () -> UpdateCoalitionMembers();
        Thread thread = new Thread(bonusUpdater);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }

    void Invite(){

        String cAddress = inviteAddress.getText().toString();
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                credentials,
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        try {
            Company c = new Company(contract.companies(cAddress).send());
            if(!c.exists){
                ((Office)page.getContext()).runOnUiThread(() ->
                        Toast.makeText(page.getContext(),"Компании с таким адресом не существует",Toast.LENGTH_SHORT).show());
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            ((Office)page.getContext()).runOnUiThread(() ->
                    Toast.makeText(page.getContext(),"Во время отправки произошла ошибка",Toast.LENGTH_SHORT).show());
            return;
        }

        if(cAddress.equals(credentials.getAddress())){
            ((Office)page.getContext()).runOnUiThread(() ->
                    Toast.makeText(page.getContext(),"Нельзя приглашать самого себя в коалицию",Toast.LENGTH_SHORT).show());
            return;
        }


        BigInteger size = BigInteger.ZERO;
        try{
            size = contract.getCoalitionSize(credentials.getAddress()).send();
        }catch (Exception e){
            e.printStackTrace();
            ((Office)page.getContext()).runOnUiThread(() ->
                    Toast.makeText(page.getContext(),"Во время отправки произошла ошибка",Toast.LENGTH_SHORT).show());
            return;
        }




        boolean inCoalition = false;
        for(BigInteger i = BigInteger.ZERO ; i.compareTo(size) == -1 ; i = i.add( BigInteger.ONE)) {

            try {
                String s = contract.getCoalitionMember(credentials.getAddress(),i).send();
                if(s.equals(credentials.getAddress())){
                    inCoalition = true;

                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                break;
            }

        }

        if(inCoalition){
            ((Office)page.getContext()).runOnUiThread(() ->
                    Toast.makeText(page.getContext(),"Данная компания уже состоит в этой коалиции",Toast.LENGTH_SHORT).show());
            return;
        }

        try {

        }catch (Exception e){
            e.printStackTrace();

        }

        try {
            ((Office)page.getContext()).runOnUiThread(() ->  Toast.makeText(page.getContext(),"Запрос отправлен на обработку", Toast.LENGTH_SHORT).show());

            contract.inviteToCoalition(cAddress).sendAsync().get();

            ((Office)page.getContext()).runOnUiThread(() ->  Toast.makeText(page.getContext(),"Приглашение отправлено", Toast.LENGTH_SHORT).show());

        }catch (Exception e){
            ((Office)page.getContext()).runOnUiThread(() ->
                    Toast.makeText(page.getContext(),"Во время отправки произошла ошибка",Toast.LENGTH_SHORT).show());
            e.printStackTrace();

        }
    }



    void LoadCoalitionInfo(){
        System.out.println("LoadCoalitionInfo");
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                credentials,
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        try {
            Tuple2<Boolean, String> s = contract.coalitions(coalitionAddress).send();
            coaltionInfo = new CoaltionInfo(s);
            System.out.println(s);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void DisplayCoalitionInfo(){
        coalitionAddressTV.setText(coalitionAddress);
        coalitionNameTV.setText(coaltionInfo.coalitionName);
    }

    void UpdateCoalitionMembers(){
        ((Office)page.getContext()).runOnUiThread(() -> {
            coalitionMembers.removeAllViews();
            System.out.println("removed");
        });

        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                credentials,
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        BigInteger coalitionSize = BigInteger.ZERO;
        try {
            coalitionSize = contract.getCoalitionSize(coalitionAddress).send();
        }catch (Exception e){

        }

        ArrayList<String> userAddresses = new ArrayList<>();
        for(BigInteger i = BigInteger.ZERO ; i.compareTo(coalitionSize) == -1 ; i = i.add( BigInteger.ONE)) {
            try {
                String s = contract.getCoalitionMember(coalitionAddress,i).send();
                userAddresses.add(s);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for (String address:userAddresses
             ) {

            AddRow(web3,credentials,address);
        }
    }

    void AddRow(Web3j web3, Credentials credentials,String companyAddress){
        System.out.println("add");
        ((Office)page.getContext()).runOnUiThread(() -> {
            View view = View.inflate(page.getContext(),R.layout.company_coalition,null);
            coalitionMembers.addView(view);
            UserRow r = new UserRow(view);

            r.SetName(Utils.getLastRequestedCompany(web3,credentials,companyAddress).companyName);
        });

        //return r;
    }

    class CoaltionInfo{
        Boolean exists;
        String coalitionName;

        public CoaltionInfo(Tuple2<Boolean, String> s){
            exists = s.getValue1();
            coalitionName = s.getValue2();
        }
    }

    class UserRow{
        TextView userName;
        public UserRow(View view){
            userName = view.findViewById(R.id.userName);
        }
        void SetName(String text){
            userName.setText(text);
        }

    }

}


