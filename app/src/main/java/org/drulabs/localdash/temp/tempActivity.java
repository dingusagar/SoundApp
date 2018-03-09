package org.drulabs.localdash.temp;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.drulabs.localdash.R;
import org.drulabs.localdash.utils.DialogUtils;

import java.io.File;

public class tempActivity extends AppCompatActivity {

    ImageView playPauseButton;
    TextView songSelect;
    boolean isPlaying = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player2);
        setTitle("Media Player");

        playPauseButton = (ImageView) findViewById(R.id.play_pause);
        songSelect = (TextView) findViewById(R.id.line1);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPauseButtonClick();
            }
        });

        songSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSongFile();
            }
        });

    }


    private void playPauseButtonClick(){
        if(isPlaying)
        {
            isPlaying = false;
            playPauseButton.setImageResource(R.drawable.uamp_ic_play_arrow_white_48dp);
        }else
        {
            isPlaying = true;
            playPauseButton.setImageResource(R.drawable.uamp_ic_pause_white_48dp);

        }
    }

    private void selectSongFile(){
//        final File dir = new File(Environment.getExternalStorageDirectory() + "/"
//                + "/localdash/" );
//
//        DialogUtils.chooseMediaFileDialog(this,dir).show();
    }
}



