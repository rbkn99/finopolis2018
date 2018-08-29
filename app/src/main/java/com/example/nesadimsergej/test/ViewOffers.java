package com.example.nesadimsergej.test;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple6;

import java.math.BigInteger;


public class ViewOffers extends SceneController {

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
        update.setOnClickListener(v -> UpdateOffers());

        offerList = page.findViewById(R.id.offerList);
        UpdateOffers();
    }
    void UpdateOffers(){
        update.setEnabled(false);
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

                ((Office)page.getContext()).runOnUiThread(() -> {
                    update.setEnabled(true);
                });


            }

            for(BigInteger i = BigInteger.ZERO ; i.compareTo(stockSize) == -1 ; i = i.add( BigInteger.ONE)) {
                try {

                    Tuple6<BigInteger, String, String, String, BigInteger, BigInteger> offer = contract.getOfferFromStock(i).send();
                    AddOffer(offer,web3,credentials,contract);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            ((Office)page.getContext()).runOnUiThread(() -> {
                update.setEnabled(true);
            });
        };
        Thread thread = new Thread(bonusUpdater);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();



    }


    void AddOffer(Tuple6<BigInteger,String,String,String,BigInteger,BigInteger> _data,Web3j web3,
                  Credentials credentials,Loyalty contract){

        View view = View.inflate(page.getContext(),R.layout.offer,null);

        Offer offer = new Offer(view, _data, offer1 -> OnOffer(offer1), offer12 -> OnDecline(offer12),offerList);
        offer.LoadTokenData(web3,credentials,contract);
        ((Office)page.getContext()).runOnUiThread(() -> {
            offerList.addView(view);
            if(credentials.getAddress().equals(offer.sellerAddress)){
                offer.Decline();
            }else{
                offer.Accept();
            }

        });

    }

    void OnOffer(Offer offer){
        Runnable bonusUpdater = () -> _OnOffer(offer);
        Thread thread = new Thread(bonusUpdater);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

    }
    void _OnOffer(Offer offer){
        Credentials bankCredentials = Credentials.create(Config.bankPrivateKey,Config.bankPublicKey);
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        Loyalty bankContract = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        String bonusAddress = offer.buyToken.tokenAddress;
        Token tokenContract = Token.load(bonusAddress,web3,credentials,Token.GAS_PRICE,Token.GAS_LIMIT);

        try {

            BigInteger balance = tokenContract.balanceOf(credentials.getAddress()).send().divide(Config.tene18);
            System.out.println(balance);
            System.out.println(offer.buyAmount);
            if(balance.compareTo(offer.buyAmount) == -1){
                ((Office)page.getContext()).runOnUiThread(() ->{
                    Toast.makeText(page.getContext(),"Недостаточно средств для обмена!",Toast.LENGTH_SHORT).show();
                });
                return;
            }

        }catch (Exception e){

        }
        try {
            ((Office)page.getContext()).runOnUiThread(() ->{
                Toast.makeText(page.getContext(),"Запрос отправлен!",Toast.LENGTH_SHORT).show();

            });
            bankContract.acceptOffer(offer.offerId, credentials.getAddress()).send();
            ((Office)page.getContext()).runOnUiThread(() ->{
                Toast.makeText(page.getContext(),"Обмен прошел успешно!",Toast.LENGTH_SHORT).show();
                offer.Destroy();
            });
        }catch (Exception e){
            ((Office)page.getContext()).runOnUiThread(() ->{
                Toast.makeText(page.getContext(),"Неизвестная ошибка, попробуйте обновить страницу!",Toast.LENGTH_SHORT).show();
            });
            e.printStackTrace();
        }
    }
    void OnDecline(Offer offer) {
        Runnable bonusUpdater = () -> _OnDecline(offer);
        Thread thread = new Thread(bonusUpdater);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

    }
    void _OnDecline(Offer offer){
        Credentials bankCredentials = Credentials.create(Config.bankPrivateKey,Config.bankPublicKey);
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;
        Loyalty bankContract = Loyalty.load(Config.contractAdress,web3,bankCredentials,Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);
        try {
            ((Office)page.getContext()).runOnUiThread(() ->{
                Toast.makeText(page.getContext(),"Запрос отправлен!",Toast.LENGTH_SHORT).show();
                offer.Destroy();
            });
            bankContract.recallOffer(offer.offerId,credentials.getAddress()).send();
            ((Office)page.getContext()).runOnUiThread(() ->{
                Toast.makeText(page.getContext(),"Предложение успешно отозвано!",Toast.LENGTH_SHORT).show();
                offer.Destroy();
            });
        }catch (Exception e){
            e.printStackTrace();
            ((Office)page.getContext()).runOnUiThread(() ->{
                Toast.makeText(page.getContext(),"Неизвестная ошибка, попробуйте обновить страницу!",Toast.LENGTH_SHORT).show();
            });
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
    LinearLayout parent;
    TokenWrapper sellToken = null;
    TokenWrapper buyToken = null;

    TextView offerId_TV,offer_seller,sellToken_TV, buyToken_TV;

    ViewOffers.OfferCallback onOffer;

    Button acceptOffer,declineOffer;
    public void Destroy(){
        System.out.println(offerView.getParent());
        parent.removeView(offerView);
    }


    private Tuple6<BigInteger,String,String,String,BigInteger,BigInteger> data;
    public Offer(View _offer, Tuple6<BigInteger,String,String,String,BigInteger,BigInteger> _data,ViewOffers.OfferCallback _onOffer,
                 ViewOffers.OfferCallback _onDecline, LinearLayout _parent){
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
        parent = _parent;

        offerId_TV = _offer.findViewById(R.id.offerId);
        offer_seller = _offer.findViewById(R.id.offer_seller);
        sellToken_TV = _offer.findViewById(R.id.sellToken);
        buyToken_TV = _offer.findViewById(R.id.buyToken);
        acceptOffer = _offer.findViewById(R.id.acceptOffer);
        declineOffer = _offer.findViewById(R.id.declineOffer);
        acceptOffer.setOnClickListener(v -> _onOffer.func(_this));
        declineOffer.setOnClickListener(v -> _onDecline.func(_this));
    }
    void Decline(){
        acceptOffer.setVisibility(View.GONE);
        declineOffer.setVisibility(View.VISIBLE);
    }

    void Accept(){
        acceptOffer.setVisibility(View.VISIBLE);
        declineOffer.setVisibility(View.GONE);
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
