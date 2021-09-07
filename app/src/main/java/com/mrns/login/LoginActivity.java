package com.mrns.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.core.content.FileProvider;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lumensoft.touchen.mguard.utils.MguardApiJsonClient;
import com.lumensoft.touchen.mguard.utils.MguardAuthException;
import com.mrns.common.BackPressCloseSystem;
import com.mrns.common.Common;
import com.mrns.common.NetcheckMoniter;
import com.mrns.main.NoticeActivity;
import com.mrns.main.R;
import com.mrns.sqllite.DBHelper;
import com.mrns.utils.BaseActivity;

import net.secuwiz.SecuwaySSL.Api.MobileApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    // UI references.
    private EditText useridView;
    private EditText mPasswordView;
    private CheckBox idCkView;
    boolean isSaveCk = false;

    IBinder tempService = null;

    BackPressCloseSystem backPressCloseSystem;

    String mode;
    boolean bOnline;
    ImageView mOn;
    ImageView mOff;
    TextView tOn;
    TextView tOff;
    Button mSignInButton;
    Button optButton;

    Intent movePage;

    Intent intent;
    String mdmDomain;
    String MGUARD_APK;
    String MGUARD_PACKAGE;
    String KT_MDM_PACKAGE;
    String OPT_PACKAGE;
    private final static int MGUARD_VERSION_CODE = 107;

    String LoginUrl;
    String sqliteFileUrl;

    SQLiteDatabase db;
    DBHelper helper;
    long insertCnt = 0;
    JSONObject userInfo = null;

    ProgressDialog dialog;

    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    String dbName;
    String dbPath;

    String USER_TABLE_NAME = "ST_USER";

    private SecuwayServiceConnection mConnection = new SecuwayServiceConnection();

    private Common util = new Common();

    private NetcheckMoniter networkMoniter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbName = getString(R.string.db_user_full_name);
        dbPath = getString(R.string.db_path);
        intent = getIntent();
        bOnline = intent.getBooleanExtra(getString(R.string.key_bonline), bOnline);
        mode = intent.getStringExtra(getString(R.string.key_mode));

        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        //설치 여부 판단
        MGUARD_APK = getString(R.string.MGUARD_APK);
        MGUARD_PACKAGE = getString(R.string.MGUARD_PACKAGE);
        KT_MDM_PACKAGE = getString(R.string.KT_MDM_PACKAGE);
        //mdmIsInstall();        //사용안함(이전프로그램 사용 안함으로 주석.)

        backPressCloseSystem = new BackPressCloseSystem(this);

        useridView = (EditText) findViewById(R.id.userid);
        EditText editText = (EditText)findViewById(R.id.userid);              //대문자
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});      //대문자
        mPasswordView = (EditText) findViewById(R.id.optpw);
        idCkView = (CheckBox) findViewById(R.id.saveId_ck);
        idCkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idCkView.isChecked()) {
                    isSaveCk = true;
                } else {
                    isSaveCk = false;
                }
            }
        });

        //스위치 세팅
        mOn = (ImageView) findViewById(R.id.switchOn);
        mOff = (ImageView) findViewById(R.id.switchOff);
        tOn = (TextView) findViewById(R.id.textOn);
        tOff = (TextView) findViewById(R.id.textOff);

        if (bOnline) {
            mOff.setVisibility(View.GONE);
            tOff.setVisibility(View.GONE);
        } else {
            mOn.setVisibility(View.GONE);
            tOn.setVisibility(View.GONE);
        }

        SharedPreferences prefLogin = getSharedPreferences("prefLogin", Activity.MODE_PRIVATE);
        //저장된 값들을 불러옵니다.
        boolean chkID = prefLogin.getBoolean(getString(R.string.key_check_id), false);
        isSaveCk = chkID;
        idCkView.setChecked(chkID);

        if (isSaveCk) {
            String saveId = prefLogin.getString(getString(R.string.key_save_id), "");
            useridView.setText(saveId);
        }

        optButton = (Button) findViewById(R.id.opt_btn);
        mSignInButton = (Button) findViewById(R.id.login_button);

        View.OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.opt_btn:
                        //OPT_PACKAGE
                        OPT_PACKAGE = getString(R.string.opt_package_name);
                        Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(OPT_PACKAGE);
                        Intent isOPTInstall = getPackageManager().getLaunchIntentForPackage(OPT_PACKAGE);

                        if (isOPTInstall == null) {//설치가 안되어있을때
                            Toast.makeText(getApplicationContext(), "OTP 프로그램을 설치하세요.\n관리자에가 문의 바랍니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(intent);
                        }
                        break;
                    case R.id.login_button:
                        //sql database download;
                        if (attemptLogin()) {//ip, pw validation
                            dialog = ProgressDialog.show(LoginActivity.this, "", getString(R.string.login_loading_msg), true);
                            dialog.show();

                            LoginUrl = getString(R.string.server_ip) + getString(R.string.req_login);
                            requestLoginPOST();
                        }

                        break;
                }
            }
        };

        optButton.setOnClickListener(listener);
        mSignInButton.setOnClickListener(listener);

        File directory = new File(dbPath);
        if(!directory.exists()){
            directory.mkdirs();
        }
        //sql database download;
        sqliteFileUrl = getString(R.string.server_ip) + getString(R.string.req_sqlite);
        new DownloadFileAsync().execute(sqliteFileUrl);
