package com.example.wwtbam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class GameActivity extends AppCompatActivity {

    TextView textview_question;
    Button buttonA,buttonB,buttonC,buttonD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();

        buttonA = findViewById(R.id.answerA);
        buttonB = findViewById(R.id.answerB);
        buttonC = findViewById(R.id.answerC);
        buttonD = findViewById(R.id.answerD);

        textview_question = findViewById(R.id.textview_question);

        getJson();

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

            JSONArray jsonArray = jOb.getJSONArray("questions");;
            JSONObject obj = jsonArray.getJSONObject(0);
            System.out.println(obj);

            JSONArray choices = obj.getJSONArray("answerChoices");
            System.out.println(choices);
            System.out.println("Question#1: "+ obj.getString("question"));
            textview_question.setText(obj.getString("question"));
            System.out.println("Choices: "+ obj.getString("answerChoices").indexOf(1));
            buttonA.setText(choices.get(1).toString());
            buttonB.setText(choices.get(2).toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
