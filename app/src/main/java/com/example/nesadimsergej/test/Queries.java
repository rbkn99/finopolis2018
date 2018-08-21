package com.example.nesadimsergej.test;

import android.app.Activity;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.web3j.abi.datatypes.Array;
import org.web3j.tuples.generated.Tuple2;

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
        for(Integer i = 0;i<20;i++)
            AddQueri(i.toString(),"ULTRANASILIE");

        Timer timer = new Timer();
        timer.schedule(new QueryUpdater(), 0, 30000);// Обновлять запросы каждые 30 секунд
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
        // Запрос к Рыбкину
        // ....
        // Вот что пришло
        ArrayList<Tuple2<String,String>> resultQueries = new ArrayList<>();
        resultQueries.add(new Tuple2<>(QueriGenerator(), "SUPER SECRET CODE"));


        for (Tuple2<String,String> t: resultQueries
             ) {
            String queriText = t.getValue1();
            String secreteCode = t.getValue2();
            AddQueri(queriText,secreteCode);
        }
    }

    void OnQueriAccepted(Queri q){
        Toast.makeText(page.getContext(), q.getText()+" +",
                        Toast.LENGTH_SHORT).show();


        q.Destroy();
    }
    void OnQueriDeclined(Queri q){
        Toast.makeText(page.getContext(), q.getText()+" -",
                        Toast.LENGTH_SHORT).show();


        q.Destroy();
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
