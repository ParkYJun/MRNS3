package com.mrns.login;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mrns.common.BackPressCloseSystem;
import com.mrns.common.MsgDialog;
import com.mrns.common.NetworkMoniter;
import com.mrns.main.MainActivity;
import com.mrns.main.R;
import com.mrns.manager.AppManager;
import com.mrns.utils.BaseActivity;
import com.raonsecure.touchen_mguard_4_0.MDMAPI;
import com.raonsecure.touchen_mguard_4_0.MDMResultCode;

import net.secuwiz.SecuwaySSL.Api.MobileApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SSLLoginActivity extends BaseActivity {

    ProgressDialog ringProgressDialog;
    Handler updateBarHandler;
    boolean bOnline = true;
    TextView ssllogin_btn;
    Intent movePage;
    private EditText sslidView;
    private EditText sslpwView;
    private CheckBox idCkView;
    boolean isSaveCk = false;
    RelativeLayout layoutOnoff;
    String mode = "";

    ImageView mOn;
    ImageView mOff;
    TextView tOn;
    TextView tOff;

    private NetworkMoniter networkMoniter = null;

    //app 설치 시 api 같이 설치 되도록 처리 하기 위한 방법
    String SECUWAYSSL_APK;//설치 apk 의 이름(assets 폴더에 파일이 존재해야 함)
    String SECUWAYSSL_PACKAGE;  //설치될 package 이름
    private final static int SECUWAYSSL_VERSION_CODE = 8;   //현재 버전 코드
    IBinder tempService = null;
    int m_nStatus = 0;

    BackPressCloseSystem backPressCloseSystem;


    // MDM
    private MDMAPI _mdm = null;
    private int initResult = -1;

    String MDM_APK;
    String MDM_URL;
    String MDM_PACKAGE;
    private int MDM_VERSION_CODE;

    private SecuwayServiceConnection mConnection = new SecuwayServiceConnection();
    private BroadcastReceiver mVpnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //TODO: move into IntentFilter, or dispatch from to DaemonEnabler.receive()
            if (intent.getAction().equals("com.secuwiz.SecuwaySSL.Service.STATUS")) {
                m_nStatus = intent.getIntExtra(getString(R.string.key_status), 0);
                if (m_nStatus == 1) {
                    if (ringProgressDialog != null) ringProgressDialog.dismiss();
                    movePage = new Intent(SSLLoginActivity.this, LoginActivity.class);
                    movePage.putExtra(getString(R.string.key_bonline), bOnline);
                    startActivity(movePage);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ssllogin);

        networkMoniter = new NetworkMoniter(this, bOnline, mode);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        layoutOnoff = (RelativeLayout) findViewById(R.id.layoutOnoff);


        MDM_URL = getString(R.string.MDM_URL);
        MDM_PACKAGE = getString(R.string.MDM_PACKAGE);
        MDM_VERSION_CODE = Integer.valueOf(getString(R.string.MDM_VERSIONCODE));

        _mdm = MDMAPI.getInstance();
        AppManager.getInstance().set_mdm(_mdm);     //스와이프 종료시 mdm 종료를 위한 글로벌 변수 셋팅


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

        View.OnClickListener testListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.switchOn:
                    case R.id.textOn:
                        setbOnline(bOnline);
                        break;
                    case R.id.switchOff:
                    case R.id.textOff:
                        setbOnline(bOnline);
                        break;
                }
            }
        };

        mOn.setOnClickListener(testListener);
        mOff.setOnClickListener(testListener);
        tOn.setOnClickListener(testListener);
        tOff.setOnClickListener(testListener);

