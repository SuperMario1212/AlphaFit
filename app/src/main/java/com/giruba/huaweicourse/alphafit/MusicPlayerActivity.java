package com.giruba.huaweicourse.alphafit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvTime, tvDuration, tvTitle, tvArtist;
    SeekBar seekBarTime, seekBarVolume;
    Button btnPlay;
    private ActionBar actionBar;

    MediaPlayer musicPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        actionBar = getSupportActionBar();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Song song = (Song) getIntent().getSerializableExtra("song");

        String title = song.getTitle();
        actionBar.setTitle(title);

        tvTime = findViewById(R.id.tvTime);
        tvDuration = findViewById(R.id.tvDuration);
        seekBarTime = findViewById(R.id.seekBarTime);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        btnPlay = findViewById(R.id.btnPlay);
        tvTitle = findViewById(R.id.tvTitle);
        tvArtist = findViewById(R.id.tvArtist);

        tvTitle.setText(song.getTitle());
        tvArtist.setText(song.getArtist());

        //plays music
        musicPlayer = new MediaPlayer();
        try {
            musicPlayer.setDataSource(song.getPath());
            musicPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        musicPlayer.setLooping(true);
        musicPlayer.seekTo(0);
        musicPlayer.setVolume(0.5f,0.5f);

        // music duration display
        String duration = millisecondsToString(musicPlayer.getDuration());
        tvDuration.setText(duration);


        // pause/play button action
        btnPlay.setOnClickListener(this);

        // volume loudness control
        seekBarVolume.setProgress(50);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                float volume = progress/100f;
                musicPlayer.setVolume(volume, volume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });

        // music duration control
        seekBarTime.setMax(musicPlayer.getDuration());
        seekBarTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean isFromUser) {
                if(isFromUser){
                    musicPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(musicPlayer != null)
                {
                    if(musicPlayer.isPlaying()){
                        try{
                            final double current = musicPlayer.getCurrentPosition();
                            final String elapsedTime = millisecondsToString((int) current);

                            //display music time change
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvTime.setText(elapsedTime);
                                    seekBarTime.setProgress((int) current);
                                }
                            });
                            Thread.sleep(1000);
                        }catch(InterruptedException e){}
                    }
                }
            }
        }).start();

    }

    //music duration
    public String millisecondsToString(int time){
        String elapsedTime = "";
        int minutes = time / 1000 / 60;
        int seconds = time / 1000 % 60;
        elapsedTime = minutes+":";
        if(seconds<10){
            elapsedTime += "0";
        }
        elapsedTime += seconds;

        return elapsedTime;
    }



    //pause play control
    //when music state pause , show button play
    //when music state play, show button pause
    @Override
    public void onClick(View view) {
        //when press button play
        if(view.getId() == R.id.btnPlay){
            //if music current state is playing
            //else it is not playing
            if(musicPlayer.isPlaying()){
                //pause the music
                musicPlayer.pause();
                //set play layout to button
                btnPlay.setBackgroundResource(R.drawable.play);
            }else {
                musicPlayer.start();
                btnPlay.setBackgroundResource(R.drawable.pause);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            if(musicPlayer.isPlaying()){
                musicPlayer.stop();
            }
        }
        return super.onOptionsItemSelected(item);
    }


}