package com.example.videotest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    VideoView videoView;
    ArrayList<File> videos;
    int video;

    final int REQUEST_ACCESS_FINE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView=(VideoView)findViewById(R.id.videoView);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_ACCESS_FINE);
        }else {
            pantallaFull();
            playVideo();
            reproducir();
        }

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    pantallaFull();
                    ++video;
                    if (video > videos.size() - 1) {
                        video = 0;
                    }
                    String x = videos.get(video).toString();
                    videoView.setVideoPath(x);
                    videoView.start();
                    Toast.makeText(MainActivity.this, "fin " + videos.get(video), Toast.LENGTH_SHORT).show();
                }
            });

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

    public void showAlertDialogButtonClicked(){

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Memoria Vacía");
        builder.setMessage("No se encontraron archivos mp4.");

        // add a button
        builder.setPositiveButton("SALIR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do something like...
                finish();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void reproducir(){
        videoView.setMediaController(new MediaController(this));
        if(videos.size()==0){
            showAlertDialogButtonClicked();
            Toast.makeText(this, "No se encontraron videos mp4", Toast.LENGTH_SHORT).show();
        }else{
            String x=videos.get(0).toString();
            videoView.setVideoPath(x);
            videoView.start();
        }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_ACCESS_FINE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permitir", Toast.LENGTH_SHORT).show();
                pantallaFull();
                playVideo();
                //Toast.makeText(this, videos.get(0).toString(), Toast.LENGTH_SHORT).show();
                //INICIA EL REPRODUCTOR
                reproducir();
        }else {
                Toast.makeText(this, "Cerrado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void playVideo(){
        videos = readSongs(Environment.getExternalStorageDirectory());
    }
}
