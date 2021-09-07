package com.mrns.map;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
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
import com.mrns.invest.util.EndlessRecyclerOnScrollListener;
import com.mrns.invest.util.FineDialog;
import com.mrns.main.R;
import com.mrns.map.adapter.BuildResultListAdapter;
import com.mrns.map.record.AddrSearchResultRecord;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_build_result)
public class BuildResultActivity extends AppCompatActivity {
    ArrayList<AddrSearchResultRecord> mItems;
    BuildResultListAdapter mAdapter;
    int socketTimeout = 30000;
    boolean bOnline;

    @ViewById(R.id.textview_title)
    TextView textviewTitle;
    @ViewById(R.id.recycler_list)
    RecyclerView recyclerList;
    private NetcheckMoniter networkMoniter = null;
    LinearLayoutManager mLayoutManager;
    int mPageNum;
    boolean mLoadMore = true;
    static int BLOCK_COUNT = 10;

    @AfterViews
    protected void InitView(){
        mLoadMore = true;
        mPageNum = 1;
        bOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), false);
        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mItems = new ArrayList<>();
        mAdapter = new BuildResultListAdapter(BuildResultActivity.this);
        mAdapter.setItems(mItems);
        mAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent moveIntent = new Intent(BuildResultActivity.this, MapWebActivity.class);

                String objectid = String.valueOf(mItems.get(position).getObjectid());
                moveIntent.putExtra(getString(R.string.key_mode), 0);
                moveIntent.putExtra(getString(R.string.key_type), "0");
                moveIntent.putExtra(getString(R.string.key_fid), objectid);

                startActivity(moveIntent);
            }

            @Override
            public void onLocationClick(int position, View v) {
            }
        });

        mLayoutManager = new LinearLayoutManager(BuildResultActivity.this);
        recyclerList.setLayoutManager(mLayoutManager);
        recyclerList.setHasFixedSize(true);
        recyclerList.setAdapter(mAdapter);
        recyclerList.setItemAnimator(new DefaultItemAnimator());
        recyclerList.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (mLoadMore) {
                    mPageNum++;
                    getItems();
                }
            }
        });
        getItems();
    }

    public void getItems() {
        final Bundle bundle = getIntent().getExtras();
        String url = getString(R.string.server_ip) + getString(R.string.url_search_road_address);

        loadingDialog(true);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(BuildResultActivity.this);
        Request request = new StringRequest(Request.Method.POST, url, successListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                String admCode = bundle.getString(getString(R.string.key_adm_code));
                String roadName = bundle.getString(getString(R.string.key_road_name));
                String startBuildingNo = bundle.getString(getString(R.string.key_start_building_no));
                String endBuildingNo = bundle.getString(getString(R.string.key_end_building_no));
                String buildingName = bundle.getString(getString(R.string.key_building_name));
                String empty = getString(R.string.empty);

                HashMap<String, String> hash = new HashMap<>();
                hash.put("pageNum", String.valueOf(mPageNum));
                hash.put("blockCount", String.valueOf(BLOCK_COUNT));
                hash.put(getString(R.string.key_adm_code), admCode);
                hash.put(getString(R.string.key_road_name), roadName == null ? empty : roadName);
                hash.put(getString(R.string.key_start_building_no), startBuildingNo == null ? empty : startBuildingNo);
                hash.put(getString(R.string.key_end_building_no), endBuildingNo == null ? empty : endBuildingNo);
                hash.put(getString(R.string.key_building_name), buildingName == null ? empty : buildingName);

                return hash;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse)
        {
            JSONObject jObject;

            try {
                JSONObject jObj = new JSONObject(reponse);
                JSONObject obj = jObj.getJSONObject("listXmlReturnVO");
                mLoadMore = !(obj.getInt("totPage") == obj.getInt("pageNum"));
                JSONArray buildArr = new JSONArray(obj.getJSONArray("resultList").toString());

                for(int i=0; i< buildArr.length(); i++) {
                    jObject = buildArr.getJSONObject(i);
                    String objectid = jObject.getString(getString(R.string.key_objectid));
                    String roadAddress = jObject.getString(getString(R.string.key_road_address));

                    AddrSearchResultRecord record = new AddrSearchResultRecord(
                            objectid,
                            roadAddress
                    );

                    mItems.add(record);
                }

                mAdapter.notifyDataSetChanged();

                String title = String.format("%s\n(총 %s건 중 %s건)", getResources().getString(R.string.title_activity_build_result), obj.getInt("totalCount"), mItems.size());
                textviewTitle.setText(title);
            } catch (JSONException e) {
                Log.e(getString(R.string.jSONException), e.getMessage());
            }finally {
                loadingDialog(false);
                Log.i(getString(R.string.done), "successListener done");
            }

        }

    };

    private Response.ErrorListener failListener = new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError e)
        {
            loadingDialog(false);
            e.getMessage();
        }
    };

    @UiThread
    public void loadingDialog(boolean view) {
        if(view)
            FineDialog.showProgress(BuildResultActivity.this, false, getResources().getString(R.string.str_invest_loading));
        else
            FineDialog.hideProgress();
    }

    @Click({R.id.imagebtn_back, R.id.textview_title})
    public void onBack() {
        super.onBackPressed();
    }

    /*
    @Click(R.id.ib_map_result_nav_home)
    public void onHome(){
        Intent movePage = new Intent(BuildResultActivity.this, MainActivity.class);
        movePage.putExtra(getString(R.string.key_bonline), true);
        movePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(movePage);
        finish();
    }
*/
}
