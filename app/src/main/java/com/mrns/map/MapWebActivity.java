package com.mrns.map;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.database.sqlite.SQLiteException;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.mrns.common.NetcheckMoniter;
import com.mrns.invest.record.RnsNpRslt;
import com.mrns.invest.record.RnsNpSttusInq;
import com.mrns.invest.record.RnsNpSubPath;
import com.mrns.invest.record.RnsNpTraverRslt;
import com.mrns.invest.record.RnsNpUnity;
import com.mrns.invest.record.RnsStdrspotUnity;
import com.mrns.invest.record.RnsSttusInqBefore;
import com.mrns.invest.util.InsertUtil;
import com.mrns.invest.util.JsonPaserUtil;
import com.mrns.invest.view.activity.InvestDetailActivity_;
import com.mrns.invest.view.activity.InvestDetailNcpActivity_;
import com.mrns.invest.view.activity.InvestInsertInfo_;

import com.mrns.invest.view.activity.InvestInsertNcpInfo_;
import com.mrns.main.MainActivity;
import com.mrns.main.R;
import com.mrns.map.adapter.PointVisiblityAdapter;
import com.mrns.utils.CommonUtils;
import com.mrns.utils.GpsInfo;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MapWebActivity extends AppCompatActivity {
    String user_id;
    String user_name;
    String points_nm;
    String base_point_no;
    String points_knd_cd;
    double mGoalLatitude;
    double mGoalLongitude;
    InsertUtil mInsertUtil;
    WebAppInterface webAppInterface;
    boolean isStd;
    int points_secd = 2;

    LocationManager mLocationManager;
    LocationListener mLocationListener;
    long mStdrspotSn;
    RnsStdrspotUnity mUnity;
    RnsSttusInqBefore before;

    RnsNpUnity ncpunity;
    RnsNpRslt rslt;
    RnsNpTraverRslt trav;
    RnsNpSubPath spath;

    boolean mFindRoute = false;

    private CommonUtils mCommonUtils;
    private WebView mWebView;
    private String url;
    LinearLayout headerView;
    boolean ckTitle = true;

    Location mLocation;
    ImageView btn_back;
    TextView down_title;
    Intent movePage;
    ImageButton mapSearch;

    Intent intent;
    int socketTimeout = 30000;
    int mode =0;
    String type = "";
    String fId = "";

    long npuSn;
    String basePointNo = "";
    String pointsKndCd = "";
    String pointDetailUrl = "";
    String pointBaseDetailUrl = "";
    String saveDataUrl = "";
    String saveFileUrl = "";
    //UI
    TextView pointTitle;
    TextView pointAddr;
    TextView pointInsDate;
    TextView pointStatus;
    ImageView btnClose;
    ImageView imageviewFindRoute;

    ImageView loca_nav_back;
    Button btnPointDetail;
    Button btnPointInsert;

    LinearLayout linear;
    LayoutInflater inflater;

    boolean bOnline;
    private NetcheckMoniter networkMoniter = null;
    public static Context aContext;
    String mRouteUrl;
    ViewPager.OnClickListener infoStdListener;
    ViewPager.OnClickListener infoNPListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_web);

        mStdrspotSn = getIntent().getLongExtra(getString(R.string.key_stdr_spot_sn), 0);
        mUnity = mUnity.FindByStdrspotSn(mStdrspotSn);

        aContext = this;
        isStd = getApplicationContext().getSharedPreferences(getString(R.string.classCheckPref), MODE_PRIVATE).getBoolean(getString(R.string.classCheckKey), true);
        mRouteUrl = getString(R.string.server_ip) + getString(R.string.req_ollehmap);

        mCommonUtils = new CommonUtils(getApplicationContext());

        intent = getIntent();
        bOnline = intent.getBooleanExtra(getString(R.string.key_bonline), bOnline);
        mode = intent.getIntExtra(getString(R.string.key_mode), mode);
        type = intent.getStringExtra(getString(R.string.key_type));
        fId = intent.getStringExtra(getString(R.string.key_fid));
        mFindRoute = intent.getBooleanExtra(getString(R.string.key_find_route), false);

        if(type == null ) type ="";

        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        SharedPreferences preSession = getApplicationContext().getSharedPreferences("preSession", Activity.MODE_PRIVATE);
        //사용자 이름 설정
        user_id = preSession.getString(getString(R.string.key_inq_user_id),getString(R.string.empty));
        user_name = preSession.getString(getString(R.string.key_inq_user_name), getString(R.string.empty));
        //뒤로가기 버튼
        btn_back = (ImageView) findViewById(R.id.btn_nav_back);
        down_title = (TextView) findViewById(R.id.titleTextView);
        //헤더
        headerView = (LinearLayout) findViewById(R.id.map_header);
        //검색버튼
        mapSearch = (ImageButton) findViewById(R.id.btn_map_search);

        //map set
        setWebView();

        ViewPager.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_nav_back :
                    case R.id.titleTextView :
                        btn_back.setImageResource(R.drawable.nav_btn_back_p);
                        movePage = new Intent(MapWebActivity.this, MainActivity.class);
                        movePage.putExtra(getString(R.string.key_bonline), bOnline);
                        startActivity(movePage);
                        break;
                    case R.id.btn_map_search :
                        movePage = new Intent(MapWebActivity.this, MapSearchActivity_.class);
                        movePage.putExtra(getString(R.string.key_bonline), bOnline);
                        startActivity(movePage);
                        break;
                }//end switch
            }//end Onclick
        };//end listener

        btn_back.setOnClickListener(listener);
        down_title.setOnClickListener(listener);
        mapSearch.setOnClickListener(listener);

        switch (mode){
            case 0 :
                break;
            case 1 :
                mStdrspotSn = intent.getLongExtra(getString(R.string.key_stdr_spot_sn), mStdrspotSn);
                npuSn = intent.getLongExtra(getString(R.string.key_npu_sn), npuSn);
                basePointNo = intent.getStringExtra(getString(R.string.key_base_point_no));
                //위치보기 UI SET (activiy_map_web_location)
                initInfoLayout();

                if (getIntent().getBooleanExtra(getString(R.string.key_map_search_move), false)) {
                    boolean isStdFromMapSearch = type.equals(getString(R.string.key_point_type_for_map_query));
                    if (isStdFromMapSearch) {
                        getMakeLocatinView();
                    } else {
                        getMakeNpuSnLocatinView(String.valueOf(npuSn));
                    }

                    initInfoListener();
                    setInfoListener(isStdFromMapSearch);
                } else {
                    if (isStd) {
                        getMakeLocatinView();
                    } else {
                        getMakeNpuSnLocatinView(String.valueOf(npuSn));
                    }

                    initInfoListener();
                    setInfoListener(isStd);
                }

        }
    }

    public boolean isInvestDataExist(long stdrspot_sn){
        boolean isEx = false;
        RnsStdrspotUnity unity = new RnsStdrspotUnity();
        unity = unity.FindByStdrspotSn(stdrspot_sn);
        if(unity != null)   isEx = true;
        //현재 ID컬럼에 데이터 안넣어서 이건 null 이 검색됨
        return isEx;
    }

    public boolean isInvestNcpDataExist(long npu_sn){
        boolean isEx = false;
        //RnsStdrspotUnity unity = new RnsStdrspotUnity();
        RnsNpUnity unity = new RnsNpUnity();
        unity = unity.FindByNpuSn(npu_sn);
        if(unity != null)   isEx = true;
        //현재 ID컬럼에 데이터 안넣어서 이건 null 이 검색됨
        return isEx;
    }

    //Unity, Before IMG 자료 수신
    public void downInvestImg(long stdrspot_sn){
        saveFileUrl = getString(R.string.server_ip) + getString(R.string.req_insert_data_file);
        saveFileUrl+= "?stdrspot_sn=" + stdrspot_sn + "&userid=" + user_id;
        new DownInvestFileAsync(getApplicationContext()).execute(saveFileUrl);
    }

    public void saveInvestNcpImages(long npuSn){
        saveFileUrl = getString(R.string.server_ip) + getString(R.string.url_insert_nc_data_download);
        saveFileUrl+= "?npu_sn=" + npuSn + "&userid=" + user_id;
        DownInvestFileAsync downInvestFileAsync = new DownInvestFileAsync(getApplicationContext());
        downInvestFileAsync.setSingleNCDownload(true);
        downInvestFileAsync.setNpuSn(npuSn);
        downInvestFileAsync.execute(saveFileUrl);
    }


    //Unity, Before JSON 자료 수신
    public void saveInvestData(final long stdrspot_sn) {
        saveDataUrl = getString(R.string.server_ip) + getString(R.string.req_insert_data_gis);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(MapWebActivity.this);
        Request request = new StringRequest(Request.Method.POST, saveDataUrl, successSaveDataListener, failSaveDataListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_stdr_spot_sn_json), String.valueOf(stdrspot_sn));
                hash.put("userid", user_id);

                return hash;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    public void saveInvestNcpData(final long npu_sn) {
        String saveaUrl = getString(R.string.server_ip) + getString(R.string.req_insert_data_ncp_gis);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(MapWebActivity.this);
        Request request = new StringRequest(Request.Method.POST, saveaUrl, successSaveNcpDataListener, failSaveDataListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_npu_sn_json), String.valueOf(npu_sn));
                hash.put("userid", user_id);

                return hash;

            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
    public void saveInvestNcpRsltData(final long npu_sn) {
        String saveUrl = getString(R.string.server_ip) + getString(R.string.req_insert_data_ncp_rstl_gis);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(MapWebActivity.this);
        Request request = new StringRequest(Request.Method.POST, saveUrl, successSaveRsltDataListener, failSaveDataListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_npu_sn_json), String.valueOf(npu_sn));
                hash.put("userid", user_id);

                return hash;

            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
    public void saveInvestNcpTraverData(final long npu_sn) {
        String saveUrl = getString(R.string.server_ip) + getString(R.string.req_insert_data_ncp_traver_gis);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(MapWebActivity.this);
        Request request = new StringRequest(Request.Method.POST, saveUrl, successSaveTraverDataListener, failSaveDataListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_npu_sn_json), String.valueOf(npu_sn));
                hash.put("userid", user_id);

                return hash;

            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
    public void saveInvestNcpSubData(final long npu_sn) {
        String saveUrl = getString(R.string.server_ip) + getString(R.string.req_insert_data_ncp_traver_sub);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(MapWebActivity.this);
        Request request = new StringRequest(Request.Method.POST, saveUrl, successSaveSubDataListener, failSaveDataListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_npu_sn_json), String.valueOf(npu_sn));
                hash.put("userid", user_id);

                return hash;

            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    public void saveInvestSttusInqData(final long npu_sn) {
        String saveUrl = getString(R.string.server_ip) + getString(R.string.req_insert_data_ncp_sttus_inq);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(MapWebActivity.this);
        Request request = new StringRequest(Request.Method.POST, saveUrl, successSaveSttusInqDataListener, failSaveDataListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_npu_sn_json), String.valueOf(npu_sn));
                hash.put("userid", user_id);

                return hash;

            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearApplicationCache(null);

        try
        {
            unregisterReceiver(networkMoniter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 웹뷰에 케시가 쌓이는것을 앱이 종료할때마다 비우는 함수
     */
    public void clearApplicationCache(java.io.File dir){
        if(dir == null){
            dir = getCacheDir();
        }

        if(dir == null){
            return ;
        }

        java.io.File[] children = dir.listFiles();
        try{
            for (File child:children) {
                if (child.isDirectory()) {
                    clearApplicationCache(child);
                } else {
                    child.delete();
                }
            }
        }catch(Exception e){
            Toast.makeText(MapWebActivity.this , "캐시 삭제 실패", Toast.LENGTH_SHORT).show();
        }
    }

    protected Response.Listener<String> successSaveDataListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            //기준점 통합 테이블(sqlite) 자료 입력
            mUnity = new RnsStdrspotUnity();
            mUnity = JsonPaserUtil.investUnityArrJsonPaser(reponse);

            try {
                mUnity.save();
            } catch (NullPointerException e) {
                e.getStackTrace();
            }

//            before = new RnsSttusInqBefore();
//            before = JsonPaserUtil.investBeforeArrJsonPaser(reponse);
//
//            try {
//                before.setSttusBfSn(mStdrspotSn);
//            } catch (NullPointerException e) {
//                e.getStackTrace();
//            }
//
//            before.save();

            try {
                SugarRecord.getSugarDataBase().execSQL("UPDATE RNS_STDRSPOT_UNITY set ID = STDRSPOT_SN WHERE STDRSPOT_SN="+ mStdrspotSn);
                //SugarRecord.getSugarDataBase().execSQL("UPDATE RNS_STTUS_INQ_BEFORE set ID = STDRSPOT_SN WHERE STTUS_BF_SN="+ mStdrspotSn);
            } catch (SQLiteException e) {
                Log.e(this.getClass().getName(), "ID 셋팅 되어 있음");
            }

            Log.i("done", "UNITY/BEFORE TABLE Insert Done");

            movePage = new Intent(MapWebActivity.this, InvestInsertInfo_.class);
            movePage.putExtra(getString(R.string.key_bonline), true);
            movePage.putExtra(getString(R.string.key_stdr_spot_sn), mStdrspotSn);
            movePage.putExtra(getString(R.string.key_point_name), mUnity.getPointsNm());
            startActivity(movePage);
        }
    };

    protected Response.Listener<String> successSaveNcpDataListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            //기준점 통합 테이블(sqlite) 자료 입력
            //unity = new RnsStdrspotUnity();
            //unity = JsonPaserUtil.investUnityArrJsonPaser(reponse);

            ncpunity = new RnsNpUnity();
            ncpunity = JsonPaserUtil.investNcpUnityJsonPaser(reponse);


            try {
                ncpunity.save();
            } catch (NullPointerException e) {
                e.getStackTrace();
            }
            /*try {
                //SugarRecord.getSugarDataBase().execSQL("UPDATE RNS_NP_UNITY set ID = NPU_SN WHERE NPU_SN="+ npuSn);
                //SugarRecord.getSugarDataBase().execSQL("UPDATE RNS_NP_STTUS_INQ_BF set ID = NPSI_BF_SN WHERE NPU_SN="+ npuSn);
            } catch (SQLiteException e) {
                Log.e(this.getClass().getName(), "ID 셋팅 되어 있음");
            }*/
            Log.i("done", "UNITY/BEFORE TABLE Insert Done");
            movePage = new Intent(MapWebActivity.this, InvestInsertNcpInfo_.class);
            movePage.putExtra("bOnline", true);
            movePage.putExtra("npuSn", npuSn);
            movePage.putExtra("pointNm", ncpunity.getPointsNm());
            startActivity(movePage);
        }
    };

    protected Response.Listener<String> successSaveRsltDataListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            //기준점 통합 테이블(sqlite) 자료 입력
            rslt = new RnsNpRslt();
            //unity = new RnsStdrspotUnity();
            rslt = JsonPaserUtil.investRnsNpRsltJsonPaser(reponse);

            try {
                rslt.save();
            } catch (NullPointerException e) {
                e.getStackTrace();
            }


            Log.i("done", "RNS_NP_RSLT TABLE Insert Done");

