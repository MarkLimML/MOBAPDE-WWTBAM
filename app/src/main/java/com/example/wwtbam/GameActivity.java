package com.example.wwtbam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    TextView textview_question,textView_timer,money;
    ProgressBar progressBar;
    Button buttonA,buttonB,buttonC,buttonD,fifty,people,swap;

    JSONArray jsonArray;
    JSONArray choices;
    JSONObject obj;

    int questionNum=0,correctAns;
    int progressValue=0;
    int hidChoiceNum, firstHidNum,secondHidNum;
    int fnum=0, snum=0, tnum=0,lnum=0;
    int flimit,slimit;

    int[] scores = {0, 100, 200, 300, 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000, 500000, 1000000};

    static bgmusic_controller bgm;

    Handler handler = new Handler();

    Random random = new Random();

    boolean hidflag;

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

        fifty = findViewById(R.id.fifty);
        people = findViewById(R.id.fifty);
        swap = findViewById(R.id.fifty);

        textview_question = findViewById(R.id.textview_question);
        textView_timer = findViewById(R.id.textView_timer);
        textView_timer.setText(String.valueOf(progressValue));

        money = findViewById(R.id.money);
        money.setText("$"+scores[0]);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setMax(30);
        //setProgressValue();
        progressValue = 0;
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
                    if(progressValue == 30){
                        showToast("Times Up !!!");
                        questionNum++;

                        if(questionNum == 9){
                            questionNum = 0;
                        }

                        showQuestionAndChoices();
                        progressValue = 0;
                    }
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(500);
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
            InputStream inputStream = getAssets().open("questions1.json");

            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer,"UTF-8");

            JSONObject jOb = new JSONObject(json);

            System.out.println("Loading");
            jsonArray = jOb.getJSONArray("questions");
            jsonArray = shuffle(jsonArray);
            System.out.println("Questions Loaded");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static JSONArray shuffle (JSONArray array) throws JSONException {
        Random rnd = new Random();
        for (int i = array.length() - 1; i > 0; i--)
        {
            int j = rnd.nextInt(i + 1);
            //JSONObject jo = array.getJSONObject(i);
            JSONObject jo2 = array.getJSONObject(j);
            //Boolean same = jo.equals(jo2);


            if (i > 19) {
                if(j > 19 && jo2.getInt("difficulty") == 3) {
                    Object object = array.get(j);
                    array.put(j, array.get(i));
                    array.put(i, object);
                }
            } else if (i < 20 && i > 9) {
                if((j < 20 && j > 9) && jo2.getInt("difficulty") == 2) {
                    Object object = array.get(j);
                    array.put(j, array.get(i));
                    array.put(i, object);
                }
            } else if (i < 10 && i > -1) {
                if((j < 10 && j > -1) && jo2.getInt("difficulty") == 1) {
                    Object object = array.get(j);
                    array.put(j, array.get(i));
                    array.put(i, object);
                }
            } else {
                i++;
            }
            System.out.println(i+" "+j);
        }
        return array;
    }

    public void showQuestionAndChoices(){
        Random rnd = new Random();
        setVisibleBtn();
        int x = 4;
        try {
            do {
                questionNum = rnd.nextInt(10);
                if (progressValue < 5)
                    questionNum += 0;
                else if (progressValue < 10)
                    questionNum += 10;
                else if (progressValue < 15)
                    questionNum += 20;

                obj = jsonArray.getJSONObject(questionNum);
                x = obj.getInt("difficulty");
            } while (x == 4);

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
        int fscore = 0;
        if(choiceNum == correctAns){
            try {
                jsonArray.getJSONObject(questionNum).put("difficulty", 4);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),"You got the Correct Answer 🙂", Toast.LENGTH_SHORT).show();
            questionNum++;

            progressValue++;

            money.setText("$"+scores[progressValue]);
            if(progressValue == 15) {
                fscore = scores[15];
                Intent intent = new Intent(this, GameOver.class);
                intent.putExtra("score", String.valueOf(fscore));
                startActivityForResult(intent, 0);
            }
            if(progressValue < 15)
                showQuestionAndChoices();
        }
        else{
            bgm.stopbg();
            if(progressValue < 5)
                fscore = scores[0];
            else if(progressValue < 10)
                fscore = scores[5];
            else if(progressValue < 15)
                fscore = scores[10];
            Toast.makeText(getApplicationContext(),"Sadly, You got the Wrong Answer 😞",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, GameOver.class);
            intent.putExtra("score", String.valueOf(fscore));
            startActivityForResult(intent, 0);
            //game over na ba
        }
    }

    public void lifeFifty(View v){
        v=findViewById(R.id.fifty);
        v.setVisibility(View.GONE);

            firstHidNum = random.nextInt(4);
            secondHidNum = random.nextInt(4);

            while(firstHidNum == correctAns || firstHidNum == 0){
                firstHidNum = random.nextInt(4);
            }

            while(secondHidNum == correctAns || secondHidNum == firstHidNum || secondHidNum == 0){
                secondHidNum = random.nextInt(4);
            }

            //showToast("First:"+firstHidNum+"second"+secondHidNum+ "corrcetAns:"+ correctAns);

                if(firstHidNum == 1){
                    buttonA.setVisibility(View.INVISIBLE);
                }
                else if(firstHidNum == 2){
                    buttonB.setVisibility(View.INVISIBLE);
                }
                else if(firstHidNum == 3){
                    buttonC.setVisibility(View.INVISIBLE);
                }
                else if(firstHidNum == 4){
                    buttonD.setVisibility(View.INVISIBLE);
                }

                if(secondHidNum == 1){
                    buttonA.setVisibility(View.INVISIBLE);
                }
                else if(secondHidNum == 2){
                    buttonB.setVisibility(View.INVISIBLE);
                }
                else if(secondHidNum == 3){
                    buttonC.setVisibility(View.INVISIBLE);
                }
                else if(secondHidNum == 4){
                    buttonD.setVisibility(View.INVISIBLE);
                }

    }

    public void lifePeople(View v){
        v=findViewById(R.id.people);
        v.setVisibility(View.GONE);


        while(fnum < 50){
            fnum = random.nextInt(100);
        }
        flimit = 100 - fnum;

        while(snum == 0 || snum == 49){
            snum = random.nextInt(flimit);
        }
        slimit = flimit - snum;

        while(tnum == 0){
            tnum = random.nextInt(slimit);
        }

        lnum = slimit - tnum;

        if(correctAns == 1){
            buttonA.setText(buttonA.getText()+ "  "+ fnum +"%");
            buttonB.setText(buttonB.getText()+ "  "+ lnum +"%");
            buttonC.setText(buttonC.getText()+ "  "+ snum +"%");
            buttonD.setText(buttonD.getText()+ "  "+ tnum +"%");
        }
        else if(correctAns == 2){
            buttonA.setText(buttonA.getText()+ "  "+ lnum +"%");
            buttonB.setText(buttonB.getText()+ "  "+ fnum +"%");
            buttonC.setText(buttonC.getText()+ "  "+ snum +"%");
            buttonD.setText(buttonD.getText()+ "  "+ tnum +"%");
        }
        else if(correctAns == 3){
            buttonA.setText(buttonA.getText()+ "  "+ lnum +"%");
            buttonB.setText(buttonB.getText()+ "  "+ snum +"%");
            buttonC.setText(buttonC.getText()+ "  "+ fnum +"%");
            buttonD.setText(buttonD.getText()+ "  "+ tnum +"%");
        }
        else if(correctAns == 4){
            buttonA.setText(buttonA.getText()+ "  "+ lnum +"%");
            buttonB.setText(buttonB.getText()+ "  "+ snum +"%");
            buttonC.setText(buttonC.getText()+ "  "+ tnum +"%");
            buttonD.setText(buttonD.getText()+ "  "+ fnum +"%");
        }

        fnum = 0;
        snum = 0;
        tnum = 0;
        lnum = 0;
    }

    public void lifeSwap(View v){
        v=findViewById(R.id.swap);
        v.setVisibility(View.GONE);
        questionNum++;
        showQuestionAndChoices();
    }

    //will work during thread
    public void showToast(final String toast)
    {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //will work during thread
    public void setVisibleBtn(){
        runOnUiThread(new Runnable() {
            public void run() {
                buttonA.setVisibility(View.VISIBLE);
                buttonB.setVisibility(View.VISIBLE);
                buttonC.setVisibility(View.VISIBLE);
                buttonD.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}