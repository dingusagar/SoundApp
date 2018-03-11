package org.drulabs.localdash.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.instacart.library.truetime.TrueTime;

import org.drulabs.localdash.LocalDashWiFiDirect;
import org.drulabs.localdash.notification.NotificationToast;

import java.io.IOException;
import java.util.Date;



public class TimeSyncUtils {
    private static TrueTime trueTime;

    private static String timeServer = "time.apple.com";

    public static  void initialise(){
        new InitTrueTimeAsyncTask().execute();
    }

    public static boolean isInitialised(){
        return TrueTime.isInitialized();
    }

    public static Date getTrueTime() throws IllegalStateException
    {
        return TrueTime.now();
    }



    private static class InitTrueTimeAsyncTask
            extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            try {
                TrueTime.build()
//                        .withSharedPreferences(SampleActivity.this)
                        .withNtpHost(timeServer)
                        .withLoggingEnabled(false)
                        .withConnectionTimeout(3_1428)
                        .initialize();
            } catch (IOException e) {
//                e.printStackTrace();

                Log.e("TRUETIME", "Exception when trying to get TrueTime", e);
            }
            return null;
        }
    }

}
