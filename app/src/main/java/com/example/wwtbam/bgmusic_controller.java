package com.example.wwtbam;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.Button;

public class bgmusic_controller {
    Context context;
    MediaPlayer mediaplayer;

    bgmusic_controller(Context context){
        this.context=context;
        mediaplayer= MediaPlayer.create(context,R.raw.wwtbambg);
    }

    public void playbg(){
      mediaplayer.start();
    }

    public void pausebg(){
        mediaplayer.pause();
    }

    public void stopbg(){
        mediaplayer.stop();
    }

    public boolean isPlay(){
        if(mediaplayer.isPlaying()){
            return true;
        }
        else{
            return false;
        }
    }



}
