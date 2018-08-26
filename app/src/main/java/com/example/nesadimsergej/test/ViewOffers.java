package com.example.nesadimsergej.test;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple6;

import java.math.BigInteger;


public class ViewOffers extends SceneController {


    ConstraintLayout exampleQueri;
    LinearLayout offerList;
    public Button back;

    public ViewOffers(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene() {
        super.SetUpScene();

        back = page.findViewById(R.id.back);

        //Tuple6<BigInteger,String,String,String,BigInteger,BigInteger> _data = new Tuple6<>(
        //        BigInteger.ZERO,"0x0","0x0","0x0",BigInteger.ZERO,BigInteger.ZERO);

        //exampleQueri = page.findViewById(R.id.queri);
        offerList = page.findViewById(R.id.offerList);
        //for(Integer i = 0;i<20;i++)
        //    AddOffer(_data);

        //Timer timer = new Timer();
        //timer.schedule(new QueryUpdater(), 0, 30000);// Обновлять запросы каждые 30 секунд
        //((Office)page.getContext()).timers.add(timer);*/
        //page.getContext().
        UpdateOffers();
    }
    void UpdateOffers(){
        Credentials bankCredentials = Credentials.create(Config.bankPrivateKey,Config.bankPublicKey);
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        Loyalty bankContract = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        Loyalty contract = Loyalty.load(Config.contractAdress,web3,credentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        BigInteger stockSize = BigInteger.ZERO;

        try{
            stockSize = contract.getStockSize().send();
        }catch (Exception e){
            e.printStackTrace();
        }

        for(BigInteger i = BigInteger.ZERO ; i.compareTo(stockSize) == -1 ; i = i.add( BigInteger.ONE)) {
            try {

                Tuple6<BigInteger, String, String, String, BigInteger, BigInteger> offer = contract.getOfferFromStock(i).send();
                AddOffer(offer,web3,credentials,contract);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }


    void AddOffer(Tuple6<BigInteger,String,String,String,BigInteger,BigInteger> _data,Web3j web3,
                  Credentials credentials,Loyalty contract){

        View view = View.inflate(page.getContext(),R.layout.offer,null);
        offerList.addView(view);
        Offer offer = new Offer(view,_data);
        offer.LoadTokenData(web3,credentials,contract);

    }
/*

    void UpdateQueries(){
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                credentials,
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);


        BigInteger requestCount = BigInteger.ZERO;
        try {
            Company s =new Company( contract.companies(credentials.getAddress()).send());
            requestCount = s.requestCount;//contract.getRequestCount().send();//(new Company(s)).requestCount;
            System.out.println(s);
        }catch (Exception e){

        }

        ArrayList<Tuple2<String,String>> resultQueries = new ArrayList<>();
        System.out.println("Request count: "+requestCount);
        for(BigInteger i = BigInteger.ZERO ; i.compareTo(requestCount) == -1 ; i = i.add( BigInteger.ONE)) {
            try {

                String s = contract.getRequestOnIndex(i).sendAsync().get();
                resultQueries.add(new Tuple2<>(s,s));
                System.out.println(s);
                //System.out.println("UpdateQueries3");
                //System.out.println("hui");

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        queriList.removeAllViews();
        for (Tuple2<String,String> t: resultQueries
                ) {
            String queriText = t.getValue1();
            String secreteCode = t.getValue2();
            AddQueri(queriText,secreteCode);
        }


    }


    private void AnswerRequest(Queri q, boolean answer){

        String requestAddress = q.secreteCode;
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                credentials,
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        try {
            contract.respond(requestAddress, answer).send();
            Toast.makeText(page.getContext(), "движж???0)",
                    Toast.LENGTH_SHORT).show();
            q.Destroy();
        }catch (Exception e) {

        }
    }

    void OnQueriAccepted(Queri q){
        AnswerRequest(q,true);
    }
    void OnQueriDeclined(Queri q){


        AnswerRequest(q,false);
    }


    class QueryUpdater extends TimerTask {
        @Override
        public void run() {
            ((Activity)page.getContext()).runOnUiThread(new Runnable() {
                public void run() {
                    UpdateQueries();
                }
            });
        }
    }


    String[] wordList = new String[]{"Школа взоравалась","Привет всем, кто первый день на крокодиле"};
    String QueriGenerator(){
        return wordList[new Random().nextInt(wordList.length)];
    }
    */


}


/*
struct Offer {
        uint256 id;
        address seller;
        address sellToken;
        address wantedToken;
        uint256 sellAmount;
        uint256 buyAmount;
    }
 */

class Offer{

    BigInteger offerId;
    String sellerAddress;
    String sellTokenCompany;
    String wantedTokenCompany;
    BigInteger sellAmount;
    BigInteger buyAmount;

    View offerView;

    TokenWrapper sellToken = null;
    TokenWrapper buyToken = null;

    TextView offerId_TV,offer_seller,sellToken_TV, buyToken_TV;

    public void Destroy(){
        ((ViewGroup)offerView.getParent()).removeView(offerView);
    }


    private Tuple6<BigInteger,String,String,String,BigInteger,BigInteger> data;
    public Offer(View _offer, Tuple6<BigInteger,String,String,String,BigInteger,BigInteger> _data){
        offerView = _offer;
        offerId = _data.getValue1();
        sellerAddress = _data.getValue2();
        sellTokenCompany = _data.getValue3();
        wantedTokenCompany = _data.getValue4();
        sellAmount = _data.getValue5().divide(Config.tene18);
        buyAmount = _data.getValue6().divide(Config.tene18);

        data = _data;


        offerId_TV = _offer.findViewById(R.id.offerId);
        offer_seller = _offer.findViewById(R.id.offer_seller);
        sellToken_TV = _offer.findViewById(R.id.sellToken);
        buyToken_TV = _offer.findViewById(R.id.buyToken);
    }

    public void LoadTokenData(Web3j web3, Credentials credentials, Loyalty contract){
        try {
            Company sell_company = new Company( contract.companies(sellTokenCompany).send());
            Company buy_company = new Company( contract.companies(wantedTokenCompany).send());
            buyToken = Pay_bonuses.getToken(web3,credentials,buy_company.token);
            sellToken = Pay_bonuses.getToken(web3,credentials,sell_company.token);

        }catch (Exception e){
            e.printStackTrace();
        }
        DisplayData();
    }

    @SuppressLint("SetTextI18n")
    public void DisplayData(){
        offerId_TV.setText(offerId.toString());
        offer_seller.setText(sellerAddress);
        sellToken_TV.setText(sellToken.name + "   ("+sellAmount.toString()+")");
        buyToken_TV.setText(buyToken.name+ "   ("+buyAmount.toString()+")");
    }




}
