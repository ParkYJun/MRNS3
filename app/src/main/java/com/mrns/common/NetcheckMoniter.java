package com.mrns.common;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.mrns.main.MainActivity;

public class NetcheckMoniter  extends BroadcastReceiver {

    Context context;
    boolean bOnline;
    private ConnectivityManager m_ConnManager = null;

    public NetcheckMoniter(Context context)
    {
        this.context = context;
        m_ConnManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void onReceive(final Context context, Intent intent)
    {
        if(m_ConnManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null || m_ConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null){
            if(!m_ConnManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected() && !m_ConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()){
//                ! temp_kkh 와이파이 연결시 connect로 설정
//                if (!m_ConnManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
                    bOnline = false;
                    //Toast.makeText(context, "Connected", Toast.LENGTH_SHORT);
                    offDisplayAlert(" 연결이 끊겼습니다!\n오프라인 모드로 변경합니다.");
//                }
            }//end isConnected
        }
    }

    private void offDisplayAlert(String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("네트워크 상태변경 알림");
        builder.setMessage(msg).setCancelable(
                false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent movePage = new Intent(context, MainActivity.class);
                        movePage.putExtra(context.getString(com.mrns.main.R.string.key_bonline), bOnline);
                        movePage.putExtra(context.getString(com.mrns.main.R.string.key_mode), "receive");
                        context.startActivity(movePage);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
