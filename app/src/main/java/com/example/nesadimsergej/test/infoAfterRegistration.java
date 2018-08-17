package com.example.nesadimsergej.test;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class infoAfterRegistration extends AppCompatActivity {

    TextView addressTV, publicKeyTV,privateKeyTV;
    private ClipboardManager myClipboard;
    private ClipData myClip;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_after_registration);
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);

        sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
        addressTV = findViewById(R.id.address);
        publicKeyTV = findViewById(R.id.publicKey);
        privateKeyTV = findViewById(R.id.privateKey);

        addressTV.setText(
                sharedPref.getString(Config.ADDRESS, "IDI"));
        publicKeyTV.setText(
                sharedPref.getString(Config.PUBLIC_KEY, "NA"));
        privateKeyTV.setText(
                sharedPref.getString(Config.PRIVATE_KEY, "3 HUYA"));

        // Переход в личный кабинет
        (findViewById(R.id.toOfficeButton)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ToOfficeBtnClicked();
            }
        });

        // Устанавливаем обработчик нажатий чтобы при нажатии текст копировался
        View.OnClickListener o = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = ((TextView)v).getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();
            }
        };

        addressTV.setOnClickListener(o);
        publicKeyTV.setOnClickListener(o);
        privateKeyTV.setOnClickListener(o);

    }

    void ToOfficeBtnClicked(){
        Intent intent;
        if(sharedPref.getBoolean(Config.IS_TCP,false)) {
            intent = new Intent(this, Office_TCP.class);
        }else {
            intent = new Intent(this, Office_User.class);
        }
        startActivity(intent);
    }

}
