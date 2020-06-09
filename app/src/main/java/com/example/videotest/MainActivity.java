package com.example.videotest;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    VideoView videoView;
    ArrayList<File> songs;
    int video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pantallaFull();
        videoView=(VideoView)findViewById(R.id.videoView);

        songs=readSongs(Environment.getExternalStorageDirectory());
        Toast.makeText(this, songs.get(0).toString(), Toast.LENGTH_SHORT).show();
        //INICIA EL REPRODUCTOR
            reproducir();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    pantallaFull();
                    ++video;
                    if(video>songs.size()-1){
                        video=0;
                    }
                    String x=songs.get(video).toString();
                    videoView.setVideoPath(x);
                    videoView.start();
                    Toast.makeText(MainActivity.this, "termino "+songs.get(video), Toast.LENGTH_SHORT).show();
                }
            });
            //Toast.makeText(this, songs.get(j).toString(),Toast.LENGTH_SHORT).show();
    }
    ArrayList<File> readSongs(File root){
        ArrayList<File> arrayList = new ArrayList<File>();
        File files[]=root.listFiles();
        for(File file: files){
            if(file.isDirectory()) {
                arrayList.addAll(readSongs(file));
            }else{
                if(file.getName().endsWith(".mp4")){
                    arrayList.add(file);
                }
            }
        }
        return arrayList;
    }

    public void reproducir(){
        videoView.setMediaController(new MediaController(this));
        String x=songs.get(0).toString();
        videoView.setVideoPath(x);
        videoView.start();
    }
    public void pantallaFull(){
        View decorView =getWindow().getDecorView();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }
}