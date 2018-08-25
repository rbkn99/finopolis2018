package com.example.nesadimsergej.test;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple8;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

public class Office extends AppCompatActivity {

    protected Office context;

    protected View balanceP,transactionP,eP;

    public ArrayList<View> pages = new ArrayList<>();

    protected ArrayList<Company> companies = new ArrayList<>();

    public ArrayList<Timer> timers = new ArrayList<>();

    protected Map<Integer, Integer> map = new HashMap<>();
    protected Map<Integer, SceneController> idToScene = new HashMap<>();


    public Credentials credentials;
    public Web3j web3;

    protected TextView money;
    protected Button updateBalanceBtn,addEth,infoBtn;
    protected SharedPreferences sharedPref;
    protected EditText balanceCheater;
    protected Button exitOfficeBtn;
    protected BottomNavigationView bottomNavigationView;

    protected EditText targetAddress;
    protected EditText targetSum;
    protected Button sendEth;
    protected Button contractTest;
    protected Button deployContractBtn;

    protected ClipData myClip;
    protected ClipboardManager myClipboard;

    protected DrawerLayout mDrawerLayout;

    protected CompanyListUpdatedEvent listUpdatedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        context = this;
        listUpdatedEvent = new CompanyListUpdatedEvent(context);

    }
    protected void HideAllPgs(){
        for (View v:pages
                ) {
            v.setVisibility(View.GONE);
        }
    }
    protected void UnHidePage(int id){
        for (View v:pages
                ) {

            if(v.getId() == id) {
                v.setVisibility(View.VISIBLE);
                if (idToScene.containsKey(id)) {
                    idToScene.get(id).OnSelected();
                } else {
                    //System.out.println("here201");
                }
            }
        }
    }
    protected boolean opened = false;
    protected void SetUpDrawerLayout(){
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.Balance);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        HideAllPgs();
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        UnHidePage(map.get(item.getItemId()));

                        return true;
                    }
                });

        navigationView.getMenu().performIdentifierAction(R.id.Balance,0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        try {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }catch (Exception e){

        }
        try {
            actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }catch (Exception e){

        }
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                        opened = true;
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                        opened = false;
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

    }

    protected int max(int a, int b){
        if(a>= b)
            return a;
        return b;
    }

    protected  void InfoPopUP(){
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.info_pop_up);


        TextView privateKeyInfo = dialog.findViewById(R.id.PrKUO);
        TextView publicKeyInfo = dialog.findViewById(R.id.PuKUO);
        TextView addressInfo = dialog.findViewById(R.id.AdUO);
        TextView pathTV = dialog.findViewById(R.id.pathTV);
        TextView nameTV = dialog.findViewById(R.id.nameTV);


        addressInfo.setText(
                credentials.getAddress());

        ECKeyPair p = credentials.getEcKeyPair();

        publicKeyInfo.setText(
                p.getPublicKey().toString(16));
        privateKeyInfo.setText(
                p.getPrivateKey().toString(16));

        pathTV.setText(sharedPref.getString("PATH", "NA"));
        nameTV.setText(sharedPref.getString("NAME", "NA"));

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

        addressInfo.setOnClickListener(o);
        publicKeyInfo.setOnClickListener(o);
        privateKeyInfo.setOnClickListener(o);
        pathTV.setOnClickListener(o);
        nameTV.setOnClickListener(o);
        dialog.show();
    }

    protected void AddEth(){

        new Thread(new Runnable() {
            public void run() {
                try {

                    float v = Float.parseFloat(balanceCheater.getText().toString());

                    TransactionReceipt transactionReceipt =
                            Transfer.sendFunds(web3, Credentials.create(Config.secretKey1), credentials.getAddress(),
                                    BigDecimal.valueOf(v), Convert.Unit.ETHER).sendAsync().get(20, TimeUnit.SECONDS);
                    UpdateBalance();

                } catch (Exception e){
                    System.out.println(e);
                }
            }
        }).run();
    }
    protected void AddEth1(){

        new Thread(new Runnable() {
            public void run() {
                try {

                    float v = Config.AddBalance;

                    TransactionReceipt transactionReceipt =
                            Transfer.sendFunds(web3, Credentials.create(Config.secretKey1), credentials.getAddress(),
                                    BigDecimal.valueOf(v), Convert.Unit.ETHER).sendAsync().get(20, TimeUnit.SECONDS);
                    UpdateBalance();

                } catch (Exception e){
                    System.out.println(e);
                }
            }
        }).run();
    }

    protected void UpdateBalance(){
        new Thread(new Runnable() {
            public void run() {
                updateBalanceBtn.setEnabled(false);
                try {
                    EthGetBalance ethGetBalance = web3
                            .ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                            .sendAsync().get(20, TimeUnit.SECONDS);
                    // Еще что-то делаем
                    BigInteger wei = ethGetBalance.getBalance();
                    //System.out.println(wei);
                    String result = wei.toString();

                    int l = result.length();
                    for(int i = l; i<18;i++)
                        result = "0"+result;

                    String a = result.substring(max( result.length() - 18,0));
                    String b = result.substring(0,max( result.length() - 18,0));
                    if( b.equals( "") || b.equals(" "))
                        b = "0";
                    result = b+"."+a;

                    money.setText(result);
                    updateBalanceBtn.setEnabled(true);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error!",
                            Toast.LENGTH_SHORT).show();
                    updateBalanceBtn.setEnabled(true);
                }
            }
        }).run();
    }

    protected void Contract(){

        try {

            Loyalty contract = Loyalty
                    .load(Config.contractAdress,web3,Credentials.create(Config.prk
                            ,Config.puk),Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

            //contract.

            System.out.println("Contract address:"+contract.getContractAddress());
            try {
                BigInteger i = new BigInteger("12345");
                System.out.println(i.bitCount());

                TransactionReceipt a = contract.addCompany(credentials.getAddress(),"PidorasCo",i).sendAsync().get();

                Toast.makeText(getApplicationContext(),  a.getBlockNumber().toString(),
                        Toast.LENGTH_SHORT).show();

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Pizdec!",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("error1");
        }

    }
    String contractAddress = "";
    protected void UploadContract(){
        new Thread(new Runnable() {
            public void run() {
                deployContractBtn.setEnabled(false);
                try {

                    Loyalty contract = Loyalty
                            .deploy(web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT.add(new BigInteger("400000"))).send();

                    contractAddress = contract.getContractAddress();
                    deployContractBtn.setEnabled(true);
                    System.out.println("Contract address1: "+contractAddress);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error!",
                            Toast.LENGTH_SHORT).show();
                    deployContractBtn.setEnabled(true);
                }
            }
        }).run();
    }

    //
    protected void SendEth(){
        new Thread(new Runnable() {
            public void run() {
                String address = targetAddress.getText().toString();
                float v = Float.parseFloat(targetSum.getText().toString());

                try {
                    TransactionReceipt transactionReceipt =
                            Transfer.sendFunds(web3, credentials, address,
                                    BigDecimal.valueOf(v), Convert.Unit.ETHER).sendAsync().get(20, TimeUnit.SECONDS);

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error!",
                            Toast.LENGTH_SHORT).show();
                }
                UpdateBalance();
            }
        }).run();
    }

    protected void LoadAll(){
        balanceP = findViewById(R.id.balanceP);
        transactionP = findViewById(R.id.transactionP);
        eP = findViewById(R.id.eP);
        deployContractBtn = findViewById(R.id.deployContractBtn);
        contractTest = findViewById(R.id.contractBtn);
        infoBtn = findViewById(R.id.infoBtn);
        targetAddress = findViewById(R.id.targetAddress);
        targetSum = findViewById(R.id.targetSum);
        sendEth = findViewById(R.id.sendEth);
        exitOfficeBtn = findViewById(R.id.exitOfficeBtn);
        money = findViewById(R.id.ethInfo);
        updateBalanceBtn = findViewById(R.id.updateBalanceBtn);
        balanceCheater = findViewById(R.id.balanceCheater);
        addEth = findViewById(R.id.addEth);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        web3 = Web3jFactory.build(new HttpService(Config.web3Address));

        try {
            Web3ClientVersion web3ClientVersion = web3.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            System.out.println("Client version: "+clientVersion);
        }catch (Exception e){
            System.out.println("No Client version");
            e.printStackTrace();
        }
        sharedPref = getSharedPreferences(Config.AccountInfo, MODE_PRIVATE);
        try {
            credentials = WalletUtils.loadCredentials(" ",
                    sharedPref.getString("PATH", "NA") + "/" + sharedPref.getString("NAME", "NA"));
        }catch (Exception e){

        }
    }

    public void AddCompanyUpdatedListener(CompanyListUpdatedListener listener){
        listUpdatedEvent.addListener(listener);
    }

    //protected void Company

    protected void LoadAllCompanies(){

        ArrayList<Company> _companies = new ArrayList<>();
        boolean hadError = false;
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        BigInteger companiesCount = BigInteger.ZERO;
        try {

            companiesCount = contract.companiesCount().send();

        }
        catch (Exception e){
            e.printStackTrace();
            hadError = true;
        }

        for(BigInteger i = BigInteger.ZERO ; i.compareTo(companiesCount) == -1 ; i = i.add( BigInteger.ONE)) {

            try {
                Tuple8<Boolean, BigInteger, String, String, Boolean, String, BigInteger, BigInteger> s = contract.companySet(i).send();
                Company currentCompany = new Company(s);
                //System.out.println(currentCompany.toString());
                _companies.add(currentCompany);

            }catch (Exception e){

                e.printStackTrace();
                hadError = true;
                break;

            }

        }

        if (! hadError){
            companies = _companies;
            listUpdatedEvent.sayHello();

        }else{
            Toast.makeText(this,"Andrey daolbaeb",Toast.LENGTH_LONG).show();
        }
    }

    void Exit(){
        for (Timer t:
             timers) {
            t.cancel();
        }
        SharedPreferences.Editor e = sharedPref.edit();
        //e.clear();
        //e.remove("PATH");
        //e.remove("NAME");
        e.apply();
        Intent intent = new Intent(this, start.class);
        startActivity(intent);
    }
}


