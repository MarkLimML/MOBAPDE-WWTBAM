package com.example.wwtbam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class gameover extends AppCompatActivity {
    static bgmusic_controller bgm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //hide the title /the top part

        bgm=new bgmusic_controller(this.getApplicationContext());

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


    //probably won't work, tried  to use "recreate()" but can not implement it
    public void start(View view) {
        bgm.stopbg();
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();

    }
//according to net not advisable daw ung mag "exit" ung app, dapat ung os mismo kasi pagka
// app lng di siya nag frefree ng resources sa phone based sa understanding ko
}
