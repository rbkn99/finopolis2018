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


    CoaltionInfo coaltionInfo = new CoaltionInfo(new Tuple2<>(false,"Кое кто получит пизды"));

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
            System.out.println("here");

            inviteButton.setVisibility(View.VISIBLE);
            inviteAddress.setVisibility(View.VISIBLE);
            inviteButton.setOnClickListener(v -> Invite());

        }else{
            inviteButton.setVisibility(View.INVISIBLE);
            inviteAddress.setVisibility(View.INVISIBLE);
        }
        OnSelected();
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

            System.out.println( Utils.getCompany(web3,credentials,credentials.getAddress()));
            System.out.println( contract.coalitions(credentials.getAddress()).send());

        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            System.out.println(credentials.getAddress());
            System.out.println(cAddress);
            contract.inviteToCoalition(cAddress).sendAsync().get();


            Toast.makeText(page.getContext(),"Приглашение отправлено", Toast.LENGTH_SHORT).show();
        }catch (Exception e){

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

        ((Office)page.getContext()).runOnUiThread(() -> coalitionMembers.removeAllViews());

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


