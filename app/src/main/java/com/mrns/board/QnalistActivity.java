package com.mrns.board;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.mrns.main.MainActivity;
import com.mrns.main.R;
import com.mrns.utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QnalistActivity extends BaseActivity {

    private boolean bOnline;
    ListView qnaList;
    Qnalist_Item_Adapter qAdapter;
    private Intent movePage;
    ImageView btn_back;
    TextView down_title;
    String qListUrl;
    int totPage = 1;
    int pageNo = 1;
    int pageSize = 10;
    boolean lastitemVisibleFlag = false;
    int listCount = 0;
    private NetcheckMoniter networkMoniter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qnalist);

        Intent intent = getIntent();
        bOnline = intent.getBooleanExtra(getString(R.string.key_bonline), bOnline);

        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        qnaList = (ListView) findViewById(R.id.qnaList);
        qAdapter = new Qnalist_Item_Adapter(this);

        //url
        qListUrl = getString(R.string.server_ip) + getString(R.string.req_qnalist);
        requestQnaListPOST();

        //뒤로가기 버튼
        btn_back = (ImageView) findViewById(R.id.btn_nav_back);
        down_title = (TextView) findViewById(R.id.titleTextView);
        ViewPager.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btn_nav_back :
                    case R.id.titleTextView :
                        btn_back.setImageResource(R.drawable.nav_btn_back_p);
                        movePage = new Intent(QnalistActivity.this, MainActivity.class);
                        movePage.putExtra(getString(R.string.key_bonline), bOnline);
                        startActivity(movePage);
                        break;
                }//end switch
            }//end Onclick
        };

        btn_back.setOnClickListener(listener);
        down_title.setOnClickListener(listener);
    }

    private void requestQnaListPOST()
    {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, qListUrl, successListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {

                HashMap<String, String> hash = new HashMap<>();
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
            doMakeQnaList(reponse);
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

    void doMakeQnaList(String str) {

        JSONObject jObject;
        JSONArray qList;

        try {
            //JSONObject set {
            JSONObject jObj = new JSONObject(str);
            JSONObject totList = jObj.getJSONObject("listXmlReturnVO");
            pageSize = totList.getInt("blockCount");
            totPage = totList.getInt("totPage");

            //JSONArray Set
            qList = new JSONArray(totList.getJSONArray("resultList").toString());
            listCount = qList.length();

            for (int i = 0; i < listCount; i++) {

                jObject = qList.getJSONObject(i);  // JSONObject 추출

                String user_psitn_cd = jObject.getString("user_psitn_cd");
                int bbs_comment_sn = jObject.getInt("bbs_comment_sn");
                int comment_ordr = jObject.getInt("comment_ordr");
                String bbs_cn = jObject.getString("bbs_cn");
                int bbs_sn = jObject.getInt("bbs_sn");
                String usid = jObject.getString("usid");
                String bbs_secd = jObject.getString("bbs_secd");
                String rgsdt = jObject.getString("rgsdt");
                String bbs_sj = jObject.getString("bbs_sj");
                int rdcnt = jObject.getInt("rdcnt");
                int startSeq = jObject.getInt("startSeq");
                int endSeq = jObject.getInt("endSeq");
                String comment_cm = jObject.getString("comment_cm");
                int cnt = jObject.getInt("cnt");

                qAdapter.addItem(new Qnalist_Item(bbs_sn, cnt, bbs_sj, usid, rgsdt));
            }
        }catch (JSONException e){
            Log.e(getString(R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "doMakeQnaList done");
        }

        //Data Binding
        qnaList.setAdapter(qAdapter);

        //ListView Position Location Set
        qnaList.post(new Runnable() {
            @Override
            public void run() {
                int pos;
                if (pageNo == 1) {
                    pos = 0;
                } else {
                    pos = (pageNo - 1) * pageSize;
                }

                qnaList.setSelection(pos);
            }
        });


        //ListView Onclick Event
        qnaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Qnalist_Item qItem = (Qnalist_Item) qAdapter.getItem(position);

                //getData
                int selQnaSeq = qItem.getQnaSeq();
                int ansCnt = qItem.getAnsCount();
                movePage = new Intent(QnalistActivity.this, QnadetailActivity.class);
                movePage.putExtra("qnaSeq", selQnaSeq);
                movePage.putExtra("ansCnt", ansCnt);

                startActivity(movePage);


            }
        });

        qnaList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    pageNo = pageNo + 1;
                    requestQnaListPOST();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                lastitemVisibleFlag = (listCount > 0 && (firstVisibleItem + visibleItemCount) == totalItemCount && pageNo < totPage);

            }
        });


    }











}
