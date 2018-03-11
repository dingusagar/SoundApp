package org.drulabs.localdash.Intro;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import org.drulabs.localdash.HomeScreen;
import org.drulabs.localdash.R;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        addSlide(AppIntroFragment.newInstance("Connect ","Connect different smartphones through wifi direct", R.drawable.wifi_connect2, Color.parseColor("#5c164e")));
        addSlide(AppIntroFragment.newInstance("Sync Time ","Sync the system times of different devices from NTP server",R.drawable.sync_time,Color.parseColor("#ba3f1d")));
        addSlide(AppIntroFragment.newInstance("Share Music ","Share the music you want to play",R.drawable.wifi_share,Color.parseColor("#7e52a0")));
        addSlide(AppIntroFragment.newInstance("Play ","Play the music from all devices ",R.drawable.play2,Color.parseColor("#305252")));


    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, HomeScreen.class));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, HomeScreen.class));
    }
}