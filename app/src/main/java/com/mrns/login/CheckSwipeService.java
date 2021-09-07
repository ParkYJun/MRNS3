//스와이프로 종료
package com.mrns.login;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.mrns.main.R;
import com.mrns.manager.AppManager;
import com.raonsecure.touchen_mguard_4_0.MDMAPI;
import com.raonsecure.touchen_mguard_4_0.MDMResultCode;

import net.secuwiz.SecuwaySSL.Api.MobileApi;


public class CheckSwipeService extends Service {
    int m_nStatus = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        // TODO: release
        stopSSLVPN();       //SSLVPN 종료
        agentClose();       //업무앱 종료
        stopSelf();
    }

    public void stopSSLVPN(){
        StopRunnable a = new StopRunnable();
        Thread thread = new Thread(a);
        thread.start();
    }


    class StopRunnable implements Runnable {
        public void run()
        {
            //bindService 가 정상적으로 되고 onServiceConnected() 까지 호출된 다음 실행
            //서비스에 연결 되었으면 vpn 로그아웃
            IBinder service = AppManager.getInstance().getService();
            if(service != null)
            {
                MobileApi objAidl = MobileApi.Stub.asInterface(service);
                try {

                    objAidl.StopVpn();
                } catch (RemoteException e) {
                    Log.d(getString(R.string.remoteException), e.getMessage());
                }finally {
                    m_nStatus = 0;
                }
            }
        }
    }

    private void agentClose(){
        MDMAPI _mdm = AppManager.getInstance().get_mdm();
        _mdm.RS_MDM_LogoutOffice(true, new MDMAPI.MGuardCallbackListener() {
            @Override
            public void onCompleted(int resultCode, String msg) {
                Log.d("MNRN", "officeLogout...onCompleted resultCode:" + resultCode + ", msg: " + msg);
                if (resultCode == MDMResultCode.ERR_MDM_ALLEADY_PERSONAL_MODE) {
                    Toast.makeText(CheckSwipeService.this, "이미 업무앱 로그아웃 상태입니다.", Toast.LENGTH_SHORT).show();
                } else if (resultCode == MDMResultCode.ERR_MDM_SUCCESS) {
                    Toast.makeText(CheckSwipeService.this, "업무앱 로그아웃 성공.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckSwipeService.this, "msg", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
