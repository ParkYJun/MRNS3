package com.mrns.downdata;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mrns.main.R;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
public class DownloadMAPAsync extends AsyncTask<String, String, String> {

    private Context mContext;
    private ProgressDialog mProgressDialog;

    public DownloadMAPAsync(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("지도자료 다운로드");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;

        try {

            URL url = new URL(aurl[0]);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int lenghtOfFile = conexion.getContentLength();
//            Log.d("ANDRO_ASYNC", "Lenght offile: " + lenghtOfFile);

            String downfileName = aurl[1];
//            Log.d("ANDRO_ASYNC", downfileName);


            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(downfileName);

            byte data[] = new byte[1024];
            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e){
            Log.e(mContext.getString(R.string.iOException), e.getMessage());
        } catch (Exception e) {
            Log.e(mContext.getString(R.string.exception), e.getMessage());
        } finally {
            Log.i(mContext.getString(R.string.done), "DownloadMAPAsync done");
        }

        return null;
    }

    protected void onProgressUpdate(String... progress) {
//        Log.d("ANDRO_ASYNC", progress[0]);
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onPostExecute(String unused) {

        mProgressDialog.dismiss();

        Toast.makeText(mContext, "다운로드를 완료했습니다.", Toast.LENGTH_SHORT).show();
    }

    public String getSDPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}