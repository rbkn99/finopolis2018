package com.example.nesadimsergej.test;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Create_coalition extends SceneController {

    EditText coalitionName;
    Button createCoalitionBtn;

    public Create_coalition(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene(){
        super.SetUpScene();
        coalitionName = page.findViewById(R.id.coalitionName);
        createCoalitionBtn = page.findViewById(R.id.createCoalitionBtn);

        createCoalitionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateCoalition();
            }
        });

    }


    void CreateCoalition(){
        Toast.makeText(page.getContext(), "Успех",
                Toast.LENGTH_SHORT).show();
        // Создаем коалицию
    }


}
