package com.example.wwtbam;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class GameActivity extends AppCompatActivity {

    TextView textview_question,textView_timer;
    ProgressBar progressBar;
    Button buttonA,buttonB,buttonC,buttonD;

    JSONArray jsonArray;
    JSONArray choices;
    JSONObject obj;

    int questionNum=0,correctAns;
    int progressValue=0;

    static bgmusic_controller bgm;

    public Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();

        bgm=new bgmusic_controller(this.getApplicationContext());

        buttonA = findViewById(R.id.answerA);
        buttonB = findViewById(R.id.answerB);
        buttonC = findViewById(R.id.answerC);
        buttonD = findViewById(R.id.answerD);

        textview_question = findViewById(R.id.textview_question);
        textView_timer = findViewById(R.id.textView_timer);
        textView_timer.setText(String.valueOf(progressValue));

        progressBar = findViewById(R.id.progressBar);

        progressBar.setMax(30);
        setProgressValue();
        getJson();
        showQuestionAndChoices();

        final Button volume = findViewById(R.id.volume);

        bgm.playbg();
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bgm.isPlay()==true) {
                    bgm.pausebg();
                    volume.setBackgroundResource(R.drawable.volumex);
                } else if(bgm.isPlay()==false) {
                    bgm.playbg();
                    volume.setBackgroundResource(R.drawable.volume);
                }



            }
        });
    }

    public void setProgressValue() {

        // set the progress
        progressBar.setProgress(progressValue);
        textView_timer.setText(String.valueOf(progressValue));
        // thread is used to change the progress value
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000);
//                    progressValue++;
//
//                    if(progressValue == progressBar.getMax()){
//                        questionNum++;
//                        showQuestionAndChoices();
//                        progressValue=0;
//                    }
//
//                  //textView_timer.setText(String.valueOf(progressValue));
//                setProgressValue();
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//        thread.start();

        new Thread(new Runnable() {
            public void run() {
                while (progressValue <= 30) {
                    progressValue += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressValue);
                            textView_timer.setText(progressValue+"/"+progressBar.getMax());
                        }
                    });
                    if(progressValue == 29){
                        //Toast.makeText(getApplicationContext(),"Times Up!!!",Toast.LENGTH_SHORT).show();
                        questionNum++;
                        showQuestionAndChoices();
                        progressValue = 0;
                    }
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    public void getJson(){

        String json;

        try {
            InputStream inputStream = getAssets().open("questions.json");

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer,"UTF-8");

            JSONObject jOb = new JSONObject(json);

            jsonArray = jOb.getJSONArray("questions");;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showQuestionAndChoices(){
        try {
            obj = jsonArray.getJSONObject(questionNum);

            choices = obj.getJSONArray("answerChoices");

            textview_question.setText(obj.getString("question"));
            correctAns = Integer.valueOf(obj.getString("correctAnswerIndex"))+1;

            buttonA.setText(choices.get(0).toString());
            buttonB.setText(choices.get(1).toString());
            buttonC.setText(choices.get(2).toString());
            buttonD.setText(choices.get(3).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void checkA(View v){
        checkAnswer(1);
    }

    public void checkB(View v){
        checkAnswer(2);
    }

    public void checkC(View v){
        checkAnswer(3);
    }

    public void checkD(View v){
        checkAnswer(4);
    }

    public void checkAnswer(int choiceNum){
        if(choiceNum == correctAns){
            Toast.makeText(getApplicationContext(),"You got the Correct Answer 🙂", Toast.LENGTH_SHORT).show();
            questionNum++;
            if(questionNum == 3){
                questionNum = 0;
            }
            progressValue = 0;
            showQuestionAndChoices();
        }
        else{
            Toast.makeText(getApplicationContext(),"Sadly, You got the Wrong Answer 😞",Toast.LENGTH_SHORT).show();
            //game over na ba
        }
    }

    public void lifeFifty(View v){
        v=findViewById(R.id.fifty);
        v.setVisibility(View.GONE);
    }

    public void lifePeople(View v){
        v=findViewById(R.id.people);
        v.setVisibility(View.GONE);
    }

    public void lifeSwap(View v){
        v=findViewById(R.id.swap);
        v.setVisibility(View.GONE);
    }
}