package com.mrns.downdata;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

import com.mrns.sqllite.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DownPointFragment extends Fragment {

    //현황조사자료 내려받기 리스트
    ListView iList;
    PointList_Item_Adapter iAdapter;

    ArrayAdapter<String> adjisa;
    ArrayList<String> jisaNmList;
    ArrayList<String> jisaCdList;
    Spinner jisa;
    String jisaUrl;
    String jisaCd = "all";

    String investListUrl;
    String pointListUrl;
    int totPage = 1;
    int pageNo = 1;
    int pageSize = 10;
    boolean lastitemVisibleFlag = false;
    int listCount = 0;
    int investSeq = 0;
    private ImageView downImg;
    private TextView downText;
    String subject;

    TextView userView;
    String user_name;
    String user_id;

    SQLiteDatabase db;
    DBHelper helper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate( com.mrns.main.R.layout.activity_down_tab_point, container, false );
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        SharedPreferences preSession = getContext().getSharedPreferences("preSession", Activity.MODE_PRIVATE);

        //사용자 이름 설정e
        user_id = preSession.getString(getString(com.mrns.main.R.string.key_inq_user_id),getString(com.mrns.main.R.string.empty));
        user_name = preSession.getString(getString(com.mrns.main.R.string.key_inq_user_name), getString(com.mrns.main.R.string.empty));
        userView = (TextView) view.findViewById(com.mrns.main.R.id.user_nm);
        userView.setText(getString(com.mrns.main.R.string.down_user_name, user_name));



        //현황조사자료 리스트
        investListUrl = getString(com.mrns.main.R.string.server_ip) + getString(com.mrns.main.R.string.req_investlist);
        //Spinner Set
        jisa = (Spinner) view.findViewById(com.mrns.main.R.id.point_jisa);
        getJisa();
        jisa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jisaCd = jisaCdList.get(position).toString();
                iAdapter = new PointList_Item_Adapter(getContext());
                pageNo = 1;
                requestinvestListPOST();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //List Set
        iList = (ListView) view.findViewById(com.mrns.main.R.id.point_list);
        iAdapter = new PointList_Item_Adapter(getContext());

        downImg = (ImageView) view.findViewById(com.mrns.main.R.id.img_invest_down);
        downText = (TextView) view.findViewById(com.mrns.main.R.id.btn_invest_down);

        ViewPager.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case com.mrns.main.R.id.img_invest_down :
                    case com.mrns.main.R.id.btn_invest_down :

                        //현황자료 대상자료 다운로드
                        if(investSeq != 0) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                            builder.setMessage(getString(com.mrns.main.R.string.downConfirm));
                            builder.setTitle("");
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pointListUrl = getString(com.mrns.main.R.string.server_ip) + getString(com.mrns.main.R.string.req_downPoint);
                                    pointListUrl+= "?bbs_sn=" + investSeq + "&userid=" + user_id;
                                    new DownloadDBAsync(getContext()).execute(pointListUrl);
                                }
                            });

                            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                            builder.create().show();
                        }else {
                            Toast.makeText(getContext(), getString(com.mrns.main.R.string.downPointListErrorMsg), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    break;
                }
            }
        };

        downImg.setOnClickListener(listener);
        downText.setOnClickListener(listener);

    }//end onCreateView

    void getJisa() {
        jisaUrl = getString(com.mrns.main.R.string.server_ip) + getString(com.mrns.main.R.string.req_jisalist);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        Request request = new StringRequest(Request.Method.POST, jisaUrl, jisaSuccessListener, jisaFailListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                return new HashMap<>();
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(com.mrns.main.R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> jisaSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse)
        {
            JSONObject jObject;
            try {
                JSONObject jObj = new JSONObject(reponse);
                JSONObject obj = jObj.getJSONObject("listXmlReturnVO");
                JSONArray jisaArr = new JSONArray(obj.getJSONArray("resultList").toString());

                jisaNmList = new ArrayList<>();
                jisaCdList = new ArrayList<>();
                for(int i=0; i< jisaArr.length(); i++) {
                    jObject = jisaArr.getJSONObject(i);
                    String key = jObject.getString("key");
                    String values = jObject.getString("value");
                    jisaCdList.add(key);
                    jisaNmList.add(values);

                }

                adjisa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, jisaNmList);
                jisa.setAdapter(adjisa);
                adjisa.notifyDataSetChanged();

            } catch (JSONException e) {
                e.getMessage();
            } finally {
                Log.i("done", ":jisaSuccessListener done");
            }


        }
    };

    private Response.ErrorListener jisaFailListener = new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError e)
        {
            e.printStackTrace();
        }
    };

    public String getSDPath() {
        File extSt = Environment.getExternalStorageDirectory();
        return extSt.getAbsolutePath();
    }

    //현황자료 대상자료 리스트
    private void requestinvestListPOST()
    {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        Request request = new StringRequest(Request.Method.POST, investListUrl, successListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("dept", jisaCd);
                hash.put("pageNum", String.valueOf(pageNo));
                hash.put("blockCount", String.valueOf(pageSize));
                return hash;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(com.mrns.main.R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> successListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String reponse)
        {
            doMakeInvestList(reponse);
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


    void doMakeInvestList(String str){

        JSONObject jObject;
        JSONArray investList;

        try {
            //JSONObject set {
            JSONObject jObj = new JSONObject(str);
            JSONObject totList = jObj.getJSONObject("listXmlReturnVO");
            pageSize = totList.getInt("blockCount");
            totPage = totList.getInt("totPage");

            //JSONArray Set
            investList = new JSONArray(totList.getJSONArray("resultList").toString());
            listCount = investList.length();

            Resources mRes = getResources();

            for (int i = 0; i < listCount; i++) {
                jObject = investList.getJSONObject(i);  // JSONObject 추출
                int bbs_sn = jObject.getInt("bbs_sn");
                String subject = jObject.getString("subject");
                String wrt_de = jObject.getString("wrt_de");
                String wrt_id = jObject.getString("wrt_id");
                int startSeq = jObject.getInt("startSeq");
                int endSeq = jObject.getInt("endSeq");

                iAdapter.addItem(new PointList_Item(bbs_sn, mRes.getDrawable(com.mrns.main.R.drawable.btn_radio_n), subject, wrt_de));

            }

        }catch (JSONException e){
            e.printStackTrace();
        }finally {
            Log.i("done", "doMakeInvestList done");
        }

        //Data Binding
        iList.setAdapter(iAdapter);
//        iAdapter.notifyDataSetChanged();

        //ListView Position Location Set
        iList.post(new Runnable() {
            @Override
            public void run() {
                int pos;

                if (pageNo == 1) {
                    pos = 0;
                } else {
                    pos = (pageNo - 1) * pageSize;
                }


                iList.setSelection(pos);
            }
        });

        iList.setAdapter(iAdapter);

        iList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                PointList_Item poItem = (PointList_Item) iAdapter.getItem(position);

                Drawable selectedIcon = getResources().getDrawable(com.mrns.main.R.drawable.btn_radio_p);
                Drawable nonSelectedIcon = getResources().getDrawable(com.mrns.main.R.drawable.btn_radio_n);

                for(int i=0; i< iAdapter.getCount();  i++){
                    if(position == i) {
                        poItem.setIcon(selectedIcon);
                    } else {
                        PointList_Item nonItem = (PointList_Item) iAdapter.getItem(i);
                        nonItem.setIcon(nonSelectedIcon);
                    }
                }

                //getData
                investSeq = poItem.getInvest_seq();
                subject = poItem.getData(0);

                iAdapter.notifyDataSetChanged();

            }//end onItemClick
        });//end setOnItemClickListener


        iList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    pageNo = pageNo + 1;
                    requestinvestListPOST();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                lastitemVisibleFlag = (listCount > 0 && (firstVisibleItem + visibleItemCount) == totalItemCount && pageNo < totPage);

            }
        });



    }



}
