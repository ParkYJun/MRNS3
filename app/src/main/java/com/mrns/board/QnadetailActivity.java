package com.mrns.board;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class QnadetailActivity extends AppCompatActivity {

    ListView qnaDetail_list;
    Qnadetail_Item_Adapter qdAdapter;
    private Intent movePage;
    ImageView btn_back;
    TextView down_title;
    int qnaSeq;
    String qDetailUrl;
    int totPage = 1;
    int pageNo = 1;
    int pageSize = 5;
    boolean lastitemVisibleFlag = false;
    int listCount = 0;

    TextView qnadetail_q_title;
    TextView qnadetail_q_body;
    TextView qna_name;
    TextView qna_date;
    TextView qna_detail_count;

    String dTitle;
    String dContent;
    String dDate;
    String dUserid;
    String dUserName;
    int ansCnt;
    private NetcheckMoniter networkMoniter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.mrns.main.R.layout.activity_qnadetail);
//        movePage.putExtra("selQnaData", selQnaData);

        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        Intent intent = getIntent();
        qnaSeq = intent.getIntExtra("qnaSeq", qnaSeq);
        ansCnt = intent.getIntExtra("ansCnt", ansCnt);

        qnaDetail_list = (ListView) findViewById(com.mrns.main.R.id.answer_list);
        qdAdapter = new Qnadetail_Item_Adapter(this);

        //뒤로가기 버튼
        btn_back = (ImageView) findViewById(com.mrns.main.R.id.btn_nav_back);
        down_title = (TextView) findViewById(com.mrns.main.R.id.titleTextView);
        qna_detail_count = (TextView) findViewById(com.mrns.main.R.id.qna_detail_count);
        qna_detail_count.setText(String.valueOf(ansCnt));

        ViewPager.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case com.mrns.main.R.id.btn_nav_back :
                    case com.mrns.main.R.id.titleTextView :
//                        btn_back.setImageResource(R.drawable.nav_btn_back_p);
//                        movePage = new Intent(QnadetailActivity.this, QnalistActivity.class);
//                        startActivity(movePage);
                        finish();
                        break;
                }//end switch
            }//end Onclick
        };

        btn_back.setOnClickListener(listener);
        down_title.setOnClickListener(listener);

        qnadetail_q_title = (TextView) findViewById(com.mrns.main.R.id.qnadetail_q_title);
        qnadetail_q_body = (TextView) findViewById(com.mrns.main.R.id.qnadetail_q_body);
        qna_name = (TextView) findViewById(com.mrns.main.R.id.qna_name);
        qna_date = (TextView) findViewById(com.mrns.main.R.id.qna_date);


        qDetailUrl = getString(com.mrns.main.R.string.server_ip) + getString(com.mrns.main.R.string.req_qnadetail);
        requestQnaDetailPOST();


    }

    private void requestQnaDetailPOST()
    {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, qDetailUrl, successListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {

                HashMap<String, String> hash = new HashMap<>();
                hash.put("bbs_sn", String.valueOf(qnaSeq));
                hash.put("pageNum", String.valueOf(pageNo));
                hash.put("blockCount", String.valueOf(pageSize));

                return hash;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(com.mrns.main.R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> successListener = new Response.Listener<String>()
    {

        @Override
        public void onResponse(String reponse)
        {
            doMakeQnaDetail(reponse);
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

    void doMakeQnaDetail(String str) {

        JSONObject jObject;
        JSONArray qdList;

        try {
            //JSONObject set {
            JSONObject jObj = new JSONObject(str);
            JSONObject qDetail = jObj.getJSONObject("listXmlReturnVO");

            JSONObject qBody = qDetail.getJSONObject("resultVO");
            pageSize = qDetail.getInt("blockCount");
            totPage = qDetail.getInt("totPage");

            dTitle = qBody.getString("bbs_sj");
            dContent = qBody.getString("bbs_cn");
            dUserid = qBody.getString("usid");
            dDate = qBody.getString("rgsdt");

            qnadetail_q_title.setText(dTitle);
            qnadetail_q_body.setText(dContent);
            qna_name.setText(dUserid);
            qna_date.setText(dDate);

            //JSONArray Set
            qdList = new JSONArray(qDetail.getJSONArray("resultList").toString());
            listCount = qdList.length();

            if(listCount > 0 ) {
                for (int i = 0; i < listCount; i++) {
                    jObject = qdList.getJSONObject(i);  // JSONObject 추출

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

                    qdAdapter.addItem(new Qnadetail_Item(comment_cm, usid, rgsdt));
                }
            } else {
                qdAdapter.addItem(new Qnadetail_Item("댓글이 없습니다", "", ""));
            }

        }catch (JSONException e){
            Log.e(getString(com.mrns.main.R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(com.mrns.main.R.string.done), "doMakeQnaDetail done");
        }

        //Data Binding
        qnaDetail_list.setAdapter(qdAdapter);

        //ListView Position Location Set
        qnaDetail_list.post(new Runnable() {
            @Override
            public void run() {
                int pos;
                if (pageNo == 1) {
                    pos = 0;
                } else {
                    pos = (pageNo - 1) * pageSize;
                }

                qnaDetail_list.setSelection(pos);
            }
        });

        //ListView Onclick Event
        qnaDetail_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Qnadetail_Item qItem = (Qnadetail_Item) qdAdapter.getItem(position);

            }
        });

        qnaDetail_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    pageNo = pageNo + 1;
                    requestQnaDetailPOST();
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                lastitemVisibleFlag = (listCount > 0 && (firstVisibleItem + visibleItemCount) == totalItemCount && pageNo < totPage);

            }
        });

    }
}
