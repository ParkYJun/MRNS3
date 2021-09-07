package com.mrns.board;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.common.NetcheckMoniter;
import com.mrns.main.R;
import com.mrns.utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class  BoarddetailActivity extends BaseActivity {

    ImageView btn_back;
    TextView down_title;
    String bDetailUrl;
    TextView bDetailTitle;
    TextView bDetailContent;
    TextView bDetailDate;
    LinearLayout fileLayout;
    int boardSeq;
    private NetcheckMoniter networkMoniter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarddetail);

        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        Intent intent = getIntent();
        boardSeq = intent.getIntExtra("boardSeq", boardSeq);

        bDetailTitle = (TextView) findViewById(R.id.boarddetail_title);
        bDetailContent = (TextView) findViewById(R.id.boarddetail_body);
        bDetailDate = (TextView) findViewById(R.id.board_detail_date);
        fileLayout = (LinearLayout) findViewById(R.id.layout_file);
        fileLayout.setVisibility(View.GONE);

        //뒤로가기 버튼
        btn_back = (ImageView) findViewById(R.id.btn_nav_back);
        down_title = (TextView) findViewById(R.id.titleTextView);
        ViewPager.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_nav_back :
                    case R.id.titleTextView :
                        finish();
                        break;
                }//end switch
            }//end Onclick
        };

        btn_back.setOnClickListener(listener);
        down_title.setOnClickListener(listener);


        bDetailUrl = getString(R.string.server_ip) + getString(R.string.req_boarddetail);
        requestBoardDetailPOST();

    }

    private void requestBoardDetailPOST()
    {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, bDetailUrl, successListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("bbs_sn", String.valueOf(boardSeq));
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
            doMakeBoardDetail(reponse);
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

    void doMakeBoardDetail(String str){
        JSONObject jObject;
        JSONArray bList;

        try {
            //JSONObject set {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject bBody = bDetail.getJSONObject("resultVO");

            int bbs_sn = bBody.getInt("bbs_sn");
            String bbs_sj = bBody.getString("bbs_sj");
            String bbs_cn = bBody.getString("bbs_cn");
            String rgsdt = bBody.getString("rgsdt");

            bDetailTitle.setText(bbs_sj);
            bDetailContent.setText(bbs_cn);
            bDetailDate.setText(rgsdt);

        }catch (JSONException e) {
            Log.e(getString(R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "doMakeBoardDetail done");
        }
    }

}
