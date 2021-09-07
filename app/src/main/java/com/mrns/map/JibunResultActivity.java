package com.mrns.map;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.mrns.invest.adapter.listener.ClickListener;
import com.mrns.invest.util.EndlessRecyclerOnScrollListener;
import com.mrns.invest.util.FineDialog;
import com.mrns.main.R;
import com.mrns.map.adapter.JibunResultListAdapter;
import com.mrns.map.record.AddrSearchResultRecord;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_jibun_result)
public class JibunResultActivity extends AppCompatActivity {
    ArrayList<AddrSearchResultRecord> mItems;
    JibunResultListAdapter mAdapter;
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
    protected void InitView() {
        mLoadMore = true;
        mPageNum = 1;

        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mItems = new ArrayList<>();
        mAdapter = new JibunResultListAdapter(JibunResultActivity.this);
        mAdapter.setItems(mItems);
        mAdapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                //objectid 이용해서 연속도로 쿼리날리는, 타입을 만들어야하네
                String objectid = String.valueOf(mItems.get(position).getObjectid());
                Intent moveIntent = new Intent(JibunResultActivity.this, MapWebActivity.class);
                moveIntent.putExtra(getString(R.string.key_mode), 0);
                moveIntent.putExtra(getString(R.string.key_type), "10");
                moveIntent.putExtra(getString(R.string.key_fid), objectid);

                startActivity(moveIntent);
            }

            @Override
            public void onLocationClick(int position, View v) {}
        });

        mLayoutManager = new LinearLayoutManager(JibunResultActivity.this);
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
        loadingDialog(true);
        final Bundle bundle = getIntent().getExtras();
        String url = getString(R.string.server_ip) + getString(R.string.url_search_parcel);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(JibunResultActivity.this);
        Request request = new StringRequest(Request.Method.POST, url, successListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> hash = new HashMap<>();

                hash.put("pageNum", String.valueOf(mPageNum));
                hash.put("blockCount", String.valueOf(BLOCK_COUNT));
                hash.put(getString(R.string.key_adm_code), bundle.getString(getString(R.string.key_adm_code)));
                hash.put(getString(R.string.key_emd_name), bundle.getString(getString(R.string.key_emd_name)));
                hash.put(getString(R.string.key_reg_code), bundle.getString(getString(R.string.key_reg_code)));
                hash.put(getString(R.string.key_main_parcel_no), bundle.getString(getString(R.string.key_main_parcel_no)));
                hash.put(getString(R.string.key_sub_parcel_no), bundle.getString(getString(R.string.key_sub_parcel_no)));

                return hash;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
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

                JSONArray parcelArr = new JSONArray(obj.getJSONArray("resultList").toString());

                for(int i=0; i< parcelArr.length(); i++) {
                    jObject = parcelArr.getJSONObject(i);
                    String objecitd = jObject.getString(getString(R.string.key_objectid));
                    String parcelAddress = jObject.getString(getString(R.string.key_parcel_address));

                    AddrSearchResultRecord record = new AddrSearchResultRecord(
                            objecitd,
                            parcelAddress
                    );

                    mItems.add(record);
                }

                mAdapter.notifyDataSetChanged();

                String title = String.format("%s\n(총 %s건 중 %s건)", getResources().getString(R.string.title_activity_build_result), obj.getInt("totalCount"), mItems.size());
                textviewTitle.setText(title);

            } catch (JSONException e) {
                loadingDialog(false);
                Log.e(getString(R.string.jSONException), e.getMessage());
            } catch (Exception e) {
                Log.e(getString(R.string.exception), e.getMessage());
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
            Toast.makeText(JibunResultActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    };

    @UiThread
    public void loadingDialog(boolean view) {
        if(view)
            FineDialog.showProgress(JibunResultActivity.this, false, getResources().getString(R.string.str_invest_loading));
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
        Intent movePage = new Intent(JibunResultActivity.this, MainActivity.class);
        movePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        movePage.putExtra(getString(R.string.key_bonline), true);
        startActivity(movePage);
        finish();
    }
*/
}