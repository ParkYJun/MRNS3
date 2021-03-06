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

    //app ?????? ??? api ?????? ?????? ????????? ?????? ?????? ?????? ??????
    String SECUWAYSSL_APK;//?????? apk ??? ??????(assets ????????? ????????? ???????????? ???)
    String SECUWAYSSL_PACKAGE;  //????????? package ??????
    private final static int SECUWAYSSL_VERSION_CODE = 8;   //?????? ?????? ??????
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
        AppManager.getInstance().set_mdm(_mdm);     //???????????? ????????? mdm ????????? ?????? ????????? ?????? ??????


        //????????? ??????
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
        EditText editText = (EditText)findViewById(R.id.sslvpnid);              //?????????
        editText.setFilters(new InputFilter[]{new InputFilter.AllCaps()});      //?????????
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
        //????????? ????????? ???????????????.
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
                            //ssl vpn ??????
                            connectSSLVPN(v);
                        }
                        break;
                }
            }
        };

        ssllogin_btn.setOnClickListener(listener);

        onPermission();     //?????? ??????(?????? ?????? ??? instrall)

        //api??? ????????? ?????? ?????? ????????? ?????? ??????????????? ?????? ???????????? ?????? ????????? ???????????? ????????? api ??????
        if (SECUWAYSSL_VERSION_CODE > getVersionCode()) {
            install();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SSLLoginActivity.this, "SSL VPN??? ??????????????? ?????????. 5????????? ??? ?????? ?????????.", Toast.LENGTH_LONG).show();

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
        ringProgressDialog = ProgressDialog.show(SSLLoginActivity.this, "????????? ????????? ????????? ...", "SSL_VPN ????????? ...", true);
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


        // bindService ??????
        // ??????????????? ???????????? onServiceConnected() ?????? ?????????

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
        // UI ????????? ???????????????.
        SharedPreferences.Editor editor = pref.edit();
        // Editor??? ???????????????.
        EditText sslidView = (EditText) findViewById(R.id.sslvpnid);
        CheckBox check1 = (CheckBox) findViewById(R.id.savesslId_ck);

        String strInputValue = "";

        if (isSaveCk) {
            // ????????? ????????? ???????????????.
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

            //bindService ??? ??????????????? ?????? onServiceConnected() ?????? ????????? ?????? ??????
            //???????????? ?????? ???????????? vpn ?????????
            if (tempService != null) {

                MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

                try {
                    //?????????,??????????????? ???????????? vpn ??????
                    strResult = objAidl.StartVpn(getString(R.string.sslvpn_addr), sslidView.getText().toString(), sslpwView.getText().toString(), 1);

                    //"0" ??? ???????????? ?????? ??????
                    if (strResult == null || !strResult.equals("0")) {
                        //"0" ??? ????????? ?????? ?????????
                        if (ringProgressDialog != null) ringProgressDialog.dismiss();


                        if (strResult.contains("????????????")) {

                            updateBarHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), "?????? ???????????? ????????? ?????????.\n?????? ID?????? ?????? ??? ?????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
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
            //bindService ??? ??????????????? ?????? onServiceConnected() ?????? ????????? ?????? ??????
            //???????????? ?????? ???????????? vpn ????????????
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
        //bindService ??? ??????????????? ?????? onServiceConnected() ?????? ????????? ?????? ??????
        //???????????? ?????? ???????????? vpn ?????? ??????
        int nStatus = 100;
        if (tempService != null) {
            MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

            try {
                nStatus = objAidl.VpnStatus();
            } catch (RemoteException e) {
                Log.d(getString(R.string.remoteException), e.getMessage());
            }
        }

        //nStatus 0 ?????? ?????? ??????
        //nStatus 1 ?????? ?????????
        //nStatus 2 ??? ?????? ???
        //vpn ????????? 0 ?????? ???????????? 2??? ??????????????? 1??? ????????? ????????? ??? ?????????
        return nStatus;
    }

    private class SecuwayServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder service) {
            //????????? ?????????  ??????????????? ??????
            tempService = service;
            AppManager.getInstance().setService(service);       //???????????? ????????? SSLVPN ????????? ?????? ????????? ?????? ??????
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

    //api ????????? ???????????? ?????? ?????? ??????
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

    //????????? api ??? version code ??????
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

                builder.setMessage("??????????????? ?????? ???????????????????");
                builder.setTitle("");
                builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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

                builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
            builder.setMessage("???????????? ????????? ?????????????????????????");
            builder.setTitle("");
            builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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

            builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
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
        if (nSDKVersion < 8)    //2.1??????
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
                        //api??? ????????? ?????? ?????? ????????? ?????? ??????????????? ?????? ???????????? ?????? ????????? ???????????? ????????? api ??????
                        initSDK();
                    }

                    @Override
                    public void onPermissionDenied(List<String> deniedPermissions) {
                        // Toast.makeText(ExternalMemoryActivity.this, "????????????.\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setDeniedMessage("?????? ????????? ??????????????? ????????? ??? ????????????.\n[??????] > [??????]?????? ????????? ?????? ????????? ??? ????????????.")
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
                    case MDMResultCode.ERR_MDM_SUCCESS: //?????? ??????
                        //??????????????? ????????? ???????????? ???
                        Toast.makeText(SSLLoginActivity.this, "????????????", Toast.LENGTH_SHORT).show();
                        mdmCheckAgent();
                        break;
                    case MDMResultCode.ERR_MDM_FAILED: //??????
                        Toast.makeText(SSLLoginActivity.this, "????????? ?????????????????????. ?????? ??????????????????", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case MDMResultCode.ERR_MDM_NOT_INSTALLED: //????????? ???????????????
                        Toast.makeText(SSLLoginActivity.this, "oneGuard ????????? ?????? ?????? ????????????. ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                        agentNotInstalled();
                        break;
                    default:
//                            showDialogResultMsg(resultCode, null);
                        break;
                }
            }
        }, MDM_PACKAGE);
    }

    // mdm ??? ?????? ?????? / ???????????? ??????
    private void mdmCheckAgent() {

        _mdm.RS_MDM_CheckAgent(this, new MDMAPI.MGuardCallbackListener() {
            @Override
            public void onCompleted(int resultCode, String msg) {
                Log.d("MNRN", "checkagent...resultCode: " + resultCode + ", onCompleted :" + msg);

                if (resultCode == MDMResultCode.ERR_MDM_NOT_INSTALLED) { //?????????????????? ????????????.
                    agentNotInstalled();
                } else if (resultCode == MDMResultCode.ERR_MDM_NOT_LOGIN) { // ????????? ??????????????? ?????? forgroud ??????(oneGuard ???)
                    Toast.makeText(SSLLoginActivity.this, "???????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                    agentNotLogin(msg);
                } else if (resultCode == MDMResultCode.ERR_MDM_DEVICE_ROOTING) {
                    Toast.makeText(SSLLoginActivity.this, "????????? ???????????????.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (resultCode == MDMResultCode.ERR_MDM_DEVICE_UNREGISTER) {
                    Toast.makeText(SSLLoginActivity.this, "MDM ????????? ???????????? ?????? ???????????????.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (resultCode == MDMResultCode.ERR_MDM_AGENT_NOT_LAST_VERSION) { //???????????? ?????? ???????????? forgroud ??????(oneGuard ???)
                    agentNotInstalled();
                } else if (resultCode == MDMResultCode.ERR_MDM_FIND_HACKED_APP) {
                    Toast.makeText(SSLLoginActivity.this, "???????????? ??????/?????? ?????????.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (resultCode == MDMResultCode.ERR_MDM_SUCCESS) {
                    Toast.makeText(SSLLoginActivity.this, "?????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    agentCheckSuccess();
                } else {
                    Log.d("MNRN", "resultCode ?????  :" + resultCode);
                    Toast.makeText(SSLLoginActivity.this, "????????? ??????????????????." + resultCode, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    //???????????? ??????????????? ?????? forgroud??? ????????? ??????
    private void agentNotLogin(String msg) {
        new Handler().postDelayed(new Runnable() {// 1 ??? ?????? ??????
            @Override
            public void run() {
                _mdm.RS_MDM_GOMDMLogin(SSLLoginActivity.this);
            }
        }, 1000);

    }

    //???????????? ?????? ?????? ?????? ????????? ??????
    private void agentCheckSuccess() {
        _mdm.RS_MDM_LoginOffice(new MDMAPI.MGuardCallbackListener() {
            @Override
            public void onCompleted(int resultCode, String msg) {
                Log.d("MNRN", "officeLogin...onCompleted resultCode:" + resultCode + ", msg: " + msg);
                if (resultCode == MDMResultCode.ERR_MDM_SUCCESS) {
                    Log.d("MNRN", "officeLogin...onCompleted resultCode:" + resultCode + ", msg: ?????????????????????");
                    Toast.makeText(SSLLoginActivity.this, "????????? ???????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    checkVPNStatus();
                } else if (resultCode == MDMResultCode.ERR_MDM_ALLEADY_OFFICE_MODE) {
                    Log.d("MNRN", "officeLogin...onCompleted resultCode:" + resultCode + ", msg: ?????? ????????? ????????? ???????????????.");
                    Toast.makeText(SSLLoginActivity.this, "????????? ???????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    checkVPNStatus();       //????????? ????????? ????????? ?????? ????????? SSLVPN??? ????????? ??????????????? OTP ????????? ???????????? ???????????????.
                } else {
                    Log.d("MNRN", "officeLogin...onCompleted resultCode:" + resultCode + ", msg: ?????????????????????");
                    Toast.makeText(SSLLoginActivity.this, "?????????????????????.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //????????? ????????????????????? ?????? ???????????? ??????
    private void agentNotInstalled() {
        final MsgDialog msgDialog = new MsgDialog(this);

        new Handler().postDelayed(new Runnable() {// 1 ??? ?????? ??????
            @Override
            public void run() {
                // ????????? ?????? ??????
                msgDialog.setTitle("OneGuard ??? ??????");
                msgDialog.setMessage("OneGuard ????????? ???????????????????\n" + "???????????? ????????? ???????????????");
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

                        Toast.makeText(SSLLoginActivity.this, "MDM ?????? ??????????????? ?????????????????? ????????????.\n3?????? ??????????????? ???????????????.", Toast.LENGTH_SHORT).show();

                        new Handler().postDelayed(new Runnable() {// 1 ??? ?????? ??????
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
                    Toast.makeText(SSLLoginActivity.this, "?????? ????????? ???????????? ???????????????.", Toast.LENGTH_SHORT).show();
                } else if (resultCode == MDMResultCode.ERR_MDM_SUCCESS) {
                    Toast.makeText(SSLLoginActivity.this, "????????? ???????????? ??????.", Toast.LENGTH_SHORT).show();
                } else if (resultCode == MDMResultCode.ERR_MDM_NOT_LOGIN) {
                    agentNotLogin(msg);
                } else {
                    Toast.makeText(SSLLoginActivity.this, "????????? ??????????????????." + resultCode, Toast.LENGTH_SHORT).show();
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