class Company{

    public boolean exists;
    public BigInteger phoneNumber;
    public String companyName;
    public String _address;
    public boolean hasToken;
    public String token;

    public BigInteger deposit;

    public BigInteger requestCount;
    String coalition;
    boolean hasCoalition;

    /*
        struct Company {
        bool exists;
        uint phoneNumber;
        string name;
        address _address;

        bool has_token;
        Token token;
        uint256 deposit;

        Request[] request_pool;
        address[] coalitionNames;
        uint64 request_count;
    }
     */


    private Tuple8<Boolean, BigInteger, String, String, Boolean, String, BigInteger, BigInteger> constructorTuple;
    public Company(Tuple8<Boolean, BigInteger, String, String, Boolean, String, BigInteger, BigInteger> s){
        exists = s.getValue1();
        phoneNumber = s.getValue2();
        companyName = s.getValue3();
        _address = s.getValue4();
        hasToken = s.getValue5();
        token = s.getValue6();
        deposit = s.getValue7();
        //coalition = s.getValue8();
        //hasCoalition = s.getValue9();
        requestCount = s.getValue8();
        constructorTuple = s;
    }

    @Override
    public String toString(){
        return constructorTuple.toString();
    }
}

class TokenWrapper{

    String address;
    String name;
    String ownerAddress;
    String nominalOwner;
    public TokenWrapper(String _address,String _name,String _ownerAddress,String _nominalOwner){
        address = _address;
        name = _name;
        ownerAddress = _ownerAddress;
        nominalOwner = _nominalOwner;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TokenWrapper ){
            return address.equals(((TokenWrapper) obj).address);
        }else {
            return super.equals(obj);
        }
    }
    @Override
    public int hashCode(){

        return address.hashCode();
    }
}

