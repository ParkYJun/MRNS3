package com.mrns.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mrns.board.BoardlistActivity;
import com.mrns.board.QnalistActivity;
import com.mrns.common.NetworkMoniter;
import com.mrns.downdata.DownActivity;
import com.mrns.invest.util.InsertUtil;
import com.mrns.login.SSLLoginActivity;
import com.mrns.map.MapWebActivity;
import com.mrns.sqllite.DBHelper;
import com.mrns.sqllite.DatabaseManager;
import com.mrns.updata.UploadActivity;
import com.mrns.utils.BaseActivity;

import net.secuwiz.SecuwaySSL.Api.MobileApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity {

    boolean bOnline;

    ImageView mOn;
    ImageView mOff;
    TextView tOn;
    TextView tOff;

    ImageView mInvest;
    ImageView mMap;
    ImageView mDown;
    ImageView mUpload;

    TextView oneText;
    TextView threeText;
    TextView fourText;

    ImageView bIcon;
    ImageView qIcon;

    TextView mBoardL;
    ImageView mBoardL1;
    TextView mBoardD;
    TextView mQnaL;
    ImageView mQnaL1;
    TextView mQnaD;

    private Intent movePage;
    private SecuwayServiceConnection mConnection = new SecuwayServiceConnection();
    IBinder tempService = null;

    String mainDataUrl;
    int Board_seq;
    int QnA_seq;
    String mode;
    SQLiteDatabase db;
    DBHelper helper;
    LinearLayout board_layer;

    private NetworkMoniter networkMoniter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        bOnline = intent.getBooleanExtra(getString(R.string.key_bonline), bOnline);
        mode = intent.getStringExtra(getString(R.string.key_mode));
        oneText = (TextView) findViewById(R.id.one_text);
        threeText = (TextView) findViewById(R.id.three_text);
        fourText = (TextView) findViewById(R.id.four_text);
        bIcon = (ImageView) findViewById(R.id.board_icon);
        qIcon = (ImageView) findViewById(R.id.qna_icon);
        board_layer = (LinearLayout) findViewById(R.id.board_layer);
        //Network 상태 리시버 등록
        networkMoniter = new NetworkMoniter(this, bOnline, mode);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        SharedPreferences prefSession = getApplicationContext().getSharedPreferences("preSession", Activity.MODE_PRIVATE);
        if(!bOnline) {

            if(prefSession.getString("usid", "").equals("")) {

                /*
                 * 1. db 파일 확인
                 */
                String dbName = getString(R.string.db_user_full_name);
                File sysDb = new File(dbName);

                /*
                 * 2. 사용자 정보 확인 & 입력
                 */
                if(sysDb.exists()){
                    helper = new DBHelper(MainActivity.this, dbName, null, 1);
                    DatabaseManager.initializeInstance(helper);
                    db = DatabaseManager.getInstance().openDatabase();

                    String sql = "SELECT USID, USER_NM, CLSF_CLCD, DTY_CLCD, JBGP_CLCD, DEPT_CODE FROM ST_USER;";
                    Cursor result = db.rawQuery(sql, null);
                    int listCount = result.getCount();

                    SharedPreferences.Editor editor = prefSession.edit();

                    while (result.moveToNext()){
                        editor.putString("usid", result.getString(0));
                        editor.putString("user_nm", result.getString(1));
                        editor.putString("clsf_clcd", result.getString(2));
                        editor.putString("dty_clcd", result.getString(3));
                        editor.putString("jbgp_clcd", result.getString(4));
                        editor.putString("dept_code", result.getString(5));
                    }//end while

                    editor.apply();
                    result.close();

                }else{
                    Toast.makeText(MainActivity.this, "로그인을 한번이라도 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                    Intent movePage = new Intent(MainActivity.this, SSLLoginActivity.class);
                    movePage.putExtra(getString(R.string.key_mode), "first");
                    startActivity(movePage);
                }

            }

        }
        //스위치 세팅
        mOn = (ImageView) findViewById(R.id.switchOn);
        mOff = (ImageView) findViewById(R.id.switchOff);
        tOn = (TextView) findViewById(R.id.textOn);
        tOff = (TextView) findViewById(R.id.textOff);

        if(bOnline){
            mOff.setVisibility(View.GONE);
            tOff.setVisibility(View.GONE);
        }else{
            mOn.setVisibility(View.GONE);
            tOn.setVisibility(View.GONE);
        }

        mInvest = (ImageView) findViewById(R.id.invest_button);
        mMap = (ImageView) findViewById(R.id.map_button);
        mDown = (ImageView) findViewById(R.id.down_button);
        mUpload = (ImageView) findViewById(R.id.upload_button);

        mBoardL = (TextView) findViewById(R.id.boardList_button);
        mQnaL = (TextView) findViewById(R.id.qnalist_button);

        //클릭 리스너 설정
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            switch (v.getId()){
                case R.id.switchOn :
                case R.id.textOn :
                    setbOnline(bOnline);
                    break;
                case R.id.switchOff :
                case R.id.textOff :
                    setbOnline(bOnline);
                    break;
                case R.id.invest_button:
                    setSugerID();
                    break;
                case R.id.map_button:
                    movePage = new Intent(MainActivity.this, MapWebActivity.class);
                    movePage.putExtra(getString(R.string.key_bonline), bOnline);
                    movePage.putExtra("type", "");
                    movePage.putExtra("fId", "");
                    startActivity(movePage);
                    break;
                case R.id.down_button:
                    movePage = new Intent(MainActivity.this, DownActivity.class);
                    movePage.putExtra(getString(R.string.key_bonline), bOnline);
                    startActivity(movePage);
                    break;
                case R.id.upload_button:
                    movePage = new Intent(MainActivity.this, UploadActivity.class);
                    movePage.putExtra(getString(R.string.key_bonline), bOnline);
                    startActivity(movePage);
                    break;
                case R.id.boardList_button:
                    movePage = new Intent(MainActivity.this, BoardlistActivity.class);
                    movePage.putExtra(getString(R.string.key_bonline), bOnline);
                    startActivity(movePage);
                    break;
                case R.id.qnalist_button:
                    movePage = new Intent(MainActivity.this, QnalistActivity.class);
                    movePage.putExtra(getString(R.string.key_bonline), bOnline);
                    startActivity(movePage);
                    break;
            }

            }
        };

        mOn.setOnClickListener(listener);
        mOff.setOnClickListener(listener);
        tOn.setOnClickListener(listener);
        tOff.setOnClickListener(listener);


        mInvest.setOnClickListener(listener);

        if (bOnline) {
            mMap.setOnClickListener(listener);
            mDown.setOnClickListener(listener);
            mUpload.setOnClickListener(listener);
            mBoardL.setOnClickListener(listener);
            mQnaL.setOnClickListener(listener);
        } else {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setAlpha(70);
            mMap.setAlpha(70);
            mDown.setAlpha(70);
            mUpload.setAlpha(70);
            bIcon.setAlpha(70);
            qIcon.setAlpha(70);

            board_layer.setBackgroundColor(paint.getColor());
            oneText.setTextColor(Color.GRAY);
            threeText.setTextColor(Color.GRAY);
            fourText.setTextColor(Color.GRAY);
            mBoardL.setTextColor(Color.GRAY);
            mQnaL.setTextColor(Color.GRAY);
        }

        mainDataUrl = getString(R.string.server_ip) + getString(R.string.req_maindata);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(networkMoniter);
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            unbindService(mConnection);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setbOnline(boolean bflag) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        if(!bflag) {
            builder.setMessage("온라인 모드로 변경하시겠습니까?");
            builder.setTitle("");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bOnline = true;
                    movePage = new Intent(MainActivity.this, SSLLoginActivity.class);
                    movePage.putExtra(getString(R.string.key_bonline), bOnline);
                    startActivity(movePage);
                }
            });

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.create().show();
        }else {

            builder.setMessage("오프라인 모드로 변경하시겠습니까?");
            builder.setTitle("");
            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bOnline = false;
                    stopSSLVPN();
//                    /*if(mVpnReceiver != null) {
//                        unregisterReceiver(mVpnReceiver);
//                    }*/
                    movePage = new Intent(MainActivity.this, MainActivity.class);
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

        }

    }

    public void setSugerID(){

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                File dbFile = new File(getString(R.string.db_full_name));
                if(dbFile.exists()){
                    Intent intent = new InsertUtil(getApplicationContext(), bOnline).getResultIntent(true);
                    intent.putExtra(getString(R.string.key_mode), mode);
                    startActivity(intent);

                }else{
                    Toast.makeText(MainActivity.this, getString(R.string.downInvestListError), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    void requestMainData(){
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, mainDataUrl, successListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                return new HashMap<>();
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> successListener = new Response.Listener<String>()
    {

        @Override
        public void onResponse(String reponse)
        {
            requestMainInfo(reponse);
        }
    };

    private Response.ErrorListener failListener = new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError e)
        {
        }
    };

    void requestMainInfo(String str){

        try{
            //JSONObject set
            JSONObject jObj = new JSONObject(str);

            JSONObject mainData = jObj.getJSONObject("listXmlReturnVO");
            JSONArray mArr = new JSONArray(mainData.getJSONArray("resultList").toString());

            for(int i=0; i<mArr.length(); i++) {
                JSONObject pObject = mArr.getJSONObject(i);

                String gubun = pObject.getString("bbs_secd");
                if(gubun.equals("1")){// 공지사항
                    String title = pObject.getString("bbs_sj");
                    mBoardD.setText(title);
                    Board_seq = pObject.getInt("bbs_sn");

                }else if(gubun.equals("2")){// QnA
                    String title = pObject.getString("bbs_sj");
                    mQnaD.setText(title);
                    QnA_seq = pObject.getInt("bbs_sn");
                }


            }

        }catch (JSONException e) {
            Log.e(getString(R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "requestMainInfo done");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    private void displayAlert(String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("네트워크 상태변경 알림");
        builder.setMessage(msg).setCancelable(
                false).setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        movePage = new Intent(getApplicationContext(), MainActivity.class);
                        movePage.putExtra(getString(R.string.key_bonline), bOnline);
                        startActivity(movePage);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private BroadcastReceiver mVpnReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(final Context context, Intent intent)
        {
            //TODO: move into IntentFilter, or dispatch from to DaemonEnabler.receive()
            if(intent.getAction().equals("com.secuwiz.SecuwaySSL.Service.STATUS"))
            {
                System.out.println("VPN status onReceive : " + intent.getIntExtra(getString(R.string.key_status), 0));
            }

        }
    };

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
        try
        {
            unregisterReceiver(mVpnReceiver);
        }catch (Exception e){
            e.printStackTrace();
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
                    Log.e(getString(R.string.remoteException), e.getMessage());
                } finally {
                    Log.i(getString(R.string.done), "StopRunnable done");
                }
            }
        }
    }

    public void stopSSLVPN(){
        StopRunnable a = new StopRunnable();
        Thread thread = new Thread(a);
        thread.start();
    }


    private class SecuwayServiceConnection implements ServiceConnection {

        public void onServiceConnected(ComponentName name, IBinder service) {
            //서비스 바인더  멤버변수로 저장
            tempService = service;
        }

        public void onServiceDisconnected(ComponentName name) {
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage("프로그램을 종료 하시겠습니까?");
                builder.setTitle("");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stopSSLVPN();
                        if(mVpnReceiver != null) {
                            unregisterReceiver(mVpnReceiver);
                        }
                        close();
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

    private void close()
    {
        finish();
        Intent intent = new Intent(MainActivity.this, SSLLoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("KILL_ACT", true);
        startActivity(intent);
    }
}
