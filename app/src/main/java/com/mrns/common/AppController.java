package com.mrns.common;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.mrns.invest.util.DBUtil;
import com.mrns.main.R;
import com.orm.SugarApp;
import com.orm.SugarRecord;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppController extends SugarApp {
    //결제용
    public Uri m_uriResult;
    public  boolean b_type      = false;

    public static final String m_strAppUrl = "appurl";
    public static final String m_strLogTag = "KCreditSample";

    public static final String TAG = AppController.class.getSimpleName();

    /** Charset for request. */
    private static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE = String
            .format("application/json; charset=%s",
                    PROTOCOL_CHARSET);

    private static AppController mInstance;

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    public static synchronized AppController getInstance() {

        return mInstance;
    }
    @Override
    public void onCreate() {
        if(!DBUtil.checkDataBase(getApplicationContext())) {
            try {
                DBUtil.copyDataBase(getApplicationContext());
            } catch (IOException e) {
                Log.e("","Error IOException");
            }
        }
        super.onCreate();

        try {
            SugarRecord.getSugarDataBase().execSQL("ALTER TABLE RNS_STDRSPOT_UNITY ADD COLUMN ID INTEGER");
        } catch (SQLiteException e) {
            Log.e(this.getClass().getName(), "컬럼이 이미 있음");
        }
        try {
            SugarRecord.getSugarDataBase().execSQL("UPDATE RNS_STDRSPOT_UNITY set ID = STDRSPOT_SN");
        } catch (SQLiteException e) {
            Log.e(this.getClass().getName(), "ID 셋팅 되어 있음");
        }
        try {
            SugarRecord.getSugarDataBase().execSQL("ALTER TABLE RNS_STTUS_INQ_BEFORE ADD COLUMN ID INTEGER");
        } catch (SQLiteException e) {
            Log.e(this.getClass().getName(), "컬럼이 이미 있음");
        }
        try {
            SugarRecord.getSugarDataBase().execSQL("UPDATE RNS_STTUS_INQ_BEFORE set ID = STTUS_BF_SN");
        } catch (SQLiteException e) {
            Log.e(this.getClass().getName(), "ID 셋팅 되어 있음");
        }

        try {
            SugarRecord.getSugarDataBase().execSQL("ALTER TABLE RNS_STTUS_INQ_HIST ADD COLUMN ID INTEGER");
        } catch (SQLiteException e) {
            Log.e(this.getClass().getName(), "컬럼이 이미 있음");
        }
        try {
            SugarRecord.getSugarDataBase().execSQL("UPDATE RNS_STTUS_INQ_HIST set ID = STTUS_SN");
        } catch (SQLiteException e) {
            Log.e(this.getClass().getName(), "ID 셋팅 되어 있음");
        }

        /*
            현황조사 시 촬영한 사진은 10일 현황조사 전송 후 삭제
         **/

        deleteOldFile();

        mInstance = this;
    }

    //모바일에서 저장한(앨범에서 불러와 저장하거나 촬영한 영상만 삭제
    //기준점 다운로드 한 자료의 삭제는 제외
    public void deleteOldFile() {
        Common util = new Common();
        String targetFolder = util.getSDPath() + "/MRNS/";
        File dir = new File(targetFolder);

        if(!dir.exists()){
            dir.mkdir();
        }else{
            File[] listFiles = dir.listFiles();
            if (listFiles != null) {
                int delFileCnt = 0;
                long delTime = Long.parseLong(getString(R.string.set_del_time));

                for (File file : listFiles) {

                    if (getExtension(file.getName()).equalsIgnoreCase("jpg") || getExtension(file.getName()).equalsIgnoreCase("png")) {

                        //File Pattern~~
                        //11140D000000615_201604291722_rogimage.jpg
                        // 20170906110312_967450_48860D000017258_201709061042_rogimage.jpg
                        // 20180109155652_drw_image.jpg
                        String[] pattern = file.getName().split("_");
                        if (pattern.length == 3) {
                            String endTime = pattern[1].substring(0, 4) + "-" + pattern[1].substring(4, 6) + "-" + pattern[1].substring(6, 8) +
                                    " " + pattern[1].substring(8, 10) + ":" + pattern[1].substring(10, 12) + ":00";
                            try {
                                if (getElapsedTime(endTime) >= delTime) {
                                    file.delete();
                                    delFileCnt++;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }//end if

                    }//end if
                }//end for
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }

        return false;
    }

    public long getElapsedTime( String end_time ) throws Exception{

        String dateFormatStr = "yyyy-MM-dd HH:mm:ss";
        Calendar calendar = Calendar.getInstance();
        DateFormat stringFormat = new SimpleDateFormat(dateFormatStr);

        try{
            long now = System.currentTimeMillis();
            Date beginDate = new Date(now);
            Date endDate = stringFormat.parse(end_time);
            long gap = (beginDate.getTime() - endDate.getTime())/1000;
            return gap / 60 / 60;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    public static String getExtension(String fileStr){
        return fileStr.substring(fileStr.lastIndexOf(".")+1,fileStr.length());
    }

    public RequestQueue getRequestQueue() {

        if (mRequestQueue == null) {

            mRequestQueue = Volley.newRequestQueue(getApplicationContext());

        }

        mRequestQueue.getCache().clear();

        return mRequestQueue;

    }

    public <T> void addToRequestQueue(Request<T> req) {

        req.setTag(TAG);
        getRequestQueue().add(req);

    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {

            mRequestQueue.cancelAll(tag);

        }
    }
}
