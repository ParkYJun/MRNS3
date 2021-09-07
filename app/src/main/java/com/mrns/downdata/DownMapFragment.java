package com.mrns.downdata;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import com.mrns.common.Common;
import com.mrns.main.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DownMapFragment extends Fragment {

    //현황조사자료 내려받기 리스트
    ListView mList;
    MapList_Item_Adapter mAdapter;

    TextView userView;

    ArrayAdapter<String> adjisa;
    ArrayList<String> jisaNmList;
    ArrayList<String> jisaCdList;
    Spinner jisamap;
    String jisaUrl;
    String jisaCd = "all";

    String downListUrl;
    String downFileUrl;
    int totPage = 1;
    int pageNo = 1;
    int pageSize = 10;
    boolean lastitemVisibleFlag = false;
    int listCount = 0;
    int seqMapSeq = 0;
    ImageView downImg;
    TextView downText;
    String subject;
    String downfileName;
    Common util = new Common();
    String user_name;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState ) {
        return inflater.inflate(R.layout.activity_down_tab_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        SharedPreferences preSession = getContext().getSharedPreferences("preSession", Activity.MODE_PRIVATE);

        //사용자 이름 설정
        user_name = preSession.getString(getString(R.string.key_inq_user_name),getString(R.string.empty));
        userView = (TextView) view.findViewById(R.id.user_nm);
        userView.setText(getString(R.string.down_user_name, user_name));

        //List Set
        mList = (ListView) view.findViewById(R.id.map_list);

        //url
        downListUrl = getString(R.string.server_ip) + getString(R.string.req_downlist);
        //Spinner Set
        jisamap = (Spinner) view.findViewById(R.id.map_jisa);
        getJisa();
        jisamap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jisaCd = jisaCdList.get(position).toString();
                pageNo = 1;
                requestdownListPOST();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        downImg = (ImageView) view.findViewById(R.id.img_down);
        downText = (TextView) view.findViewById(R.id.btn_down);

        ViewPager.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_down :
                    case R.id.btn_down :

                        if(seqMapSeq != 0) {
                            downFileUrl = getString(R.string.server_ip) + getString(R.string.req_downfile);
                            requestdownFilePOST(seqMapSeq);
                        }else {
                            Toast.makeText(getContext(), getString(R.string.downMapErrorMsg), Toast.LENGTH_SHORT).show();
                            return;
                        }
                            break;
                }
            }
        };

        downImg.setOnClickListener(listener);
        downText.setOnClickListener(listener);

    }//end OnCreated


    void getJisa() {
        jisaUrl = getString(R.string.server_ip) + getString(R.string.req_jisalist);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        Request request = new StringRequest(Request.Method.POST, jisaUrl, jisaSuccessListener, jisaFailListener)
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
                jisamap.setAdapter(adjisa);
                adjisa.notifyDataSetChanged();

            } catch (JSONException e) {
                e.getMessage();
            } finally {
                Log.i("done", "jisaSuccessListener done");
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

    private void requestdownFilePOST(int seq)
    {

        String downloadUrl = downFileUrl + "?inq_map_sn=" + seq;
        String downPath = util.getSDPath() + File.separator + "MRNS";

        File delDir = Environment.getExternalStoragePublicDirectory(downPath + File.separator + downfileName);
        if(delDir.isFile()){
            delDir.delete();
        }

        String targetFileName = downPath + File.separator + downfileName;
        new DownloadMAPAsync(getContext()).execute(downloadUrl, targetFileName);

    }

    private void requestdownListPOST()
    {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        Request request = new StringRequest(Request.Method.POST, downListUrl, successListener, failListener)
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
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> successListener = new Response.Listener<String>()
    {

        @Override
        public void onResponse(String reponse)
        {
            doMakeDownMapList(reponse);
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


    void doMakeDownMapList(String str){

        JSONObject jObject;
        JSONArray bList;

        try {
            //JSONObject set
            JSONObject jObj = new JSONObject(str);
            JSONObject totList = jObj.getJSONObject("listXmlReturnVO");
//            pageSize = totList.getInt("blockCount");
            totPage = totList.getInt("totPage");

            //JSONArray Set
            bList = new JSONArray(totList.getJSONArray("resultList").toString());
            listCount = bList.length();
            mAdapter = new MapList_Item_Adapter(getContext());
            Resources mRes = getResources();

            for (int i = 0; i < listCount; i++) {
                jObject = bList.getJSONObject(i);  // JSONObject 추출
                int inq_map_sn = jObject.getInt("inq_map_sn");
                String subject = jObject.getString("subject");
                String wrt_de = jObject.getString("wrt_de");
                String wrt_id = jObject.getString("wrt_id");
                String atch_nm = jObject.getString("atch_nm");
                int startSeq = jObject.getInt("startSeq");
                int endSeq = jObject.getInt("endSeq");

                mAdapter.addItem(new MapList_Item(inq_map_sn, mRes.getDrawable(R.drawable.btn_radio_n), subject, wrt_de, atch_nm));

            }
        }catch (JSONException e){
            Log.e(getString(R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "doMakeDownMapList done");
        }

        //Data Binding
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        //ListView Position Location Set
        mList.post(new Runnable() {
            @Override
            public void run() {
                int pos;
                if (pageNo == 1) {
                    pos = 0;
                } else {
                    pos = (pageNo - 1) * pageSize;
                }

                mList.setSelection(pos);
            }
        });

        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MapList_Item mapItem = (MapList_Item) mAdapter.getItem(position);

                Drawable selectedIcon = getResources().getDrawable(R.drawable.btn_radio_p);
                Drawable nonSelectedIcon = getResources().getDrawable(R.drawable.btn_radio_n);

                for(int i=0; i< mAdapter.getCount();  i++){
                    if(position == i) {
                        mapItem.setIcon(selectedIcon);
                    } else {
                        MapList_Item nonItem = (MapList_Item) mAdapter.getItem(i);
                        nonItem.setIcon(nonSelectedIcon);
                    }

                }//end for

                //getData
                seqMapSeq = mapItem.getMapSeq();
                subject = mapItem.getData(0);
                downfileName = mapItem.getData(2);


                mAdapter.notifyDataSetChanged();

            }//end onItemClick
        });//end setOnItemClickListener


        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    pageNo = pageNo + 1;
                    requestdownListPOST();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                lastitemVisibleFlag = (listCount > 0 && (firstVisibleItem + visibleItemCount) == totalItemCount && pageNo < totPage);

            }
        });



    }




}