/*            movePage = new Intent(MapWebActivity.this, InvestInsertInfo_.class);
            movePage.putExtra("bOnline", true);
            movePage.putExtra("stdrspotSn", stdrspotSn);
            movePage.putExtra("pointNm", before.getPointsNm());
            startActivity(movePage);*/
        }
    };
    protected Response.Listener<String> successSaveTraverDataListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            //기준점 통합 테이블(sqlite) 자료 입력
           // unity = new RnsStdrspotUnity();
          //  unity = JsonPaserUtil.investUnityArrJsonPaser(reponse);

            trav = new RnsNpTraverRslt();
            trav = JsonPaserUtil.investRnsNpTraverRsltJsonPaser(reponse);
            try {
                trav.save();
            } catch (NullPointerException e) {
                e.getStackTrace();
            }

            Log.i("done", "TraverRslt TABLE Insert Done");

            /*movePage = new Intent(MapWebActivity.this, InvestInsertInfo_.class);
            movePage.putExtra("bOnline", true);
            movePage.putExtra("stdrspotSn", stdrspotSn);
            movePage.putExtra("pointNm", before.getPointsNm());
            startActivity(movePage);*/
        }
    };
    protected Response.Listener<String> successSaveSubDataListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            //기준점 통합 테이블(sqlite) 자료 입력
      //      unity = new RnsStdrspotUnity();
       //     unity = JsonPaserUtil.investUnityArrJsonPaser(reponse);

            spath = new RnsNpSubPath();
            spath = JsonPaserUtil.investRnsNpSubPathJsonPaser(reponse);
            try {
                spath.save();
            } catch (NullPointerException e) {
                e.getStackTrace();
            }
            Log.i("done", "SubPath TABLE Insert Done");

            /*movePage = new Intent(MapWebActivity.this, InvestInsertInfo_.class);
            movePage.putExtra("bOnline", true);
            movePage.putExtra("stdrspotSn", stdrspotSn);
            movePage.putExtra("pointNm", before.getPointsNm());
            startActivity(movePage);*/
        }
    };
    protected Response.Listener<String> successSaveSttusInqDataListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            RnsNpSttusInq inq = new RnsNpSttusInq();
            inq = JsonPaserUtil.investRnsNpSttusInqJsonPaser(reponse);
            inq.save();

            Log.i("done", "sttusInq TABLE Insert Done");
            movePage = new Intent(MapWebActivity.this, InvestInsertNcpInfo_.class);
            movePage.putExtra("bOnline", true);
            movePage.putExtra("stdrspotSn", npuSn);
            movePage.putExtra("pointNm", points_nm);
            startActivity(movePage);
        }
    };

    protected Response.ErrorListener failSaveDataListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {
            e.getCause();
            Toast.makeText(MapWebActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();

        }
    };

    void initInfoLayout() {
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linear = (LinearLayout) inflater.inflate(R.layout.activiy_map_web_location, null);

        LinearLayout.LayoutParams paramlinear = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        getWindow().addContentView(linear, paramlinear);

        //위치보기 UI SET (activiy_map_web_location)
        pointTitle = (TextView) findViewById(R.id.point_title_map_web_location);
        pointAddr = (TextView) findViewById(R.id.point_txt_addr);
        pointInsDate = (TextView) findViewById(R.id.point_install_date);
        pointStatus = (TextView) findViewById(R.id.point_status_map_web_location);
        loca_nav_back = (ImageView) findViewById(R.id.btn_nav_location_back);
        btnPointDetail = (Button) findViewById(R.id.btn_point_detail);
        btnPointInsert = (Button) findViewById(R.id.btn_point_insert);
        imageviewFindRoute = (ImageView) findViewById(R.id.imageview_find_route);
        loca_nav_back = (ImageView) findViewById(R.id.btn_nav_location_back);
        btnClose = (ImageView) findViewById(R.id.btn_close);
    }

    void initInfoListener() {
        View.OnClickListener infoBaseListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_nav_location_back :
                        finish();
                        break;
                    case  R.id.btn_close :
                        linear.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        };

        loca_nav_back.setOnClickListener(infoBaseListener);
        btnClose.setOnClickListener(infoBaseListener);

        infoStdListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_point_detail :
                        onBtnPointDetail(true);
                        break;
                    case R.id.btn_point_insert :
                        onBtnPointInsert(true);
                        break;
                    case R.id.imageview_find_route:
                        onFindRoute(true);
                        break;
                }
            }
        };

        infoNPListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_point_detail :
                        onBtnPointDetail(false);
                        break;
                    case R.id.btn_point_insert :
                        onBtnPointInsert(false);
                        break;
                    case R.id.imageview_find_route:
                        onFindRoute(false);
                        break;
                }
            }
        };
    }

    void setInfoListener(boolean forStdPoint) {
        View.OnClickListener tempListener;

        if (forStdPoint) {
            tempListener = infoStdListener;
        } else {
            tempListener = infoNPListener;
        }

        btnPointDetail.setOnClickListener(tempListener);
        btnPointInsert.setOnClickListener(tempListener);
        imageviewFindRoute.setOnClickListener(tempListener);
    }

    void onBtnPointDetail(final boolean forStdPoint) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapWebActivity.this);
        builder.setMessage(getString(R.string.goto_point_detail));
        builder.setTitle("");
        builder.setPositiveButton(getString(R.string.str_invest_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (forStdPoint) {
                    movePage = new Intent(MapWebActivity.this, InvestDetailActivity_.class);
                    movePage.putExtra(getString(R.string.key_stdr_spot_sn), mStdrspotSn);
                } else {
                    movePage = new Intent(MapWebActivity.this, InvestDetailNcpActivity_.class);
                    movePage.putExtra(getString(R.string.key_npu_sn), npuSn);
                }

                movePage.putExtra(getString(R.string.key_bonline), true);
                movePage.putExtra(getString(R.string.key_is_geo), true);
                movePage.putExtra(getString(R.string.key_title), pointTitle.getText().toString());
                startActivity(movePage);
            }
        });

        builder.setNegativeButton(getString(R.string.str_invest_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    void onBtnPointInsert(final boolean forStdPoint) {
        if (isStd != forStdPoint) {
            //입력 불가
            String message = getString(R.string.empty);

            if (isStd) {
                message = getString(R.string.message_insert_in_std);
            } else {
                message = getString(R.string.message_insert_in_np);
            }

            Toast.makeText(MapWebActivity.this, message, Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapWebActivity.this);
            builder.setMessage(getString(R.string.goto_insert_detail));
            builder.setTitle(getString(R.string.empty));
            builder.setPositiveButton(getString(R.string.str_invest_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                if (forStdPoint) {
                    if (!isInvestDataExist(mStdrspotSn)) {
                        saveInvestData(mStdrspotSn);
                        downInvestImg(mStdrspotSn);
                    } else {
                        movePage = new Intent(MapWebActivity.this, InvestInsertInfo_.class);
                        movePage.putExtra(getString(R.string.key_stdr_spot_sn), mStdrspotSn);
                        movePage.putExtra("bOnline", true);
                        movePage.putExtra("pointNm", points_nm);
                        startActivity(movePage);
                        //before = RnsSttusInqBefore.FindByStdrspotSn(mStdrspotSn);
                    }
                } else {
                    if(!isInvestNcpDataExist(npuSn)){
                        saveInvestNcpData(npuSn);
                        saveInvestNcpTraverData(npuSn);
                        saveInvestNcpRsltData(npuSn);
                        saveInvestNcpSubData(npuSn);
                        saveInvestSttusInqData(npuSn);
                        saveInvestNcpImages(npuSn);
                    }

                    movePage = new Intent(MapWebActivity.this, InvestInsertNcpInfo_.class);
                    movePage.putExtra("npuSn", npuSn);
                    movePage.putExtra("bOnline", true);
                    movePage.putExtra("pointNm", points_nm);
                    startActivity(movePage);
                }
                }
            });

            builder.setNegativeButton(getString(R.string.str_invest_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.create().show();
        }
    }

    public void onFindRoute(boolean forStdPoint) {
        final boolean isStd = forStdPoint;
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLocation = location;
                findRoute(isStd);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };

        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, mLocationListener);
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, mLocationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    void showMessage(String message) {
        final String toastMessage = message;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            Toast.makeText(getBaseContext(), toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    void findRoute(boolean forStdPoint) {
        double latitude = 35.8371895;
        double longitude = 127.0583138;
        if (null == mLocation) {
            showMessage("위치정보를 받아올 수 없습니다.");
            return;
        } else {
            showMessage("길찾기를 시작합니다.");
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
            mLocationManager.removeUpdates(mLocationListener);
        }

        //! 파라미터
        String keyValue;

        if (forStdPoint) {
            keyValue = base_point_no;
        } else {
            keyValue = String.valueOf(npuSn);
        }

        final String strParameter = String.format("'%s', '%s', '%d', '%s'", latitude, longitude, points_secd, keyValue);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript:getGoalCoordinate(" + strParameter + ")");
            }
        });
    }

    void setInfoUI(final boolean selStdPoint) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //infoLayout이 초기화되었는지 확인
                if (null != linear) {
                    linear.setVisibility(View.VISIBLE);
                } else {
                    initInfoLayout();
                    initInfoListener();
                }

                //점 구분 후 리스너 세팅
                setInfoListener(selStdPoint);
            }
        });
    }

    void getMakeLocatinView(){
        pointDetailUrl = getString(R.string.server_ip) + getString(R.string.req_pointInfo);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, pointDetailUrl, successListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_stdr_spot_sn_json), String.valueOf(mStdrspotSn));
                return hash;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    void getMakeBaseLocatinView(String pBasePointNo, String pAdmCode, String pPointName) {
        final String basePointNo = pBasePointNo;
        final String admCode = pAdmCode;
        final String pointName = pPointName;

        pointBaseDetailUrl = getString(R.string.server_ip) + getString(R.string.req_pointInfo_gis);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, pointBaseDetailUrl, successListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_base_point_no), basePointNo);
                hash.put(getString(R.string.key_adm_code), admCode);
                hash.put(getString(R.string.key_point_name), pointName);

                return hash;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    void getMakeNpuSnLocatinView(String npuSn) {
        final String npu_sn = npuSn;
        pointBaseDetailUrl = getString(R.string.server_ip) + getString(R.string.req_pointInfo_ncp_gis);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, pointBaseDetailUrl, successListener_1, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("npu_sn", npu_sn);
                return hash;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> successListener = new Response.Listener<String>()
    {

        @Override
        public void onResponse(String reponse)
        {
            try {
                //JSONObject set {
                JSONObject jObj = new JSONObject(reponse);
                JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");

                if(!bDetail.getString("resultVO").equalsIgnoreCase("null")){
                    JSONObject body = bDetail.getJSONObject("resultVO");

                    mStdrspotSn = body.getLong(getString(R.string.key_stdr_spot_sn_json));
                    base_point_no = body.getString(getString(R.string.key_base_point_no_json));
                    points_nm = body.getString("points_nm");
                    points_knd_cd = body.getString("points_knd_cd");
                    String instl_de = mCommonUtils.changeNullValue(body.getString("instl_de"), "-");
                    String mtrStatusCode = body.getString("mtr_sttus_cd");
                    String locplc_lnm = mCommonUtils.changeNullValue(body.getString("locplc_lnm"), "-");
                    String title = String.format("%s (%s)", points_nm, getPoinstKind(points_knd_cd));
                    String locplc = String.format("%s : %s", getString(R.string.str_map_location_addr_title), locplc_lnm);
                    String insDate = String.format("%s : %s", getString(R.string.str_map_location_insdate_title), instl_de);
                    String pStatus = String.format("%s : %s", getString(R.string.str_map_location_status_title), getMTRSTTUS(mtrStatusCode));

                    pointTitle.setText(title);
                    pointAddr.setText(locplc);
                    pointInsDate.setText(insDate);
                    pointStatus.setText(pStatus);
                } else {
                    pointTitle.setText(getString(R.string.pointInfo));
                    btnPointDetail.setVisibility(View.INVISIBLE);
                    btnPointInsert.setVisibility(View.INVISIBLE);
                }

            }catch (JSONException e) {
                Log.e(getString(R.string.jSONException), e.getMessage());
            }finally {
                Log.i(getString(R.string.done), "successListener done");
                setInfoUI(true);
            }
        }
    };

    private Response.ErrorListener failListener = new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError e)
        {
            e.printStackTrace();
        }
    };

    private Response.Listener<String> successListener_1 = new Response.Listener<String>()
    {

        @Override
        public void onResponse(String reponse)
        {
            JSONObject jObject;
            JSONArray bList;

            try {
                //JSONObject set {
                JSONObject jObj = new JSONObject(reponse);
                JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");

                if(!bDetail.getString("resultVO").equalsIgnoreCase("null")){
                    JSONObject body = bDetail.getJSONObject("resultVO");

                    npuSn = body.getLong("npu_sn");
                    points_secd = body.getInt("points_secd");
                    points_nm = body.getString("points_nm");
                    points_knd_cd = body.getString("points_knd_cd");
                    String lay_de = mCommonUtils.changeNullValue(body.getString("lay_de"), "-");
                    String mtrStatusCode = body.getString("mtr_sttus_cd");
                    String full_road_addr = mCommonUtils.changeNullValue(body.getString("full_road_addr"), "-");
                    String title = String.format("%s (%s)", points_nm, points_knd_cd);
                    String locplc = String.format("%s : %s", getString(R.string.str_map_location_addr_title), full_road_addr);
                    String insDate = String.format("%s : %s", getString(R.string.str_map_location_insdate_title), lay_de);
                    String pStatus = String.format("%s : %s", getString(R.string.str_map_location_status_title), getMTRSTTUS(mtrStatusCode));
                    pointTitle.setText(title);
                    pointAddr.setText(locplc);
                    pointInsDate.setText(insDate);
                    pointStatus.setText(pStatus);
                } else {
                    pointTitle.setText(getString(R.string.pointInfo));
                    btnPointDetail.setVisibility(View.INVISIBLE);
                }

            }catch (JSONException e) {
                Log.e(getString(R.string.jSONException), e.getMessage());
            }finally {
                Log.i(getString(R.string.done), "successListener done");
                setInfoUI(false);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    // Layout
    private void setWebView(){
        url = getString(R.string.server_ip) + getString(R.string.req_ollehmap);
        url+= "?type=" + type + "&fid=" + fId;

        if (type.equals(getString(R.string.key_point_type_for_map_query))) {
            String admCode;
            String pointName;

            if (null == mUnity) {
                admCode = getIntent().getStringExtra(getString(R.string.key_adm_code));
                pointName = getIntent().getStringExtra(getString(R.string.key_point_name));
            } else {
                admCode = mUnity.getAdmCd();
                pointName = mUnity.getPointsNm();
            }

            url += "&adm_code=" + admCode + "&point_name=" + pointName;
        }

        if (mFindRoute) {
            url += "&find_route=true";
        }

        mWebView = (WebView) findViewById(R.id.webTestMap);
        webAppInterface = new WebAppInterface(this);
        mWebView.addJavascriptInterface(webAppInterface, "Android");
        // javascript set
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        //url set
        mWebView.loadUrl(url);
        //WebView Client set

        mWebView.setWebViewClient(new WebViewClientClass());
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.clearFormData();
    }

    private String getMTRSTTUS(String sttus){
        return mCommonUtils.getMtrSttus(sttus, true);
    }

    private String getPoinstKind(String points_knd){
        return mCommonUtils.getPointsKind(points_knd);
    }


    class WebAppInterface extends Activity {
        Context mContext;
        private GpsInfo gps;

        /** Instantiate the interface and set the context */
        WebAppInterface(Context c) {
            mContext = c;
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public String getGpsInfo() {
            String locationCombo = "";
            gps = new GpsInfo(mContext);

            // GPS 사용유무 가져오기
            if (gps.isGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                locationCombo = String.valueOf(latitude) + "," + String.valueOf(longitude);

            } else {
                // GPS 를 사용할수 없으므로
                gps.showSettingsAlert();
            }

            return locationCombo;
        }

        @JavascriptInterface
        public void setBasePointNo(String basePointNo , String admCode, String pointName) {
            setInfoUI(true);
//            ((MapWebActivity)MapWebActivity.aContext).setInfoUI(true);
            getMakeBaseLocatinView(basePointNo, admCode, pointName);
        }
        @JavascriptInterface
        public void setNpuSnPointNo(String npuSn) {
            setInfoUI(true);
            //((MapWebActivity)MapWebActivity.aContext).setInfoUI(false);
            getMakeNpuSnLocatinView(npuSn);
        }

        @JavascriptInterface
        public void setLogMessage(String msg){
            Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void setLoadEnd(){

//            mProgressDialog.dismiss();
            Toast.makeText(mContext, "GIS INIT1", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void setFindRouteUrl(double startX, double startY, double goalX, double goalY) {
            final double sx = startX;
            final double sy = startY;
            final double gx = goalX;
            final double gy = goalY;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String strOption = String.format("?sx=%s&sy=%s&gx=%s&gy=%s&origin=olMap.html", sx, sy, gx, gy);

                    mWebView.clearCache(true);
                    mWebView.loadUrl(mRouteUrl + strOption);
                }
            });
        }

        @JavascriptInterface
        public void onFindRouteJS() {
            onFindRoute(isStd);
        }

        @JavascriptInterface
        public void popupNCPoint(boolean isNational, String[] layerIds) {
            final boolean checkNational = isNational;

            final PointVisiblityAdapter adapter = new PointVisiblityAdapter(MapWebActivity.this, checkNational, layerIds);
            new AlertDialog.Builder(MapWebActivity.this)
                    .setTitle(adapter.getTitle())
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mWebView.post(new Runnable(){
                                @Override
                                public void run(){
                                    mWebView.loadUrl("javascript:setNewParam('" + adapter.getNewParam() + "', " + checkNational + ")");
                                }
                            });
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
