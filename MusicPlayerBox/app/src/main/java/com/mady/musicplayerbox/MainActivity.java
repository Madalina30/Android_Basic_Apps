package com.mady.musicplayerbox;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //declarations
    private MediaPlayer mediaPlayer;
    private ImageView artistImage;
    private TextView leftTime;
    private TextView rightTime;
    private SeekBar seekBar;
    private Button previous;
    private Button play;
    private Button next;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setUpUI();
        rightTime.setText(new SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getDuration())));

        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean user) {
                if (user) {
                    mediaPlayer.seekTo(progress);
                }
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
                int currentPosition = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                leftTime.setText(dateFormat.format(new Date(currentPosition)));
                rightTime.setText(dateFormat.format(new Date(duration - currentPosition)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    public void setUpUI() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.music);

        artistImage = findViewById(R.id.image);
        leftTime = findViewById(R.id.leftTime);
        rightTime = findViewById(R.id.rightTime);
        seekBar = findViewById(R.id.seekBar);
        previous = findViewById(R.id.previous);
        play = findViewById(R.id.play);
        next = findViewById(R.id.next);

        previous.setOnClickListener(this);
        play.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    public void pauseMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            play.setBackgroundResource(R.drawable.ic_play);
        }
    }

    public void startMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            updateThread();
            play.setBackgroundResource(R.drawable.ic_pause);
        }
    }

    public void previousMusic(){
        //for now - to implement for more songs
        if (mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(0);
        }
    }

    public void nextMusic(){
        //for now - to implement for more songs
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(mediaPlayer.getDuration());
            rightTime.setText("00:00");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.previous:
                previousMusic();
                break;

            case R.id.play:
                if (mediaPlayer.isPlaying()) {
                    pauseMusic();
                } else {
                    startMusic();
                }
                break;

            case R.id.next:
                nextMusic();
                break;
        }
    }

    public void updateThread(){
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (mediaPlayer != null && mediaPlayer.isPlaying()){
                        Thread.sleep(50);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int newPosition = mediaPlayer.getCurrentPosition();
                                int newMax = mediaPlayer.getDuration();
                                seekBar.setMax(newMax);
                                seekBar.setProgress(newPosition);

                                leftTime.setText(new SimpleDateFormat("mm:ss").format(new Date(newPosition)));
                                rightTime.setText(new SimpleDateFormat("mm:ss").format(new Date(newMax - newPosition)));
                            }
                        });
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer.isPlaying() && mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        thread.interrupt();
        thread = null;
        super.onDestroy();
    }
}