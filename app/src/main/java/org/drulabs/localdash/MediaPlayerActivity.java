package org.drulabs.localdash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TimeUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.instacart.library.truetime.TrueTime;

import org.drulabs.localdash.model.MediaPlayerCommandDTO;
import org.drulabs.localdash.notification.NotificationToast;
import org.drulabs.localdash.transfer.DataSender;
import org.drulabs.localdash.utils.DialogUtils;
import org.drulabs.localdash.utils.TimeSyncUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static java.lang.Math.abs;

public class MediaPlayerActivity extends AppCompatActivity {


    public static final String ACTION_COMMAND_RECEIVED = "org.drulabs.localdash.command_received";
    public static final String KEY_MEDIA_PLAYER_COMMAND_DATA = "mp_command_data";
    public static final String KEY_MEDIA_FILE = "mp_file";
    public static final String KEY_DEST_IP = "chatterip";
    public static final String KEY_DEST_PORT = "chatterport";

    private static final long EXECUTION_OFFSET = 2000;


    private File file  = null;
    private MediaPlayer mediaPlayer;
    private String destIP;
    private int destPort;

    ImageView playPauseButton;
    TextView songSelect;
    boolean isPlaying = false;

    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player2);
        setTitle("Media Player");

        initialize();

        playPauseButton = (ImageView) findViewById(R.id.play_pause);
        songSelect = (TextView) findViewById(R.id.line1);

        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPauseButtonClick(view);
            }
        });

        songSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSongFile();
            }
        });



    }

    private void initialize() {


        progressBar = new ProgressBar(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_COMMAND_RECEIVED);
        LocalBroadcastManager.getInstance(MediaPlayerActivity.this).registerReceiver(mediaplayerCommandReceiver, filter);


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            NotificationToast.showToast(MediaPlayerActivity.this, "Invalid arguments to open chat");
            finish();
        }

        destIP = extras.getString(KEY_DEST_IP);
        destPort = extras.getInt(KEY_DEST_PORT);




    }




    private BroadcastReceiver mediaplayerCommandReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_COMMAND_RECEIVED:
                    MediaPlayerCommandDTO mediaPlayerCommandDTO =
                            (MediaPlayerCommandDTO)intent.getSerializableExtra(KEY_MEDIA_PLAYER_COMMAND_DATA);

                    NotificationToast.showToast(getApplicationContext(),"Media Player Command : " +
                                                                    mediaPlayerCommandDTO.toString());
                    if(!isSongSelected()){
                        NotificationToast.showToast(MediaPlayerActivity.this,"Select a song first");
                        break;
                    }
                    scheduleMediaPlayerTask(mediaPlayerCommandDTO);


                    break;
                default:
                    break;
            }
        }
    };

    private void playPauseButtonClick(View view){
        if(!isSongSelected()){
            NotificationToast.showToast(this,"Select a song first !");
            return;
        }
        if(isPlaying) // to pause
        {
            isPlaying = false;
            changeButtonUItoPause();
            ButtonAction(MediaPlayerCommandDTO.PAUSE);
        }else // to play
        {
            isPlaying = true;
            changeButtonUItoPlay();
            ButtonAction(MediaPlayerCommandDTO.PLAY);

        }
    }
    private boolean isSongSelected(){
        if(file == null)
            return false;

        return true;
    }

    private void changeButtonUItoPlay(){
        playPauseButton.setImageResource(R.drawable.uamp_ic_pause_white_48dp);
    }

    private void changeButtonUItoPause(){
        playPauseButton.setImageResource(R.drawable.uamp_ic_play_arrow_white_48dp);
    }

    private void selectSongFile(){
        final File dir = new File(Environment.getExternalStorageDirectory() + "/"
                + "/localdash/" );

        DialogUtils.chooseMediaFileDialog(this,dir).show();
    }


    public void ButtonAction(String action) {
        MediaPlayerCommandDTO commandDTO = new MediaPlayerCommandDTO(action);
        Date trueDate;
        try{
            trueDate = TimeSyncUtils.getTrueTime();
        }catch (IllegalStateException e){
            trueDate = new Date();
            NotificationToast.showToast(getApplicationContext(),"Error in device times");
        }
        commandDTO.setTimeToExec(trueDate.getTime() + EXECUTION_OFFSET);

        DataSender.sendMediaPlayerCommandInfo(MediaPlayerActivity.this,destIP,destPort,commandDTO);
        NotificationToast.showToast(this,"sending "+action+" command ");

        scheduleMediaPlayerTask(commandDTO);
    }



    public void scheduleMediaPlayerTask(final MediaPlayerCommandDTO commandDTO){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                switch (commandDTO.getCommand()){
                    case MediaPlayerCommandDTO.PLAY:
                        mediaPlayer.start();
                        changeButtonUItoPlay();
                        break;
                    case MediaPlayerCommandDTO.PAUSE:
                        mediaPlayer.pause();
                        changeButtonUItoPause();
                        break;
                    case MediaPlayerCommandDTO.STOP:
                        mediaPlayer.stop();

                        break;
                }
            }
        };

        final Handler handler = new Handler();
        Date trueDate;
        try{
            trueDate = TimeSyncUtils.getTrueTime();
        }catch (IllegalStateException e){
            trueDate = new Date();
            NotificationToast.showToast(getApplicationContext(),"Error in device times");
        }
        long delay = abs(trueDate.getTime() - commandDTO.getTimeToExec());
        handler.postDelayed(runnable,delay);
    }

    public void setFileToPlay(File file)
    {
        this.file = file;
        if(file != null ){
            setUpMediaPlayer();
            songSelect.setText("Playing " + file.getName());
        }

    }

    public void setUpMediaPlayer()
    {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_local_dash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_time_sync) {
            TimeSyncUtils.initialise();
            NotificationToast.showToast(getApplicationContext(),"connecting to NTP server");
            return true;
        }else if (id == R.id.check_time_sync){
            if(TimeSyncUtils.isInitialised()){
                NotificationToast.showToast(getApplicationContext(),"Time sync was successful");
            }else{
                NotificationToast.showToast(getApplicationContext(),"Time sync failed");
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
