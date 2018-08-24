package com.example.nesadimsergej.test;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.math.BigInteger;
import java.util.ArrayList;

public class Pay_bonuses extends SceneController {

    Spinner companySelector,tokenSelector;
    EditText paySum,sum;
    Button payBtn;
    //TextView tokenName;

    public Pay_bonuses(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene(){
        super.SetUpScene();
        companySelector = page.findViewById(R.id.companySelector);
        //bonusSelector = page.findViewById(R.id.bonusSelector);
        paySum = page.findViewById(R.id.paySum);
        sum = page.findViewById(R.id.sum);
        payBtn = page.findViewById(R.id.payBtn);
        //tokenName = page.findViewById(R.id.tokenName);
        tokenSelector = page.findViewById(R.id.tokenSelector);

        companySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
            {
                CompanySelected(parent,view,pos,id);
            }

            public void onNothingSelected(AdapterView<?> parent)
            {

            }

        });
        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //companySelector.setAdapter(adapter);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pay();
            }
        });
        //dropdown.setSelection(0);
        ((Office)page.getContext()).AddCompanyUpdatedListener(new CompanyListUpdatedListener() {
            @Override
            public void f(Office office) {
                UpdateCompaniesDropdowns(office);
            }
        });
    }
    ArrayList<Company> companies;
    void UpdateCompaniesDropdowns(Office office){
        companies = office.companies;
        if(office.companies.isEmpty())
            return;

        ArrayList<String> companyNames = new ArrayList<>();
        for (Company c:office.companies
             ) {
            companyNames.add(c.companyName);
        }

        int selectedItem = companySelector.getSelectedItemPosition();
        String selectedCompanyName = "";
        if(selectedItem >=0)
            selectedCompanyName =(String) companySelector.getItemAtPosition(selectedItem);

        //System.out.println("EEE BOOOI");
        //System.out.println(selectedCompanyName);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(office, android.R.layout.simple_spinner_dropdown_item, companyNames);
        companySelector.setAdapter(adapter);

        int newSelectedItem = 0;
        if(!selectedCompanyName.equals(""))
            newSelectedItem = companyNames.indexOf(selectedCompanyName);
        companySelector.setSelection(newSelectedItem);
    }

    void CompanySelected(AdapterView<?> parent, View view, int position, long id){
        //System.out.println(companies.get(position).companyName);

        Company selectedCompany = companies.get(position);

        Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;

        Loyalty loyaltyContract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        try {


            Company normalCompany = new Company(loyaltyContract.companies(selectedCompany._address).send());
            if(normalCompany.hasToken){

                CalculatePossibleTokens(web3,credentials,normalCompany);
                ArrayList<TokenWrapper> tokens = CalculatePossibleTokens(web3,credentials,normalCompany);

                TokenWrapper selected =(TokenWrapper) tokenSelector.getSelectedItem();

                int i = tokens.indexOf(selected);

                ArrayAdapter<TokenWrapper> adapter = new ArrayAdapter<>(page.getContext(), android.R.layout.simple_spinner_dropdown_item, tokens);
                tokenSelector.setAdapter(adapter);
                tokenSelector.setSelection(i);

            }else{

            }
        }catch (Exception e){

        }
    }
    ArrayList<TokenWrapper> CalculatePossibleTokens(Web3j web3,Credentials credentials,Company company){
        ArrayList<TokenWrapper> tokens = new ArrayList<>();
        tokens.add(getToken(web3,credentials,company.token));

        return tokens;
    }

    TokenWrapper getToken(Web3j web3,Credentials credential,String address){
        Token tokenContract = Token.load(address,web3,credential,Token.GAS_PRICE,Token.GAS_LIMIT);
        String tokenName = "ERROR";
        try{
            tokenName = tokenContract.name().send();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new TokenWrapper(address,tokenName);
    }



    void Pay(){
        Company selectedCompany = companies.get(companySelector.getSelectedItemPosition());
        TokenWrapper selectedToken =(TokenWrapper) tokenSelector.getSelectedItem();

        Web3j web3 = ((Office)page.getContext()).web3;
        Credentials credentials = ((Office)page.getContext()).credentials;
        Credentials bankCredentials = Credentials.create(Config.prk,Config.puk);

        Loyalty loyaltyContractBank = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        String paySumStr = paySum.getText().toString();

        if(paySumStr.isEmpty()){
            Toast.makeText(page.getContext(),"Введите сумму в рублях",Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            BigInteger sumR = (new BigInteger(paySumStr)).multiply(new BigInteger("1000000000000000000"));

            loyaltyContractBank.transferBonuses(selectedCompany._address, credentials.getAddress(),
                    sumR, BigInteger.ZERO,
                    "0x0000000000000000000000000000000000000000").send();


        }catch (Exception e){
            System.out.println("хуй");
            e.printStackTrace();
        }

    }

}
