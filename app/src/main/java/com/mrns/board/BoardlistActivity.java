package com.mrns.board;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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

public class BoardlistActivity extends BaseActivity {

    private boolean bOnline;
    ListView boardList;
    Boardlist_Item_Adapter bAdapter;
    private Intent movePage;
    ImageView btn_back;
    TextView down_title;
    String bListUrl;
    int totPage = 1;
    int pageNo = 1;
    int pageSize = 10;
    boolean lastitemVisibleFlag = false;
    int listCount = 0;
    int newCnt = 0;
    private NetcheckMoniter networkMoniter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardlist);

        Intent intent = getIntent();
        bOnline = intent.getBooleanExtra(getString(R.string.key_bonline), bOnline);

        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        boardList = (ListView) findViewById(R.id.BoardList);
        bAdapter = new Boardlist_Item_Adapter(this);

        //url
        bListUrl = getString(R.string.server_ip) + getString(R.string.req_boardlist);
        requestBoardListPOST();

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
                        movePage = new Intent(BoardlistActivity.this, MainActivity.class);
                        movePage.putExtra(getString(R.string.key_bonline), bOnline);
                        startActivity(movePage);
                        break;
                }//end switch
            }//end Onclick
        };

        btn_back.setOnClickListener(listener);
        down_title.setOnClickListener(listener);

    }

    private void requestBoardListPOST()
    {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, bListUrl, successListener, failListener)
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
            doMakeBoardList(reponse);
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

    void doMakeBoardList(String str){

        JSONObject jObject;
        JSONArray bList;

        try {
            //JSONObject set {
            JSONObject jObj = new JSONObject(str);
            JSONObject totList = jObj.getJSONObject("listXmlReturnVO");
            pageSize = totList.getInt("blockCount");
            totPage = totList.getInt("totPage");

            //JSONArray Set
            bList = new JSONArray(totList.getJSONArray("resultList").toString());
            listCount = bList.length();

            Resources mRes = getResources();
            Drawable icon = mRes.getDrawable(R.drawable.ic_notice_arrow_p);
            Drawable icon1 = mRes.getDrawable(R.drawable.ic_notice_new);
            for (int i = 0; i < listCount; i++) {
                jObject = bList.getJSONObject(i);  // JSONObject 추출
                int bbs_sn = jObject.getInt("bbs_sn");
                String bbs_secd = jObject.getString("bbs_secd");
                String bbs_sj = jObject.getString("bbs_sj");
                String rgsdt = jObject.getString("rgsdt");
                int rdcnt = jObject.getInt("rdcnt");
                int bbs_comment_sn = jObject.getInt("bbs_comment_sn");
                int comment_ordr = jObject.getInt("comment_ordr");
                int cnt = jObject.getInt("cnt");
                int startSeq = jObject.getInt("startSeq");
                int endSeq = jObject.getInt("endSeq");
                if(i==0 && newCnt == 0){
                    bAdapter.addItem(new Boardlist_Item(bbs_sn,icon1, bbs_sj, rgsdt));
                    newCnt ++;
                }else{
                    bAdapter.addItem(new Boardlist_Item(bbs_sn,icon, bbs_sj, rgsdt));
                }


            }
        }catch (JSONException e){
            Log.e(getString(R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "doMakeBoardList done");
        }

        //Data Binding
        boardList.setAdapter(bAdapter);

        //ListView Position Location Set
        boardList.post(new Runnable() {
            @Override
            public void run() {
                int pos;
                if (pageNo == 1) {
                    pos = 0;
                } else {
                    pos = (pageNo - 1) * pageSize;
                }

                boardList.setSelection(pos);
            }
        });

        //ListView Onclick Event
        boardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Boardlist_Item bItem = (Boardlist_Item) bAdapter.getItem(position);

                //getData
                int selBoardSeq = bItem.getBoard_seq();
                String[] selBoardData = bItem.getData();
                movePage = new Intent(BoardlistActivity.this, BoarddetailActivity.class);
                movePage.putExtra("boardSeq", selBoardSeq);
                movePage.putExtra("selBoardData", selBoardData);

                startActivity(movePage);



            }
        });


        boardList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    pageNo = pageNo + 1;
                    requestBoardListPOST();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                lastitemVisibleFlag = (listCount > 0 && (firstVisibleItem + visibleItemCount) == totalItemCount && pageNo < totPage);

            }
        });



    }


}
