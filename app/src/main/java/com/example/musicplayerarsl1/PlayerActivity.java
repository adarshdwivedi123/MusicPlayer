package com.example.musicplayerarsl1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btn_next,btn_previous,btn_pause;
    TextView songTextLabel;
    SeekBar songSeekbar;
    static MediaPlayer myMediaPlayer;
    int position;
    ArrayList<File> mySongs;
    Thread updateseekBar;
    String sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btn_next=(Button)findViewById(R.id.next);
        btn_previous=(Button)findViewById(R.id.previous);
        btn_pause=(Button)findViewById(R.id.pause);
        songTextLabel=(TextView)findViewById(R.id.songLabel);
        songSeekbar=(SeekBar)findViewById(R.id.seekBar);
        getSupportActionBar().setTitle("Now Playing"); //here we want to set the now palying
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



   //so firstly here we want to update the seek bar so here we want to use of thread
        updateseekBar=new Thread(){
            @Override
            public void run() {
                int totalDuration=myMediaPlayer.getDuration();
                int currentPosition=0;
                while(currentPosition<totalDuration){
                    try{
                        sleep(500);
                        currentPosition=myMediaPlayer.getCurrentPosition();
                        songSeekbar.setProgress(currentPosition);



                    }
                    catch (InterruptedException e){
                        e.printStackTrace();

                    }
                }

            }
        };
        if(myMediaPlayer!=null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }


        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mySongs=(ArrayList)bundle.getParcelableArrayList( "songs");  //yha pe key and value recev ki jaye gi
        sname=mySongs.get(position).getName().toString();  //item pe jb click krvu ga to uska kuch position
        String songName=i.getStringExtra("songname");
        songTextLabel.setText(songName);  //textlabel ko set
        songTextLabel.setSelected(true);
        position=bundle.getInt(" pos",0);
        Uri u=Uri.parse(mySongs.get(position).toString());
        myMediaPlayer=myMediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        songSeekbar.setMax(myMediaPlayer.getDuration());

        updateseekBar.start();
        //here we want to change the color of Seekbaar
        songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);


        //here we apply listener  on progressbar
        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                    myMediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekbar.setMax(myMediaPlayer.getDuration()); //postion set
                if(myMediaPlayer.isPlaying())
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();
                }
                else
                {
                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                }
            }
        });

                btn_next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         myMediaPlayer.stop();
                         myMediaPlayer.release();
                         position=((position+1)%mySongs.size());  //next song me jaye ge to postion plus one

                         Uri u=Uri.parse(mySongs.get(position).toString());
                         myMediaPlayer=myMediaPlayer.create(getApplicationContext(),u);
                         sname=mySongs.get(position).getName().toString();
                         songTextLabel.setText(sname);
                         myMediaPlayer.start();

                    }
                });

                btn_previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myMediaPlayer.stop();
                        myMediaPlayer.release();  //in below line array ki postion  zero se kaam  nhi hogyi
                        position=((position-1)<0)?(mySongs.size()-1):(position-1);

                        Uri u=Uri.parse(mySongs.get(position).toString());
                        myMediaPlayer=myMediaPlayer.create(getApplicationContext(),u);
                        sname=mySongs.get(position).getName().toString();
                        songTextLabel.setText(sname);
                        myMediaPlayer.start();
                    }
                });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==android.R.id.home)   //for back button
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}