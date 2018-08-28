package com.example.nesadimsergej.test;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tuples.generated.Tuple2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class Queries extends SceneController {


    ConstraintLayout exampleQueri;
    LinearLayout queriList;
    TextView ifEmpty;

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
        ifEmpty = page.findViewById(R.id.ifEmpty);
        //page.getContext().
    }


    void AddQueri(Web3j web3,Credentials credentials,String senderAddress, String secreteCode){

        String companyName = Utils.getLastRequestedCompany(web3,credentials,senderAddress).companyName;
        String coalitionName = Utils.getCoalition(web3,credentials,senderAddress).name;

        ((Office)page.getContext()).runOnUiThread(() -> {
            View view = View.inflate(page.getContext(),R.layout.queri,null);
            String queryText ="";
            queryText = queryText + companyName;
            queryText = queryText +" приглашает вас вступить в коалицию ";
            queryText = queryText + coalitionName;

            ((TextView)view.findViewById(R.id.answer_text)).setText(queryText);
            queriList.addView(view);
            Queri q = new Queri(view,secreteCode,
                    qu -> OnQueriAccepted(qu),
                    qu -> OnQueriDeclined(qu)
            );
        });
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
        }catch (Exception e){

        }

        ArrayList<Tuple2<String,String>> resultQueries = new ArrayList<>();
        int added = 0;
        for(BigInteger i = BigInteger.ZERO ; i.compareTo(requestCount) == -1 ; i = i.add( BigInteger.ONE)) {
            try {

                String s = contract.getRequestOnIndex(i).sendAsync().get();
                resultQueries.add(new Tuple2<>(s,s));
                //System.out.println(s);
                added ++;
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        ((Office)page.getContext()).runOnUiThread(() ->queriList.removeAllViews());

        for (Tuple2<String,String> t: resultQueries
             ) {
            String queriText = t.getValue1();
            String secreteCode = t.getValue2();
            AddQueri(web3,credentials,queriText,secreteCode);
        }
        int _added = added;
        ((Office)page.getContext()).runOnUiThread(() ->{
            if(_added == 0){
                ifEmpty.setVisibility(View.VISIBLE);
            }else{
                ifEmpty.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void AnswerRequest(Queri q, boolean answer){

        String message;
        if(answer){
            message = "Приглашение принято";
        }else {
            message = "Приглашение отклонено";
        }

        String requestAddress = q.secreteCode;
        Credentials credentials = ((Office)page.getContext()).credentials;
        Web3j web3 = ((Office)page.getContext()).web3;

        Loyalty contract = Loyalty.load(Config.contractAdress,web3,
                credentials,
                Loyalty.GAS_PRICE,Loyalty.GAS_LIMIT);

        try {
            ((Office)page.getContext()).runOnUiThread(() -> {
                Toast.makeText(page.getContext(), "Запрос успешно отправлен",
                        Toast.LENGTH_SHORT).show();
            });

            contract.respond(requestAddress, answer).send();
            ((Office)page.getContext()).runOnUiThread(() -> {
                Toast.makeText(page.getContext(), message,
                        Toast.LENGTH_SHORT).show();
                q.Destroy();
            });


        }catch (Exception e) {
            ((Office)page.getContext()).runOnUiThread(() -> {
                Toast.makeText(page.getContext(), "Во время обработки запроса произошла непредвиденная ошибка",
                        Toast.LENGTH_SHORT).show();
            });
            e.printStackTrace();
        }
    }

    void OnQueriAccepted(Queri q){


        Runnable bonusUpdater = () -> AnswerRequest(q,true);;
        Thread thread = new Thread(bonusUpdater);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();


    }
    void OnQueriDeclined(Queri q){

        Runnable bonusUpdater = () -> AnswerRequest(q,false);;
        Thread thread = new Thread(bonusUpdater);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();

    }


    class QueryUpdater extends TimerTask {
        @Override
        public void run() {
            ((Activity)page.getContext()).runOnUiThread(new Runnable() {
                public void run() {

                    Runnable bonusUpdater = () -> UpdateQueries();;
                    Thread thread = new Thread(bonusUpdater);
                    thread.setPriority(Thread.MIN_PRIORITY);
                    thread.start();

                }
            });
        }
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

        queriText = queri.findViewById(R.id.answer_text);
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
