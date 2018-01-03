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
import android.view.View;

import org.drulabs.localdash.model.MediaPlayerCommandDTO;
import org.drulabs.localdash.notification.NotificationToast;
import org.drulabs.localdash.transfer.DataSender;
import org.drulabs.localdash.utils.DialogUtils;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        initialize();
    }

    private void initialize() {
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
                    scheduleMediaPlayerTask(mediaPlayerCommandDTO);


                    break;
                default:
                    break;
            }
        }
    };


    public void pauseButtonAction(View view) {
        MediaPlayerCommandDTO commandDTO = new MediaPlayerCommandDTO(MediaPlayerCommandDTO.PAUSE);
        Date now = new Date();
        commandDTO.setTimeToExec(now.getTime() + EXECUTION_OFFSET);

        DataSender.sendMediaPlayerCommandInfo(MediaPlayerActivity.this,destIP,destPort,commandDTO);
        NotificationToast.showToast(this,"sending pause command ");

        scheduleMediaPlayerTask(commandDTO);
    }

    public void playButtonAction(View view) {

        MediaPlayerCommandDTO commandDTO = new MediaPlayerCommandDTO(MediaPlayerCommandDTO.PLAY);
        Date now = new Date();
        commandDTO.setTimeToExec(now.getTime() + EXECUTION_OFFSET);

        DataSender.sendMediaPlayerCommandInfo(MediaPlayerActivity.this,destIP,destPort,commandDTO);
        NotificationToast.showToast(this,"sending play command ");
        scheduleMediaPlayerTask(commandDTO);

    }

    public void scheduleMediaPlayerTask(final MediaPlayerCommandDTO commandDTO){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                switch (commandDTO.getCommand()){
                    case MediaPlayerCommandDTO.PLAY:
                        mediaPlayer.start();
                        break;
                    case MediaPlayerCommandDTO.PAUSE:
                        mediaPlayer.pause();
                        break;
                    case MediaPlayerCommandDTO.STOP:
                        mediaPlayer.stop();
                        break;
                }
            }
        };

        final Handler handler = new Handler();
        long delay = abs((new Date()).getTime() - commandDTO.getTimeToExec());
        handler.postDelayed(runnable,delay);
    }

    public void setFileToPlay(File file)
    {
        this.file = file;
        if(file != null )
            setUpMediaPlayer();
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


    public void selectFileToPlay_Button(View view) {
        final File dir = new File(Environment.getExternalStorageDirectory() + "/"
                + "/localdash/" );

        DialogUtils.chooseMediaFileDialog(this,dir).show();

    }
}
