package com.example.nesadimsergej.test;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class Pay_bonuses extends SceneController {

    Spinner companySelector,bonusSelector;
    EditText paySum,sum;
    Button payBtn;

    public Pay_bonuses(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene(){
        super.SetUpScene();
        companySelector = page.findViewById(R.id.companySelector);
        bonusSelector = page.findViewById(R.id.bonusSelector);
        paySum = page.findViewById(R.id.paySum);
        sum = page.findViewById(R.id.sum);
        payBtn = page.findViewById(R.id.payBtn);


        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //companySelector.setAdapter(adapter);

        //dropdown.setSelection(0);
        ((Office)page.getContext()).AddCompanyUpdatedListener(new CompanyListUpdatedListener() {
            @Override
            public void f(Office office) {
                UpdateDropdowns(office);
            }
        });
    }

    void UpdateDropdowns(Office office){

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

        System.out.println("EEE BOOOI");
        System.out.println(selectedCompanyName);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(office, android.R.layout.simple_spinner_dropdown_item, companyNames);
        companySelector.setAdapter(adapter);

        int newSelectedItem = 0;
        if(!selectedCompanyName.equals(""))
            newSelectedItem = companyNames.indexOf(selectedCompanyName);
        companySelector.setSelection(newSelectedItem);

    }



}