//        }
    }

    void requestLoginPOST() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, LoginUrl, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> hash = new HashMap<>();
                hash.put("usid", useridView.getText().toString());
                hash.put("otp", mPasswordView.getText().toString());
                return hash;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> successListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String reponse) {
            requestUserInfo(reponse);
        }
    };

    private Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {
            dialog.dismiss();
            Toast.makeText(LoginActivity.this, getString(R.string.permission_network), Toast.LENGTH_SHORT).show();
        }
    };

    void requestUserInfo(String str) {

        try {
            //JSONObject set
            JSONObject jObj = new JSONObject(str);
            JSONObject tInfo = jObj.getJSONObject("listXmlReturnVO");

            if (!tInfo.isNull("resultVO")) {

                //전역변수에 사용자 정보 저장
                userInfo = tInfo.getJSONObject("resultVO");

                //user 세션 저장하기
                saveSessionUserInfo();

                //userInfo sqlite에 담기
                long insertUserInfoCnt = userInfoInsertUpdate();
                dialog.dismiss();

                movePage = new Intent(LoginActivity.this, NoticeActivity.class);
                movePage.putExtra(getString(R.string.key_bonline), bOnline);
                startActivity(movePage);

            } else {
                dialog.dismiss();
                Toast.makeText(LoginActivity.this, getString(R.string.permission_rationale), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(getString(R.string.jSONException), e.getMessage());
        } finally {
            Log.i(getString(R.string.done), "requestUserInfo done");
        }
    }

    public void saveSessionUserInfo() throws JSONException {

        SharedPreferences prefSession = getSharedPreferences("preSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefSession.edit();

        editor.putString(getString(R.string.key_inq_user_id), userInfo.getString("usid"));
        editor.putString(getString(R.string.key_inq_user_name), userInfo.getString("user_nm"));
        editor.putString("clsf_clcd", userInfo.getString("clsf_clcd"));
        editor.putString("dty_clcd", userInfo.getString("dty_clcd"));
        editor.putString("jbgp_clcd", userInfo.getString("jbgp_clcd"));
        editor.putString("dept_code", userInfo.getString("dept_code"));
        editor.putString(getString(R.string.key_inq_position_code), userInfo.getString("ofcps_clcd"));
        editor.putString(getString(R.string.key_inq_branch_code), userInfo.getString("dept_code"));
        editor.apply();

    }

    private BroadcastReceiver mVpnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO: move into IntentFilter, or dispatch from to DaemonEnabler.receive()
            if (intent.getAction().equals("com.secuwiz.SecuwaySSL.Service.STATUS")) {
                System.out.println("VPN status onReceive : " + intent.getIntExtra(getString(R.string.key_status), 0));
            }

        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DOWNLOAD_PROGRESS:
                mProgressDialog = new ProgressDialog(this);
                mProgressDialog.setMessage(getString(R.string.login_setting_msg));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
                return mProgressDialog;
            default:
                return null;
        }
    }

    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(DIALOG_DOWNLOAD_PROGRESS);
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;

            try {

                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(dbName);

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
            } catch (IOException e) {
                Log.e(getString(R.string.iOException), e.getMessage());
            } finally {
                Log.i(getString(R.string.done), "DownloadFileAsync done");
            }
            return null;

        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {

            mProgressDialog.dismiss();

        }
    }

    //user정보 insert하기
    private long userInfoInsertUpdate() {

        helper = new DBHelper(getApplicationContext(), dbName, null, 1);
        db = helper.getWritableDatabase();
        helper.onCreate(db);

        try {

            ContentValues values = new ContentValues();
            values.put("empuno", userInfo.getString("empuno"));
            values.put("usid", userInfo.getString("usid"));
            values.put("user_nm", userInfo.getString("user_nm"));
            values.put("pwd", userInfo.getString("pwd"));
            values.put("clsf_clcd", userInfo.getString("clsf_clcd"));
            values.put("dty_clcd", userInfo.getString("dty_clcd"));
            values.put("ofcps_clcd", userInfo.getString("ofcps_clcd"));
            values.put("jbgp_clcd", userInfo.getString("jbgp_clcd"));
            values.put("user_sttus_secd", userInfo.getString("user_sttus_secd"));
            values.put("use_yn", userInfo.getString("use_yn"));
            values.put("last_upddt", userInfo.getString("last_upddt"));
            values.put("email", userInfo.getString("email"));
            values.put("mbtlnum", userInfo.getString("mbtlnum"));
            values.put("cmpny_wtelno", userInfo.getString("cmpny_wtelno"));
            values.put("dept_code", userInfo.getString("dept_code"));
            values.put("grad_se_cd", userInfo.getString("grad_se_cd"));
            values.put("dept_nm", userInfo.getString("dept_nm"));
            values.put("upper_dept_code", userInfo.getString("upper_dept_code"));
            values.put("upper_dept_nm", userInfo.getString("upper_dept_nm"));

            try {
                insertCnt = db.insertOrThrow(USER_TABLE_NAME, null, values);
            } catch (Exception e) {
                insertCnt = db.update(USER_TABLE_NAME, values, "empuno=" + userInfo.getString("empuno"), null);
            }

        } catch (NullPointerException e) {
            mProgressDialog.dismiss();
            Log.e(getString(R.string.nullPointerException), e.getMessage());
            return -1;
        } catch (Exception e) {
            mProgressDialog.dismiss();
            Log.e(getString(R.string.exception), e.getMessage());
            return -1;
        } finally {
            db.close();
        }
        return insertCnt;
    }


    @Override
    protected void onStop() {
        SharedPreferences pref = getSharedPreferences("prefLogin", Activity.MODE_PRIVATE);
        // UI 상태를 저장합니다.
        SharedPreferences.Editor editor = pref.edit();
        // Editor를 불러옵니다.
        EditText userid = (EditText) findViewById(R.id.userid);
        CheckBox check1 = (CheckBox) findViewById(R.id.saveId_ck);

        if (isSaveCk) {
            // 저장할 값들을 입력합니다.
            editor.putString(getString(R.string.key_save_id), userid.getText().toString());
        } else {
            // 값들을 초기화 합니다.
            editor.putString(getString(R.string.key_save_id), "");
        }

        editor.putBoolean(getString(R.string.key_check_id), check1.isChecked());
        editor.apply();

        super.onStop();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

                builder.setMessage("프로그램을 종료 하시겠습니까?");
                builder.setTitle("");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopSSLVPN();
                        backPressCloseSystem.onBackPressed();
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.create().show();


                break;
        }

        return false;
    }

    @Override
    protected void onResume() {
        IntentFilter completeFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mVpnReceiver, completeFilter);

        Intent intent = new Intent("net.secuwiz.SecuwaySSL.mobileservice");
        intent.setPackage("net.secuwiz.SecuwaySSL.mobileservice");

        // bindService 호출
        // 정상적으로 호출되면 onServiceConnected() 부분 호출됨
        if (!bindService(intent, mConnection, BIND_AUTO_CREATE)) {
            System.out.println("bind service error");
        }

        IntentFilter intentFilter = new IntentFilter("com.secuwiz.SecuwaySSL.Service.STATUS");
        registerReceiver(mVpnReceiver, intentFilter);

        super.onResume();
    }

    @Override
    protected void onPause() {

        unregisterReceiver(mVpnReceiver);
//        unregisterReceiver(completeReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            unregisterReceiver(networkMoniter);
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            unbindService(mConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class StopRunnable implements Runnable {
        public void run() {
            //bindService 가 정상적으로 되고 onServiceConnected() 까지 호출된 다음 실행
            //서비스에 연결 되었으면 vpn 로그아웃
            if (tempService != null) {
                MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

                try {
                    objAidl.StopVpn();
                } catch (RemoteException e) {
                    Log.e(getString(R.string.remoteException), e.getMessage());
                } finally {
                    Log.i(getString(R.string.done), "done StopRunnable");
                }
            }
        }
    }

    public void stopSSLVPN() {
        StopRunnable a = new StopRunnable();
        Thread thread = new Thread(a);
        thread.start();
    }

    //설치된 api 의 version code 확인
    public int getVersionCode(String APK_PACKAGE) {
        int versionCode = 0;

        try {
            versionCode = getPackageManager().getPackageInfo(APK_PACKAGE, 0).versionCode;
        } catch (NullPointerException e) {
            Log.e("NullPointerException", e.getMessage());
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("NameNotFoundException", e.getMessage());
        } finally {
            Log.i("done", "getVersionCode : " + versionCode);
        }

        return versionCode;
    }

    //api 파일을 설치하기 위한 처리 방법
    private void install(String APK_NAME) {
        try {
            InputStream is = getAssets().open(APK_NAME);
            FileOutputStream fos = null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fos = openFileOutput(APK_NAME, Context.MODE_PRIVATE);
            } else {
                fos = openFileOutput(APK_NAME, Context.MODE_WORLD_READABLE| Context.MODE_WORLD_WRITEABLE);
            }

            byte[] buffer = new byte[1024];
            int byteRead;

            while((byteRead = is.read(buffer)) != -1)
            {
                fos.write(buffer, 0, byteRead);
            }

            fos.flush();
            fos.close();
            is.close();

            File apk = getFileStreamPath(APK_NAME);
            if (apk.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(LoginActivity.this, getApplicationContext().getPackageName() + ".fileprovider", apk);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    uri = Uri.fromFile(apk);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                intent.setDataAndType(uri, "application/vnd.android.package-archive");

                startActivity(intent);
            }
        } catch (IOException e) {
            Log.e(getString(R.string.iOException), e.getMessage());
        } finally {
            Log.i(getString(R.string.done), "install done");
        }
    }

    private class SecuwayServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder service) {
            //서비스 바인더  멤버변수로 저장
            tempService = service;

        }

        public void onServiceDisconnected(ComponentName name) {
        }
    }


    public void setbOnline(boolean bflag) {

        if (bflag) {
            mOn.setVisibility(View.GONE);
            tOn.setVisibility(View.GONE);
            mOff.setVisibility(View.VISIBLE);
            tOff.setVisibility(View.VISIBLE);
            bOnline = false;
        } else {
            mOn.setVisibility(View.VISIBLE);
            tOn.setVisibility(View.VISIBLE);
            mOff.setVisibility(View.GONE);
            tOff.setVisibility(View.GONE);
            bOnline = true;
        }

    }

    private boolean attemptLogin() {
        boolean cancel = false;
        View focusView = null;

        String userid = useridView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (userid.equals("")) {
            useridView.setError(null);
            useridView.requestFocus();
        }

        if (password.equals("")) {
            mPasswordView.setError(null);
            mPasswordView.requestFocus();
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_sslpw_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(userid)) {
            useridView.setError(getString(R.string.error_sslid_required));
            focusView = useridView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {
            return true;
        }

    }

    //사용안함(예전프로그램 사용 안함으로 주석.)
    void mdmIsInstall() {
        Intent isMdmInstall = getPackageManager().getLaunchIntentForPackage(MGUARD_PACKAGE);
        if (isMdmInstall == null) {//MGUARD 설치가 안되어있을때
            isMdmInstall = getPackageManager().getLaunchIntentForPackage(KT_MDM_PACKAGE);
            if (isMdmInstall == null) { //MDM 설치가 안되어있을 때
                Toast.makeText(getApplicationContext(), "mGuard 또는 KT MDM 보안 프로그램을 설치하세요.\n관리자에가 문의 바랍니다.\n3초후 프로그램이 종료됩니다.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                    @Override
                    public void run() {
                        // 실행할 동작 코딩
                        backPressCloseSystem.onBackPressed();
                    }
                }, 3000);
            }
        }//end if
    }

    //사용안함
    void mdmConnection() {
        try {

            MGUARD_APK = getString(R.string.MGUARD_APK);
            MGUARD_PACKAGE = getString(R.string.MGUARD_PACKAGE);

            //설치 여부 판단
            if (MGUARD_VERSION_CODE >= getVersionCode(MGUARD_PACKAGE)) {
                install(MGUARD_APK);
            }


            // mGuard 도메인
            mdmDomain = getString(R.string.MGUARD_ADDR);
            /*
             * API 요청
             *
             * @param domain
             * @param isSsl - https로 연결 = true, http로 연결 = false
             * @param serviceName
             * @param commandName
             * @param requestData
             * @param cookieData
             * @return - 처리 결과 JSON 형태 입니다.
             * @throws Exception
             */

            MguardApiJsonClient mguardApiClient = new MguardApiJsonClient();

            // 관리자 계정으로 로그인 인증
//                            String authCookie = mguardApiClient.getAuthCookie(httpDomain, true, "systemadmin@lumensoft.co.kr", "Ekswl56&*");
            String authCookie = mguardApiClient.getAuthCookie(mdmDomain, true, "test5", "test5!");

//                            Toast.makeText(LoginActivity.this, "로그인인증 성공 : " + authCookie, Toast.LENGTH_SHORT).show();

            Map<String, String> requestData = new HashMap<>();
            requestData.put("DEVICE_ID", "354677061262645"); //안드로이드 IMEI /IOS :시리얼넘버

            // api 요청 - 단말 인증
            String resultData = mguardApiClient.service(mdmDomain, true, "mdmdi01", "checkRegisteredDevice", requestData, authCookie);

            Toast.makeText(LoginActivity.this, "단말 인증 성공 : " + resultData, Toast.LENGTH_SHORT).show();


            // api 요청 - 정책 변경
            requestData.put("POLICY_CODE", "2"); //1번 정책해지 2번 정책적용 => 로그아웃에서는 1번
            requestData.put("deviceId", "354677061262645");
            requestData.put("systemName", "mcpms");

            String resultData1 = mguardApiClient.service(mdmDomain, true, "mdmWebService", "setPolicyCodeByDeviceId", requestData, authCookie);

        } catch (MguardAuthException e) {
            //재인증
            Log.e(getString(R.string.mguardAuthException), e.getMessage());
        } catch (Exception e) {
            Log.e(getString(R.string.exception), e.getMessage());
        } finally {
            Log.i(getString(R.string.done), "mdmConnection done");
        }
    }


}

