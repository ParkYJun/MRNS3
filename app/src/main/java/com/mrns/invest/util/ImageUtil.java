package com.mrns.invest.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ImageUtil {
    public static void saveBitmaptoJpeg(Bitmap bitmap,String folder, String name) {
        String ex_storage = EmulatorPath.getPath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/" + folder + "/";
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

    public static HashMap<String, Integer> getBitmapSize(Bitmap bmpSource, int maxWidth, int maxHeight){
        int iWidth = bmpSource.getWidth();      //비트맵이미지의 넓이
        int iHeight = bmpSource.getHeight();     //비트맵이미지의 높이
        int newWidth = iWidth ;
        int newHeight = iHeight ;
        float rate = 0.0f;

        HashMap<String,Integer> values = new HashMap<String,Integer>();

        //이미지의 가로 세로 비율에 맞게 조절
        if(iWidth > iHeight ){
            if(maxWidth < iWidth ){
                rate = maxWidth / (float) iWidth ;
                newHeight = (int) (iHeight * rate);
                newWidth = maxWidth;
            }
        }else{
            if(maxHeight < iHeight ){
                rate = maxHeight / (float) iHeight ;
                newWidth = (int) (iWidth * rate);
                newHeight = maxHeight;
            }
        }
        values.put("width", newWidth);
        values.put("height", newHeight);
        return values;
    }

    public static Bitmap cropBitmap(Bitmap bitmap, int width, int height, Matrix m) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        // 이미지를 crop 할 좌상단 좌표
        int x = 0;
        int y = 0;
        if (originWidth > width) { // 이미지의 가로가 view 의 가로보다 크면..
            x = (originWidth - width)/2;
        }
        if (originHeight > height) { // 이미지의 세로가 view 의 세로보다 크면..
            y = (originHeight - height)/2;
        }
        return Bitmap.createBitmap(bitmap, x, y, width, height, m, true);
    }
    public static Bitmap resizeBitmapImageFn(Bitmap bmpSource, int maxResolution){
        int iWidth = bmpSource.getWidth();      //비트맵이미지의 넓이
        int iHeight = bmpSource.getHeight();     //비트맵이미지의 높이
        int newWidth = iWidth ;
        int newHeight = iHeight ;
        float rate = 0.0f; //4대3 비율 맞추기위해서

        //이미지의 가로 세로 비율에 맞게 조절
        if(iWidth > iHeight ){
            if(maxResolution < iWidth ){
                //rate = maxResolution / (float) iWidth ;
                newHeight = (int) (iHeight * rate);
                newWidth = maxResolution;
            }
        }else{
            if(maxResolution < iHeight ){
                //rate = maxResolution / (float) iHeight ;
                newWidth = (int) (iWidth * rate);
                newHeight = maxResolution;
            }
        }

        return Bitmap.createScaledBitmap(bmpSource, newWidth, newHeight, true);
    }

    public static Bitmap resizeBitmapImageFn(Bitmap bmpSource, int maxWidth, int maxHeight){
        int iWidth = bmpSource.getWidth();      //비트맵이미지의 넓이
        int iHeight = bmpSource.getHeight();     //비트맵이미지의 높이
        int newWidth = iWidth ;
        int newHeight = iHeight ;
        float rate = 0.0f;

        //이미지의 가로 세로 비율에 맞게 조절
        if (iWidth < iHeight ) {
            if (maxWidth < iWidth) {
                rate = maxWidth / (float) iWidth ;
                newHeight = (int) (iHeight * rate);
                newWidth = maxWidth;
            }
        } else {
            if (maxHeight < iHeight) {
                rate = maxHeight / (float) iHeight ;
                newWidth = (int) (iWidth * rate);
                newHeight = maxHeight;
            }
        }
        return Bitmap.createScaledBitmap(bmpSource, newWidth, newHeight, true);
    }

}
