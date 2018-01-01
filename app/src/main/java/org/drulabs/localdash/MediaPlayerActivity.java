package org.drulabs.localdash;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.drulabs.localdash.model.ChatDTO;
import org.drulabs.localdash.model.MediaPlayerCommandDTO;
import org.drulabs.localdash.notification.NotificationToast;

import java.io.File;

public class MediaPlayerActivity extends AppCompatActivity {


    public static final String ACTION_COMMAND_RECEIVED = "org.drulabs.localdash.command_received";
    public static final String KEY_MEDIA_PLAYER_COMMAND_DATA = "mp_command_data";
    public static final String KEY_MEDIA_FILE = "mp_file";

    private File file;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        file = (File) getIntent().getExtras().get(KEY_MEDIA_FILE);
        if(file == null)
            NotificationToast.showToast(this,"Did not receive file properly");
        else
            initialize();
    }

    private void initialize() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_COMMAND_RECEIVED);
        LocalBroadcastManager.getInstance(MediaPlayerActivity.this).registerReceiver(mediaplayerCommandReceiver, filter);

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


                    break;
                default:
                    break;
            }
        }
    };
}
