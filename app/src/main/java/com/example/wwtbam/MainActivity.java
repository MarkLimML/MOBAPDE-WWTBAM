package com.example.wwtbam;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    static bgmusic_controller bgm;
    Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //hide the title /the top part

        bgm=new bgmusic_controller(this.getApplicationContext());

        start = findViewById(R.id.start);

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

    public void start(View view) {
        start.setEnabled(false);
        bgm.stopbg();
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        start.setEnabled(true);
        //finish();
    }




}
