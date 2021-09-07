package com.mrns.invest.util;

import android.os.Environment;

/**
 * Created by luclipse on 2016-02-20.
 */
public class EmulatorPath {
    public static String getPath() {
        String sdcard = Environment.getExternalStorageState();
        if (!sdcard.equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getRootDirectory().getAbsolutePath();
        } else {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }
}
