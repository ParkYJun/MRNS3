package com.mrns.invest.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {
    public static void saveBitmaptoJpeg(Bitmap bitmap,String folder, String name) {
        String ex_storage = EmulatorPath.getPath();
        // Get Absolute Path in External Sdcard
        String foler_name = File.separator + folder + File.separator;
        String file_name = name + ".jpg";
        String string_path = ex_storage + foler_name;
        File file_path;
        FileOutputStream out = null;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            out = new FileOutputStream(string_path + file_name);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException exception) {
            Log.e("FileNotFoundException", exception.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
        }
    }
}
