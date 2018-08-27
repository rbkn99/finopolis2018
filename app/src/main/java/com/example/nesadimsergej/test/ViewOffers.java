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
    Button update;

    public ViewOffers(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene() {
        super.SetUpScene();

        back = page.findViewById(R.id.back);
        update = page.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateOffers();
            }
        });

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
        offerList.removeAllViews();
        Runnable bonusUpdater = () -> {
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
        };
        Thread thread = new Thread(bonusUpdater);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();



    }


    void AddOffer(Tuple6<BigInteger,String,String,String,BigInteger,BigInteger> _data,Web3j web3,
                  Credentials credentials,Loyalty contract){

        View view = View.inflate(page.getContext(),R.layout.offer,null);
        ((Office)page.getContext()).runOnUiThread(() -> offerList.addView(view));
        Offer offer = new Offer(view, _data, new OfferCallback() {
            @Override
            public void func(Offer offer) {
                OnOffer(offer);
            }
        });
        offer.LoadTokenData(web3,credentials,contract);

    }

    void OnOffer(Offer offer){
        Credentials bankCredentials = Credentials.create(Config.bankPrivateKey,Config.bankPublicKey);
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        Loyalty bankContract = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        System.out.println(offer.offerId);

        try {
            bankContract.acceptOffer(offer.offerId, credentials.getAddress()).send();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    interface OfferCallback {

        void func(Offer offer);
    }
}




class Offer{

    Offer _this;

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

    ViewOffers.OfferCallback onOffer;

    Button acceptOffer;
    public void Destroy(){
        ((ViewGroup)offerView.getParent()).removeView(offerView);
    }


    private Tuple6<BigInteger,String,String,String,BigInteger,BigInteger> data;
    public Offer(View _offer, Tuple6<BigInteger,String,String,String,BigInteger,BigInteger> _data,ViewOffers.OfferCallback _onOffer){
        _this = this;
        offerView = _offer;
        offerId = _data.getValue1();
        sellerAddress = _data.getValue2();
        sellTokenCompany = _data.getValue3();
        wantedTokenCompany = _data.getValue4();
        sellAmount = _data.getValue5().divide(Config.tene18);
        buyAmount = _data.getValue6().divide(Config.tene18);
        onOffer = _onOffer;
        data = _data;


        offerId_TV = _offer.findViewById(R.id.offerId);
        offer_seller = _offer.findViewById(R.id.offer_seller);
        sellToken_TV = _offer.findViewById(R.id.sellToken);
        buyToken_TV = _offer.findViewById(R.id.buyToken);
        acceptOffer = _offer.findViewById(R.id.acceptOffer);
        acceptOffer.setOnClickListener(v -> _onOffer.func(_this));
    }

    public void LoadTokenData(Web3j web3, Credentials credentials, Loyalty contract){
        try {
            Company sell_company = Utils.getCompany(web3,credentials,sellTokenCompany); //new Company( contract.companies(sellTokenCompany).send());
            Company buy_company = Utils.getCompany(web3,credentials,wantedTokenCompany); //new Company( contract.companies(wantedTokenCompany).send());
            buyToken = Utils.getToken(web3,credentials,buy_company.token);
            sellToken = Utils.getToken(web3,credentials,sell_company.token);

        }catch (Exception e){
            e.printStackTrace();
        }
        DisplayData();
    }

    @SuppressLint("SetTextI18n")
    public void DisplayData(){
        ((Office)offerView.getContext()).runOnUiThread(() ->{
            offerId_TV.setText(offerId.toString());
            offer_seller.setText(sellerAddress);
            sellToken_TV.setText(sellToken.name + "   ("+sellAmount.toString()+")");
            buyToken_TV.setText(buyToken.name+ "   ("+buyAmount.toString()+")");

        });
    }


}
