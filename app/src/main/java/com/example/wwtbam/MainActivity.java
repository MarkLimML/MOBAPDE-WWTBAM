package com.example.wwtbam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide(); //hide the title /the top part
    }

    public void start(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();

    }
}
