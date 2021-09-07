package com.mrns.downdata;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.mrns.common.Common;
import com.mrns.invest.record.UserInfoRecord;
import com.mrns.main.R;
import com.orm.SugarRecord;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DownloadDBAsync extends AsyncTask<String, String, String> {

    private Common util = new Common();

    private Context mContext;
    private ProgressDialog mProgressDialog;
    private String downfileName;
    private String targetName;
    private String urlStr;

    public DownloadDBAsync(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setMessage("현황조사 대상자료 다운로드");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        downfileName = Environment.getExternalStorageDirectory() + "/MRNS.zip";
        targetName = util.getSDPath() + "/MRNS/";

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... aurl) {
        int count;

        try {
            urlStr = aurl[0];
            URL url = new URL(urlStr);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int lenghtOfFile = conexion.getContentLength();
//            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

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
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            mProgressDialog.dismiss();
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
//테이블 존재 유무 확인
        String  unityTable = "select count(*) from sqlite_master where Name = 'RNS'";
        Cursor cursorUnityTable = SugarRecord.getSugarDataBase().rawQuery(unityTable, null);
        cursorUnityTable.moveToFirst();
        long cnt = cursorUnityTable.getLong(0);
        cursorUnityTable.close();
        if(cnt > 0){
            String deleteSqlStdrspotUnity = "delete from rns_stdrspot_unity";
            String deleteSqlSttus = "delete from rns_sttus_inq_before";
            String deleteSqlRnsNpUnity = "delete from rns_np_unity";
            String deleteSqlRnsNpSttus = "delete from rns_np_sttus_inq_bf";
            Cursor cursorDeleteSqlStdrspotUnity = SugarRecord.getSugarDataBase().rawQuery(deleteSqlStdrspotUnity, null);
            Cursor cursorDeleteSqlSttus = SugarRecord.getSugarDataBase().rawQuery(deleteSqlSttus, null);
            Cursor cursorDeleteSqlRnsNpUnity = SugarRecord.getSugarDataBase().rawQuery(deleteSqlRnsNpUnity, null);
            Cursor cursorDeleteSqlRnsNpSttus = SugarRecord.getSugarDataBase().rawQuery(deleteSqlRnsNpSttus, null);

            cursorDeleteSqlStdrspotUnity.moveToFirst();
            cursorDeleteSqlSttus.moveToFirst();
            cursorDeleteSqlRnsNpUnity.moveToFirst();
            cursorDeleteSqlRnsNpSttus.moveToFirst();

            cursorDeleteSqlStdrspotUnity.close();
            cursorDeleteSqlSttus.close();
            cursorDeleteSqlRnsNpUnity.close();
            cursorDeleteSqlRnsNpSttus.close();

        }
        try {
            unZipIt(downfileName, targetName);
            Toast.makeText(mContext, "다운로드를 완료했습니다.", Toast.LENGTH_SHORT).show();

            SharedPreferences pref = mContext.getSharedPreferences(mContext.getString(R.string.classCheckPref), Context.MODE_PRIVATE);
            boolean isStd = true;

            if (urlStr.contains("?nsil_sn=")) {
                isStd = false;
            }

            pref.edit().putBoolean(mContext.getString(R.string.classCheckKey), isStd).apply();

            //2018-02-21 현황조사 > 기본정보 초기화
            SharedPreferences preSession = mContext.getSharedPreferences("preSession", Activity.MODE_PRIVATE);
            SharedPreferences representSession = mContext.getSharedPreferences(mContext.getString(R.string.key_represent_inq_session), Activity.MODE_PRIVATE);
            SharedPreferences realSession = mContext.getSharedPreferences(mContext.getString(R.string.key_real_inq_session), Activity.MODE_PRIVATE);

            String empty = mContext.getString(R.string.empty);
            String keyUserID = mContext.getString(R.string.key_inq_user_id);
            String keyUserName = mContext.getString(R.string.key_inq_user_name);
            String keyBranchCode = mContext.getString(R.string.key_inq_branch_code);
            String keyPositionCode = mContext.getString(R.string.key_inq_position_code);

            UserInfoRecord userInfoRecord = new UserInfoRecord(
                    preSession.getString(keyUserID, empty),
                    preSession.getString(keyUserName, empty),
                    preSession.getString(keyBranchCode, empty),
                    preSession.getString(keyPositionCode, empty)
            );

            SharedPreferences sessonArray[] = {representSession, realSession};

            for (SharedPreferences session : sessonArray) {
                if (session != null && userInfoRecord != null) {
                    SharedPreferences.Editor editor = session.edit();

                    editor.putString(keyUserID, userInfoRecord.getUserId());
                    editor.putString(keyUserName, userInfoRecord.getUserName());
                    editor.putString(keyBranchCode, userInfoRecord.getBranchCode());
                    editor.putString(keyPositionCode, userInfoRecord.getPositionCode());
                    editor.apply();
                }
            }

        } catch (FileNotFoundException e) {
            Toast.makeText(mContext, "다운로드 중 에러가 발생했습니다. 에러 이유 : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void unZipIt(String zipFile, String outputFolder) throws FileNotFoundException {

        byte[] buffer = new byte[1024];

        try{

            //create output directory is not exists
            File folder = new File(outputFolder);
            if(!folder.exists()){
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(new FileInputStream(zipFile));
            //get the zipped file list entry
            ZipEntry ze = null;
            try {
                ze = zis.getNextEntry();
            } catch (IOException e) {
                e.getMessage();
            } finally {
                Log.i("done", "unzip start!!");
            }

            while(ze!=null){

                String fileName = ze.getName();
                File newFile = new File(outputFolder + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();

                FileOutputStream fos = new FileOutputStream(newFile);

                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }

                fos.close();
                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }finally {
            //다운로드 한 zip 파일은 삭제한다.
            File delZip = new File(zipFile);
            if(delZip.isFile()) {
                delZip.delete();
            }

            Resources resources = mContext.getResources();

            //DB 경로 변경
            File outPath = new File(resources.getString(R.string.db_path));
            if(!outPath.isDirectory()) {
                outPath.mkdirs();
            }

            String source = targetName + resources.getString(R.string.db_name);
            moveFile(source, resources.getString(R.string.db_full_name));
        }
    }

    // 파일을 해당위치로 복사하고 지운다.
    private boolean moveFile(String source, String dest) {
        boolean result;

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;

        try {
            inputStream = new FileInputStream(source);
            outputStream = new FileOutputStream(dest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        FileChannel fcin = null;
        FileChannel fcout = null;
        long size;

        try {
            fcin = inputStream.getChannel();
            fcout = outputStream.getChannel();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        try {
            size = fcin.size();
            fcin.transferTo(0, size, fcout);

            fcout.close();
            fcin.close();
            outputStream.close();
            inputStream.close();

            result = true;
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }

        File f = new File(source);

        if (f.delete()) {
            result = true;
        }
        return result;
    }
}
