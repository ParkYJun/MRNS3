package com.mrns.common;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by geopeople on 2016-02-01.
 */
public class BackPressCloseSystem {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseSystem(Activity activity) {
        this.activity = activity;
    }

    public void onBackPressed() {

        programShutdown();
    }

    private void programShutdown() {
        activity .moveTaskToBack(true);
        activity .finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

}
