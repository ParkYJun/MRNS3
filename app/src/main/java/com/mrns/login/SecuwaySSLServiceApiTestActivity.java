package com.mrns.login;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mrns.main.R;

import net.secuwiz.SecuwaySSL.Api.MobileApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SecuwaySSLServiceApiTestActivity extends AppCompatActivity {

    //app 설치 시 api 같이 설치 되도록 처리 하기 위한 방법
    String SECUWAYSSL_APK;//설치 apk 의 이름(assets 폴더에 파일이 존재해야 함)
    String SECUWAYSSL_PACKAGE;  //설치될 package 이름
    private final static int SECUWAYSSL_VERSION_CODE = 1;   //현재 버전 코드

    private EditText editAddr, editId, editPwd;

    private String strAddr, strId, strPwd;


    IBinder tempService = null;

    int m_nStatus = 0;

    private SecuwayServiceConnection mConnection = new SecuwayServiceConnection();


    private BroadcastReceiver mVpnReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent)
        {
            //TODO: move into IntentFilter, or dispatch from to DaemonEnabler.receive()
            if(intent.getAction().equals("com.secuwiz.SecuwaySSL.Service.STATUS"))
            {
                System.out.println("VPN status onReceive : " + intent.getIntExtra(getString(R.string.key_status), 0));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sslvpntest);

        SECUWAYSSL_APK = getString(R.string.SECUWAYSSL_APK);
        SECUWAYSSL_PACKAGE = getString(R.string.SECUWAYSSL_PACKAGE);

        //api가 설치가 되어 있지 않거나 현재 프로젝트에 있는 버전보다 낮은 버전이 설치되어 있으면 api 설치
        if(SECUWAYSSL_VERSION_CODE > getVersionCode())
        {
            install();
        }

        editAddr = (EditText)findViewById(R.id.addrEdit);
        editId = (EditText)findViewById(R.id.useridEdit);
        editPwd = (EditText)findViewById(R.id.userpwEdit);

        SharedPreferences pref = getSharedPreferences("PrefTest", 0);
        strAddr = pref.getString("VPNADDRESS", "");
        strId = pref.getString("VPNUSERID", "");
        strPwd = pref.getString("VPNUSERPWD", "");

        editAddr.setText(strAddr);
        editId.setText(strId);
        editPwd.setText(strPwd);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try
        {
            unbindService(mConnection);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent("net.secuwiz.SecuwaySSL.mobileservice");
        intent.setPackage("net.secuwiz.SecuwaySSL.mobileservice");

        // bindService 호출
        // 정상적으로 호출되면 onServiceConnected() 부분 호출됨
        if(!bindService(intent, mConnection,BIND_AUTO_CREATE))
        {
            System.out.println("bind service error");
        }

        IntentFilter intentFilter = new IntentFilter("com.secuwiz.SecuwaySSL.Service.STATUS");
        registerReceiver(mVpnReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(mVpnReceiver);
    }

    class StartRunnable implements Runnable {
        public void run()
        {
            SharedPreferences pref = getSharedPreferences("PrefTest", 0);
            SharedPreferences.Editor edit = pref.edit();

            edit.putString("VPNADDRESS", editAddr.getText().toString());
            edit.putString("VPNUSERID", editId.getText().toString());
            edit.putString("VPNUSERPWD", editPwd.getText().toString());

            edit.apply();

            String strResult;

            //bindService 가 정상적으로 되고 onServiceConnected() 까지 호출된 다음 실행
            //서비스에 연결 되었으면 vpn 로그인
            if(tempService != null)
            {

                MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

                try {
                    //아이디,비밀번호를 이용하여 vpn 시작
//	    			strResult = objAidl.StartVpn(editAddr.getText().toString(), editId.getText().toString(), editPwd.getText().toString(), 1);
                    strResult = objAidl.StartVpn(getString(R.string.sslvpn_addr), getString(R.string.sslid), getString(R.string.sslpw), 1);

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class StopRunnable implements Runnable {
        public void run()
        {
            //bindService 가 정상적으로 되고 onServiceConnected() 까지 호출된 다음 실행
            //서비스에 연결 되었으면 vpn 로그아웃
            if(tempService != null)
            {
                MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

                try {

                    objAidl.StopVpn();
                } catch (RemoteException e) {
                    e.getStackTrace();
                }
            }
        }
    }

    public void handleClick(View v){
        StartRunnable a = new StartRunnable();
        Thread thread = new Thread(a);
        thread.start();
    }

    public void handleClick2(View v){
        StopRunnable a = new StopRunnable();
        Thread thread = new Thread(a);
        thread.start();
    }

    public void handleClick3(View v){
        //bindService 가 정상적으로 되고 onServiceConnected() 까지 호출된 다음 실행
        //서비스에 연결 되었으면 vpn 상태 체크
        if(tempService != null)
        {
            MobileApi objAidl = MobileApi.Stub.asInterface(tempService);

            try {
                objAidl.VpnStatus();
            } catch (RemoteException e) {
                Log.e(getString(R.string.remoteException), e.getMessage());
            } finally {
                Log.i(getString(R.string.done), "handleClick3 done");
            }
        }

        //nStatus 0 이면 연결 안됨
        //nStatus 1 이면 연결됨
        //nStatus 2 면 연결 중
        //vpn 상태는 0 에서 시작해서 2를 반복하다가 1로 변하면 연결이 된 상태임
    }

    private class SecuwayServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder service) {
            //서비스 바인더  멤버변수로 저장
            tempService = service;
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    }

    //api 파일을 설치하기 위한 처리 방법
    private void install()
    {
        try
        {
            InputStream is = getAssets().open(SECUWAYSSL_APK);
            FileOutputStream fos = null;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fos = openFileOutput(SECUWAYSSL_APK, Context.MODE_PRIVATE);
            } else {
                fos = openFileOutput(SECUWAYSSL_APK, Context.MODE_WORLD_READABLE| Context.MODE_WORLD_WRITEABLE);
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

            File apk = getFileStreamPath(SECUWAYSSL_APK);
            if (apk.exists()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(SecuwaySSLServiceApiTestActivity.this, getApplicationContext().getPackageName() + ".fileprovider", apk);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    uri = Uri.fromFile(apk);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                intent.setDataAndType(uri, "application/vnd.android.package-archive");

                startActivity(intent);
            }
        }
        catch(IOException e)
        {
            Log.e(getString(R.string.iOException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "install done");
        }
    }

    //설치된 api 의 version code 확인
    public int getVersionCode()
    {
        int versionCode = 0;

        try
        {
            versionCode = getPackageManager().getPackageInfo(SECUWAYSSL_PACKAGE, 0).versionCode;
        }
        catch(PackageManager.NameNotFoundException e) {
            Log.e(getString(R.string.nameNotFoundException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "getVersionCode done");
        }

        return versionCode;
    }
}
