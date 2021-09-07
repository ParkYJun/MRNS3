package com.mrns.downdata;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.mrns.main.R;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private Context mContext;
    ImageView mImageView;
    boolean mIsLast;

    public DownloadImageTask(boolean isLast, Context context, ImageView imageView) {
        this.mIsLast = isLast;
        this.mContext = context;
        this.mImageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(mContext.getResources().getString(R.string.exception), e.getMessage());
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        if (null == result) {
            BitmapDrawable noImg = (BitmapDrawable)mContext.getResources().getDrawable(R.drawable.noimage);
            mImageView.setImageDrawable(noImg);
        } else {
            mImageView.setImageBitmap(result);
        }
//        if (mIsLast) {
//            Toast.makeText(mContext, mContext.getString(R.string.message_complete_download_images), Toast.LENGTH_SHORT).show();
//        }
    }
}