//        layoutOnoff.setVisibility(View.GONE);

        updateBarHandler = new Handler();

        SECUWAYSSL_APK = getString(R.string.SECUWAYSSL_APK);
        SECUWAYSSL_PACKAGE = getString(R.string.SECUWAYSSL_PACKAGE);

        // Set up the login form.
        sslidView = (EditText) findViewById(R.id.sslvpnid);
        EditText editText = (EditText)findViewById(R.id.sslvpnid);              //대문자
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});      //대문자
        sslpwView = (EditText) findViewById(R.id.sslvpnpw);
        ssllogin_btn = (TextView) findViewById(R.id.sslvpn_button);
        idCkView = (CheckBox) findViewById(R.id.savesslId_ck);
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

        backPressCloseSystem = new BackPressCloseSystem(this);

        SharedPreferences pref = getSharedPreferences(getString(R.string.key_shared_preferences), Activity.MODE_PRIVATE);
        //저장된 값들을 불러옵니다.
        boolean chkID = pref.getBoolean(getString(R.string.key_check_id), false);
        isSaveCk = chkID;
        idCkView.setChecked(chkID);
        if (isSaveCk) {
            String saveId = pref.getString(getString(R.string.key_save_id), "");
            sslidView.setText(saveId);
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sslvpn_button:

                        if (attemptLogin()) {//ip, pw validation
                            launchRingDialog(v);
                            //ssl vpn 접속
                            connectSSLVPN(v);
                        }
                        break;
                }
            }
        };

        ssllogin_btn.setOnClickListener(listener);

        onPermission();     //권한 확인(권한 확인 후 instrall)

        //api가 설치가 되어 있지 않거나 현재 프로젝트에 있는 버전보다 낮은 버전이 설치되어 있으면 api 설치
        if (SECUWAYSSL_VERSION_CODE > getVersionCode()) {
            install();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SSLLoginActivity.this, "SSL VPN을 설치하셔야 합니다. 5초후에 앱 종료 됩니다.", Toast.LENGTH_LONG).show();

                    finish();
                }
            }, 5000);

            return;
        }

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CHECK_PERMISSION) {
//            if (resultCode != RESULT_OK) {
//                finish();
//            }
//        } else if (requestCode == REQUEST_SELECT_RESTRICT) {
//            Map<Restrict.Key, Restrict.Value> controlMap = null;
//            if (resultCode == RESULT_OK) {
//                Bundle bundle = data.getBundleExtra("resultBundle");
//                if (bundle != null) {
//                    Map<String, Integer> map = new HashMap<>();
//                    for (String key : bundle.keySet()) {
//                        int value = bundle.getInt(key, 0);
//                        map.put(key, value);
//                    }
//                    controlMap = Restrict.convertEnumMap(map);
//                }
//            }
//            if (controlMap != null) {
//                _mdm.RS_MDM_ForceRestrict(controlMap, new MDMAPI.MGuardCallbackListener() {
//                    @Override
//                    public void onCompleted(int resultCode, String msg) {
//                        showDialogResultMsg(resultCode, msg);
//                    }
//                });
//            } else {
//                Toast.makeText(this, "REQUEST_SELECT_RESTRICT, canceled", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public static boolean isRunningProcess(Context context, String packageName) {

        boolean isRunning = false;

        ActivityManager actMng = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> list = actMng.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo rap : list) {

            Log.d("DUBUG", "+++++++++++++++" + rap.processName);
        }

        return false;
    }

    public void launchRingDialog(View view) {
        ringProgressDialog = ProgressDialog.show(SSLLoginActivity.this, "잠시만 기다려 주세요 ...", "SSL_VPN 접속중 ...", true);
        ringProgressDialog.setCancelable(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Thread.sleep(10000);
                } catch (Exception e) {
                    ringProgressDialog.dismiss();
                    return;
                }
            }
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();


        // bindService 호출
        // 정상적으로 호출되면 onServiceConnected() 부분 호출됨

        IntentFilter intentFilter = new IntentFilter("com.secuwiz.SecuwaySSL.Service.STATUS");

        registerReceiver(mVpnReceiver, intentFilter);

        Intent intent = new Intent(SECUWAYSSL_PACKAGE);
        intent.setPackage(SECUWAYSSL_PACKAGE);

        if (!bindService(intent, mConnection, BIND_AUTO_CREATE)) {
            System.out.println("bind service error");
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        // UI 상태를 저장합니다.
        SharedPreferences.Editor editor = pref.edit();
        // Editor를 불러옵니다.
        EditText sslidView = (EditText) findViewById(R.id.sslvpnid);
        CheckBox check1 = (CheckBox) findViewById(R.id.savesslId_ck);

        String strInputValue = "";

        if (isSaveCk) {
            // 저장할 값들을 입력합니다.
            strInputValue = sslidView.getText().toString();
        }

        editor.putString(getString(R.string.key_save_id), strInputValue);
        editor.putBoolean(getString(R.string.key_check_id), check1.isChecked());
        editor.apply();

        if (ringProgressDialog != null) {
            ringProgressDialog.dismiss();
        }
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

    @Override
    protected void onPause() {
        super.onPause();
        if (mVpnReceiver != null) {
            unregisterReceiver(mVpnReceiver);
        }
    }

    String strResult;

    class StartRunnable implements Runnable {
        public void run() {
            strResult = "error";

            //bindService 가 정상적으로 되고 onServiceConnected() 까지 호출된 다음 실행
            //서비스에 연결 되었으면 vpn 로그인
            if (tempService != null) {

                MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

                try {
                    //아이디,비밀번호를 이용하여 vpn 시작
                    strResult = objAidl.StartVpn(getString(R.string.sslvpn_addr), sslidView.getText().toString(), sslpwView.getText().toString(), 1);

                    //"0" 이 리턴되면 연결 성공
                    if (strResult == null || !strResult.equals("0")) {
                        //"0" 이 아니면 에러 메시지
                        if (ringProgressDialog != null) ringProgressDialog.dismiss();


                        if (strResult.contains("로그인한")) {

                            updateBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "이미 로그인한 사용자 입니다.\n해당 ID접속 해제 후 다시 접속해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            updateBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), strResult, Toast.LENGTH_SHORT).show();
                                }
                            });
                            sslpwView.requestFocus();
                        }
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
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
                    Log.d(getString(R.string.remoteException), e.getMessage());
                } finally {
                    m_nStatus = 0;
                }
            }
        }
    }

    public void connectSSLVPN(View v) {
        StartRunnable a = new StartRunnable();
        Thread thread = new Thread(a);
        thread.start();
    }

    public void stopSSLVPN() {
        StopRunnable a = new StopRunnable();
        Thread thread = new Thread(a);
        thread.start();
    }

    public int getVpnStatus() {
        //bindService 가 정상적으로 되고 onServiceConnected() 까지 호출된 다음 실행
        //서비스에 연결 되었으면 vpn 상태 체크
        int nStatus = 100;
        if (tempService != null) {
            MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

            try {
                nStatus = objAidl.VpnStatus();
            } catch (RemoteException e) {
                Log.d(getString(R.string.remoteException), e.getMessage());
            }
        }

        //nStatus 0 이면 연결 안됨
        //nStatus 1 이면 연결됨
        //nStatus 2 면 연결 중
        //vpn 상태는 0 에서 시작해서 2를 반복하다가 1로 변하면 연결이 된 상태임
        return nStatus;
    }

    private class SecuwayServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder service) {
            //서비스 바인더  멤버변수로 저장
            tempService = service;
            AppManager.getInstance().setService(service);       //스와이프 종료시 SSLVPN 종료를 위한 글로벌 변수 셋팅
            startService(new Intent(SSLLoginActivity.this, CheckSwipeService.class));
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    }

    private boolean attemptLogin() {

        boolean cancel = false;
        View focusView = null;

        String userid = sslidView.getText().toString();
        String password = sslpwView.getText().toString();

        if (userid.equals("")) {
            sslidView.setError(null);
            sslidView.requestFocus();
        }

        if (password.equals("")) {
            sslpwView.setError(null);
            sslpwView.requestFocus();
        }

        if (TextUtils.isEmpty(password)) {
            sslpwView.setError(getString(R.string.error_sslpw_required));
            focusView = sslpwView;
            cancel = true;
        }

        if (TextUtils.isEmpty(userid)) {
            sslidView.setError(getString(R.string.error_sslid_required));
            focusView = sslidView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt
            return true;
        }
    }

    //api 파일을 설치하기 위한 처리 방법
    private void install() {
        try {
            InputStream is = getAssets().open(SECUWAYSSL_APK);
            FileOutputStream fos = null;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                fos = openFileOutput(SECUWAYSSL_APK, Context.MODE_PRIVATE);
            } else {
                fos = openFileOutput(SECUWAYSSL_APK, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
            }

            byte[] buffer = new byte[1024];
            int byteRead;

            while ((byteRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteRead);
            }

            fos.flush();
            fos.close();
            is.close();

            File apk = getFileStreamPath(SECUWAYSSL_APK);
            if (apk.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = null;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(SSLLoginActivity.this, getApplicationContext().getPackageName() + ".provider.MGuardSdkFileProvider", apk);
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

    //설치된 api 의 version code 확인
    public long getVersionCode() {
        long versionCode = 0;

        try {
            if(Build.VERSION.SDK_INT < 28)
                versionCode = getPackageManager().getPackageInfo(SECUWAYSSL_PACKAGE, 0).versionCode;
            else{
                versionCode = getPackageManager().getPackageInfo(SECUWAYSSL_PACKAGE, 0).getLongVersionCode();
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(SSLLoginActivity.this);

                builder.setMessage("프로그램을 종료 하시겠습니까?");
                builder.setTitle("");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        agentClose();
                        stopSSLVPN();
                        if (mVpnReceiver != null) {
                            unregisterReceiver(mVpnReceiver);
                            mVpnReceiver = null;
                        }

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


    public void setbOnline(boolean bflag) {

        AlertDialog.Builder builder = new AlertDialog.Builder(SSLLoginActivity.this);

        if (bflag) {
            builder.setMessage("오프라인 모드로 변경하시겠습니까?");
            builder.setTitle("");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mOn.setVisibility(View.GONE);
                    tOn.setVisibility(View.GONE);
                    mOff.setVisibility(View.VISIBLE);
                    tOff.setVisibility(View.VISIBLE);
                    bOnline = false;

                    movePage = new Intent(SSLLoginActivity.this, MainActivity.class);
                    movePage.putExtra(getString(R.string.key_bonline), bOnline);
                    movePage.putExtra(getString(R.string.key_mode), "beoff");
                    startActivity(movePage);
                }
            });

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.create().show();
        } else {
            mOn.setVisibility(View.VISIBLE);
            tOn.setVisibility(View.VISIBLE);
            mOff.setVisibility(View.GONE);
            tOff.setVisibility(View.GONE);
            bOnline = true;
        }

    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean isKill = intent.getBooleanExtra("KILL_ACT", false);
        if (isKill)
            close();
    }

    private void close() {
        finish();
        int nSDKVersion = Integer.parseInt(Build.VERSION.SDK);
        if (nSDKVersion < 8)    //2.1이하
        {
            ActivityManager actMng = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            actMng.restartPackage(getPackageName());
        } else {
            new Thread(new Runnable() {
                public void run() {
                    ActivityManager actMng = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    String strProcessName = getApplicationInfo().processName;

                    List<ActivityManager.RunningAppProcessInfo> list = actMng.getRunningAppProcesses();
                    for (ActivityManager.RunningAppProcessInfo rap : list) {
                        if (rap.processName.equals(strProcessName)) {
                            if (rap.importance >= ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND)
                                actMng.restartPackage(getPackageName());
                            Thread.yield();
                            break;
                        }
                    }

                }
            }, "Process Killer").start();
        }
    }

    private void onPermission() {

        TedPermission.with(this)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        //api가 설치가 되어 있지 않거나 현재 프로젝트에 있는 버전보다 낮은 버전이 설치되어 있으면 api 설치
                        initSDK();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        // Toast.makeText(ExternalMemoryActivity.this, "권한거부.\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setDeniedMessage("모든 권한을 허용하셔야 사용할 수 있습니다.\n[설정] > [권한]에서 권한을 다시 허용할 수 있습니다.")
                .setPermissions(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.GET_ACCOUNTS,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.ACCESS_NETWORK_STATE,
                        android.Manifest.permission.READ_CONTACTS,
                        android.Manifest.permission.READ_PHONE_STATE,
                        android.Manifest.permission.CAMERA
                )
                .check();

    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // MDM
    private void initSDK() {

        _mdm.RS_MDM_Init(this, MDM_URL, new MDMAPI.MGuardConnectionListener() {
            @Override
            public void onComplete(int resultCode) {
                initResult = resultCode;
                switch (resultCode) {
                    case MDMResultCode.ERR_MDM_SUCCESS: //연결 성공
                        //바인드되면 파이도 패턴조회 함
                        Toast.makeText(SSLLoginActivity.this, "연결성공", Toast.LENGTH_SHORT).show();
                        mdmCheckAgent();
                        break;
                    case MDMResultCode.ERR_MDM_FAILED: //실패
                        Toast.makeText(SSLLoginActivity.this, "연결에 실패하였습니다. 다시 실행해주세요", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case MDMResultCode.ERR_MDM_NOT_INSTALLED: //설치가 안되어있음
                        Toast.makeText(SSLLoginActivity.this, "oneGuard 설치가 되어 있지 않습니다. 설치를 진행하겠습니다.", Toast.LENGTH_SHORT).show();
                        agentNotInstalled();
                        break;
                    default:
//                            showDialogResultMsg(resultCode, null);
                        break;
                }
            }
        }, MDM_PACKAGE);
    }

    // mdm 위 변조 확인 / 최신버전 확인
    private void mdmCheckAgent() {

        _mdm.RS_MDM_CheckAgent(this, new MDMAPI.MGuardCallbackListener() {
            @Override
            public void onCompleted(int resultCode, String msg) {
                Log.d("MNRN", "checkagent...resultCode: " + resultCode + ", onCompleted :" + msg);

                if (resultCode == MDMResultCode.ERR_MDM_NOT_INSTALLED) { //설치되어있지 않습니다.
                    agentNotInstalled();
                } else if (resultCode == MDMResultCode.ERR_MDM_NOT_LOGIN) { // 로그인 안되어있을 경우 forgroud 전환(oneGuard 창)
                    Toast.makeText(SSLLoginActivity.this, "로그인이 되어있지 않습니다.", Toast.LENGTH_SHORT).show();
                    agentNotLogin(msg);
                } else if (resultCode == MDMResultCode.ERR_MDM_DEVICE_ROOTING) {
                    Toast.makeText(SSLLoginActivity.this, "루팅된 단말입니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (resultCode == MDMResultCode.ERR_MDM_DEVICE_UNREGISTER) {
                    Toast.makeText(SSLLoginActivity.this, "MDM 서버에 등록되지 않은 단말입니다.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (resultCode == MDMResultCode.ERR_MDM_AGENT_NOT_LAST_VERSION) { //최신버전 체크 아닐경우 forgroud 전환(oneGuard 창)
                    agentNotInstalled();
                } else if (resultCode == MDMResultCode.ERR_MDM_FIND_HACKED_APP) {
                    Toast.makeText(SSLLoginActivity.this, "에이전트 위조/변조 되었음.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (resultCode == MDMResultCode.ERR_MDM_SUCCESS) {
                    Toast.makeText(SSLLoginActivity.this, "상태 확인이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    agentCheckSuccess();
                } else {
                    Log.d("MNRN", "resultCode ?????  :" + resultCode);
                    Toast.makeText(SSLLoginActivity.this, "에러가 발생했습니다." + resultCode, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //로그인이 안되어있는 경우 forgroud로 원가드 전환
    private void agentNotLogin(String msg) {
        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
            @Override
            public void run() {
                _mdm.RS_MDM_GOMDMLogin(SSLLoginActivity.this);
            }
        }, 1000);

    }

    //에이전트 체크 성공 업무 로그인 수행
    private void agentCheckSuccess() {
        _mdm.RS_MDM_LoginOffice(new MDMAPI.MGuardCallbackListener() {
            @Override
            public void onCompleted(int resultCode, String msg) {
                Log.d("MNRN", "officeLogin...onCompleted resultCode:" + resultCode + ", msg: " + msg);
                if (resultCode == MDMResultCode.ERR_MDM_SUCCESS) {
                    Log.d("MNRN", "officeLogin...onCompleted resultCode:" + resultCode + ", msg: 성공하였습니다");
                    Toast.makeText(SSLLoginActivity.this, "모바일 장치관리 상태 확인", Toast.LENGTH_SHORT).show();
                    checkVPNStatus();
                } else if (resultCode == MDMResultCode.ERR_MDM_ALLEADY_OFFICE_MODE) {
                    Log.d("MNRN", "officeLogin...onCompleted resultCode:" + resultCode + ", msg: 이미 업무앱 로그인 상태입니다.");
                    Toast.makeText(SSLLoginActivity.this, "모바일 장치관리 상태 확인", Toast.LENGTH_SHORT).show();
                    checkVPNStatus();       //업무앱 로그인 성공시 다른 앱에서 SSLVPN이 로그인 되어있다면 OTP 로그인 화면으로 넘어가도록.
                } else {
                    Log.d("MNRN", "officeLogin...onCompleted resultCode:" + resultCode + ", msg: 실패하였습니다");
                    Toast.makeText(SSLLoginActivity.this, "실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //설치가 안되어있는경우 설치 프로그램 진행
    private void agentNotInstalled() {
        final MsgDialog msgDialog = new MsgDialog(this);

        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
            @Override
            public void run() {
                // 실행할 동작 코딩
                msgDialog.setTitle("OneGuard 미 설치");
                msgDialog.setMessage("OneGuard 설치를 하시겠습니까?\n" + "확인버튼 클릭시 설치됩니다");
                msgDialog.setPositiveButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        _mdm.RS_MDM_InstallAgent(SSLLoginActivity.this, new MDMAPI.FileDownloadProgressListener() {
                            @Override
                            public void onCompleted() {
                                Log.d("MNRN", "installAgent...onCompleted");
                            }

                            @Override
                            public void onFailed() {
                                Log.d("MNRN", "installAgent...onFailed");
                            }
                        });
                    }

                });
                msgDialog.setNegativeButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        msgDialog.dismiss();

                        Toast.makeText(SSLLoginActivity.this, "MDM 보안 프로그램을 설치해주시기 바랍니다.\n3초후 프로그램이 종료됩니다.", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {// 1 초 후에 실행
                            @Override
                            public void run() {
                                moveTaskToBack(true);
                                finish();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(0);
                            }
                        }, 3000);

                    }
                });
                msgDialog.show();
            }
        }, 1000);
    }

    private void agentClose(){
        _mdm.RS_MDM_LogoutOffice(new MDMAPI.MGuardCallbackListener()
        {
            @Override
            public void onCompleted ( int resultCode, String msg){
                Log.d("MNRN", "officeLogout...onCompleted resultCode:" + resultCode + ", msg: " + msg);
                if (resultCode == MDMResultCode.ERR_MDM_ALLEADY_PERSONAL_MODE) {
                    Toast.makeText(SSLLoginActivity.this, "이미 업무앱 로그아웃 상태입니다.", Toast.LENGTH_SHORT).show();
                } else if (resultCode == MDMResultCode.ERR_MDM_SUCCESS) {
                    Toast.makeText(SSLLoginActivity.this, "업무앱 로그아웃 성공.", Toast.LENGTH_SHORT).show();
                } else if (resultCode == MDMResultCode.ERR_MDM_NOT_LOGIN) {
                    agentNotLogin(msg);
                } else {
                    Toast.makeText(SSLLoginActivity.this, "에러가 발생했습니다." + resultCode, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkVPNStatus(){
        if(getVpnStatus() == 1){
            movePage = new Intent(SSLLoginActivity.this, LoginActivity.class);
            movePage.putExtra(getString(R.string.key_bonline), bOnline);
            startActivity(movePage);
        }
    }
}
