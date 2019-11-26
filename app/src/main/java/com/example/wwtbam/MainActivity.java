package com.example.wwtbam;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    final MediaPlayer mediaplayer = MediaPlayer.create(this, R.raw.wwtbambg );
    final Button volume = findViewById(R.id.volume);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //hide the title /the top part


        mediaplayer.start();
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaplayer.isPlaying()) {
                    mediaplayer.pause();
                    volume.setBackgroundResource(R.drawable.volumex);
                } else {
                    mediaplayer.start();
                    volume.setBackgroundResource(R.drawable.volume);
                }

            }
        });

    }

    public void start(View view) {
        mediaplayer.stop();
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();

    }
}