class TokenWrapperWithBalance{
    TokenWrapper wrapper;
    BigInteger balance;
    public TokenWrapperWithBalance(String _address,String _name, BigInteger _balance, String ownerAddress,String _nominalAddress){
        wrapper = new TokenWrapper(_address,_name,ownerAddress,_nominalAddress);
        balance = _balance;
    }
    @Override
    public String toString() {
        return wrapper.name;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof TokenWrapperWithBalance ){
            return wrapper.equals(((TokenWrapperWithBalance) obj).wrapper);
        }else {
            return super.equals(obj);
        }
    }
    @Override
    public int hashCode(){

        return wrapper.hashCode();
    }

}


interface CompanyListUpdatedListener {
    void f(Office office);
}

class CompanyListUpdatedEvent {
    private List<CompanyListUpdatedListener> listeners = new ArrayList<CompanyListUpdatedListener>();

    Office office;
    public CompanyListUpdatedEvent(Office _office){
        office = _office;
    }

    public void addListener(CompanyListUpdatedListener toAdd) {
        listeners.add(toAdd);
    }

    public void sayHello() {
        System.out.println("Hello!!");

        // Notify everybody that may be interested.
        for (CompanyListUpdatedListener hl : listeners)
            hl.f(office);
    }
}

// Someone interested in "Hello" events
class Responder implements CompanyListUpdatedListener {



    @Override
    public void f(Office office) {

    }
}
