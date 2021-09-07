package com.mrns.invest.innerDetail.history;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.mrns.invest.adapter.listener.ClickListener;
import com.mrns.invest.record.RnsSttusInqHist;
import com.mrns.invest.util.EndlessRecyclerOnScrollListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


@EActivity(com.mrns.main.R.layout.activity_invest_history_list)
public class investHistoryListActivity extends AppCompatActivity {

    @ViewById(com.mrns.main.R.id.textview_sub_title)
    TextView textviewSubTitle;
    @ViewById(com.mrns.main.R.id.recycler_list)
    RecyclerView recyclerList;

    InvestHisoryListAdapter mAdapter;
    ArrayList<RnsSttusInqHist> mItems;
    boolean mIsDesc = false;
    String mUrl;
    int mPageNum;
    boolean mLoadMore = true;
    static int mBlockCount = 10;
    private NetcheckMoniter networkMoniter = null;
    LinearLayoutManager mLayoutManager;

    @AfterViews
    protected void InitView() {
        mLoadMore = true;
        final boolean bOnline = getIntent().getBooleanExtra(getString(com.mrns.main.R.string.key_bonline), false);
        final long stdrspotSn = getIntent().getLongExtra(getString(com.mrns.main.R.string.key_stdr_spot_sn), 0);
        final String title = getIntent().getStringExtra(getString(com.mrns.main.R.string.key_title));
        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mPageNum = 1;
        textviewSubTitle.setText(title);
        mIsDesc = false;
        mItems = new ArrayList<>();
        mUrl = getString(com.mrns.main.R.string.server_ip) + getString(com.mrns.main.R.string.url_liststtusinqhist);

        mAdapter = new InvestHisoryListAdapter(investHistoryListActivity.this);
        mAdapter.setItems(mItems);
        mAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = investHistoryDetailActivity_.intent(investHistoryListActivity.this).get();
                intent.putExtra(getString(com.mrns.main.R.string.key_bonline), bOnline);
                intent.putExtra(getString(com.mrns.main.R.string.key_title), title);
                intent.putExtra("SttusSn", mItems.get(position).getSttusSn());
                startActivity(intent);
            }

            @Override
            public void onLocationClick(int position, View v) {
            }
        });
        mLayoutManager = new LinearLayoutManager(investHistoryListActivity.this);
        recyclerList.setLayoutManager(mLayoutManager);
        recyclerList.setHasFixedSize(true);
        recyclerList.setAdapter(mAdapter);
        recyclerList.setItemAnimator(new DefaultItemAnimator());
        recyclerList.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (mLoadMore) {
                    mPageNum++;
                    requestListHistoryPOST(stdrspotSn, mPageNum, mBlockCount);
                }

            }
        });
        requestListHistoryPOST(stdrspotSn, mPageNum, mBlockCount );
    }



    @UiThread
    public void notifyData() {
        mAdapter.notifyDataSetChanged();
    }

    @Click(com.mrns.main.R.id.imagebtn_back_home)
    public void onBack() {
        super.onBackPressed();
    }

    @UiThread
    @Click(com.mrns.main.R.id.layout_sort_by_inq_date)
    public void sortInqde() {
        if (mIsDesc) {
            //ib_invest_sort_install.setBackgroundResource(R.drawable.img_list_arrow_up);
            mIsDesc = false;
            Collections.sort(mItems, new InqDeDescCompare());
        } else {
            //ib_invest_sort_install.setBackgroundResource(R.drawable.img_list_arrow_down);
            mIsDesc = true;
            Collections.sort(mItems, new InqDeAscCompare());
        }
    }


    //오름차순(ASC)
    static class InqDeAscCompare implements Comparator<RnsSttusInqHist> {
        @Override
        public int compare(RnsSttusInqHist arg0, RnsSttusInqHist arg1) {
            return arg0.getInqDe().compareTo(arg1.getInqDe());
        }
    }

    //내림차순(DESC)
    static class InqDeDescCompare implements Comparator<RnsSttusInqHist> {
        @Override
        public int compare(RnsSttusInqHist arg0, RnsSttusInqHist arg1) {
            return arg1.getInqDe().compareTo(arg0.getInqDe());
        }
    }

    protected void requestListHistoryPOST(final long stdrspot_sn, final int pageNum, final int blockCount) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, mUrl, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(com.mrns.main.R.string.key_stdr_spot_sn_json), String.valueOf(stdrspot_sn));
                hash.put("pageNum", String.valueOf(pageNum));
                hash.put("blockCount", String.valueOf(blockCount));
                return hash;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(com.mrns.main.R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    protected Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            investHistroyJsonPaser(reponse);
        }
    };

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {

        }
    };

    protected void investHistroyJsonPaser(String str) {
        JSONObject jObject;
        try {
            //JSONObject set {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");

            mLoadMore = !(bDetail.getInt("totPage") == bDetail.getInt("pageNum"));
            JSONArray list = bDetail.getJSONArray("resultList");

            for (int i = 0; i < list.length(); i++) {
                jObject = list.getJSONObject(i);  // JSONObject 추출
                RnsSttusInqHist hist = new RnsSttusInqHist();
                hist.setSttusSn(jObject.getLong("sttus_sn"));
                hist.setInqDe(jObject.getString("inq_de"));
                hist.setInqNm(jObject.getString("inq_nm"));
                hist.setInqInsttCd(jObject.getString("inq_instt_cd"));
                mItems.add(hist);
            }
            notifyData();

        } catch (JSONException e){
            Log.e(getString(com.mrns.main.R.string.jSONException), e.getMessage());
        }catch (Exception e){
            Log.e(getString(com.mrns.main.R.string.exception), e.getMessage());
        }finally {
            Log.i(getString(com.mrns.main.R.string.done), "investHistroyJsonPaser done");
        }
    }
}
