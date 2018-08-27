package com.example.nesadimsergej.test;

import android.app.Activity;
import android.os.Debug;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.abi.datatypes.Array;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple10;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tuples.generated.Tuple8;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Queries extends SceneController {


    ConstraintLayout exampleQueri;
    LinearLayout queriList;

    public Queries(View _page){
        super();
        page = _page;

        SetUpScene();
    }

    @Override
    void SetUpScene() {
        super.SetUpScene();
        exampleQueri = page.findViewById(R.id.queri);
        queriList = page.findViewById(R.id.queriList);
        //for(Integer i = 0;i<20;i++)
        //    AddQueri(i.toString(),"ULTRANASILIE");

        Timer timer = new Timer();
        timer.schedule(new QueryUpdater(), 0, 30000);// Обновлять запросы каждые 30 секунд
        ((Office)page.getContext()).timers.add(timer);
        //page.getContext().
    }


    void AddQueri(String text, String secreteCode){
        View view = View.inflate(page.getContext(),R.layout.queri,null);
        ((TextView)view.findViewById(R.id.QueriText)).setText(text);
        queriList.addView(view);

        Queri q = new Queri(view,secreteCode,
                qu -> OnQueriAccepted(qu),
                qu -> OnQueriDeclined(qu)
        );
    }


    void UpdateQueries(){
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                credentials,
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);


        BigInteger requestCount = BigInteger.ZERO;
        try {
            Company s =Utils.getCompany(web3,credentials,credentials.getAddress());//  contract.companies(credentials.getAddress()).send());
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


}


interface QueriCallback {

    void func(Queri qu);
}

class Queri{

    QueriCallback accept;
    QueriCallback decline;
    String secreteCode;

    View queri;
    TextView queriText;
    Button yesBtn,noBtn;

    public void Destroy(){
        ((ViewGroup)queri.getParent()).removeView(queri);
    }

    public String getText(){
        return queriText.getText().toString();
    }

    public void setText(String text){
        queriText.setText(text);
    }



    public Queri(View _queri,
                String _secreteCode,
                 QueriCallback _accept,
                 QueriCallback _decline){
        secreteCode = _secreteCode;
        queri = _queri;
        accept = _accept;
        decline = _decline;

        queriText = queri.findViewById(R.id.QueriText);
        yesBtn = queri.findViewById(R.id.yesBtn);
        noBtn = queri.findViewById(R.id.noBtn);
        yesBtn.setOnClickListener(v -> OnClickYes());
        noBtn.setOnClickListener(v -> OnClickNo());

    }

    void OnClickYes(){
        //Toast.makeText(queri.getContext(), queriText.getText(),
        //        Toast.LENGTH_SHORT).show();
        // Accept queri
        accept.func(this);
    }
    void OnClickNo(){
        //Toast.makeText(queri.getContext(), "No",
        //        Toast.LENGTH_SHORT).show();
        // Decline queri
        decline.func(this);
    }

}
