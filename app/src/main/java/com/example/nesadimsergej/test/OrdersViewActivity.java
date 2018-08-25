package com.example.nesadimsergej.test;

import android.content.SharedPreferences;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import java.util.ArrayList;

public class OrdersViewActivity extends AppCompatActivity {

    Web3j web3;
    Credentials credentials;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_view);
        web3 = Web3jFactory.build(new HttpService(Config.web3Address));
        sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
        try {
            credentials = WalletUtils.loadCredentials(" ",
                    sharedPref.getString("PATH", "NA") + "/" + sharedPref.getString("NAME", "NA"));
        }
        catch (Exception e) {

        }
        LoadAllOrders();
    }

    void LoadAllOrders() {
        ArrayList<Order> _companies = new ArrayList<>();
        boolean hadError = false;
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        try {
            Toast.makeText(this, contract.owner().send(), Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            System.err.println("hujnia");
        }
    }
}
