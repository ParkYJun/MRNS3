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
import com.mrns.invest.util.EndlessRecyclerOnScrollListener;
import com.mrns.invest.util.FineDialog;
import com.mrns.main.R;
import com.mrns.map.adapter.PointResultListAdapter;
import com.mrns.map.adapter.listener.ClickListener;
import com.mrns.map.record.PointResultRecord;

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

@EActivity(R.layout.activity_point_result)
public class PointResultActivity extends AppCompatActivity {
    ArrayList<PointResultRecord> items;
    @ViewById(R.id.textview_title)
    TextView textviewTitle;
    @ViewById(R.id.recycler_list)
    RecyclerView recyclerList;

    boolean isInstDESC = false;
    boolean isInvestDESC = false;
    PointResultListAdapter adapter;
    int mMode = 1;
    boolean mBOnline;
    private NetcheckMoniter networkMoniter = null;

    LinearLayoutManager mLayoutManager;
    int mPageNum;
    boolean mLoadMore = true;
    static int BLOCK_COUNT = 10;

    @AfterViews
    protected void InitView() {
        mLoadMore = true;
        mPageNum = 1;
        mBOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), false);
        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        isInstDESC = false;
        isInvestDESC = false;
        items = new ArrayList<>();
        adapter = new PointResultListAdapter(PointResultActivity.this);
        adapter.setItems(items);
        adapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent movePage = new Intent(PointResultActivity.this, MapWebActivity.class);
                movePage.putExtra(getString(R.string.key_bonline), mBOnline);
                movePage.putExtra(getString(R.string.key_mode), mMode);
                //지적, 국가, 공공 고려
                String pointSecd = items.get(position).getPointSecd();
                String keyValue;
                String type;
                PointResultRecord record = items.get(position);

                switch (pointSecd) {
                    case "0":
                        keyValue = record.getNpuSn();
                        type = getString(R.string.key_national_point_type_for_map_query);
                        break;
                    case "1":
                        keyValue = record.getNpuSn();
                        type = getString(R.string.key_common_point_type_for_map_query);
                        break;
                    case "2":
                    default:
                        keyValue = record.getBasePointNo();
                        type = getString(R.string.key_point_type_for_map_query);
                }

                String stdrspotSn = record.getStdrspotSn();
                String npuSn = record.getNpuSn();
                String pointName = record.getPointName();
                String admCode = record.getAdmCode();

                movePage.putExtra(getString(R.string.key_fid), keyValue);
                movePage.putExtra(getString(R.string.key_stdr_spot_sn), stdrspotSn.equals("null") ? 0 : Long.valueOf(stdrspotSn));
                movePage.putExtra(getString(R.string.key_npu_sn), npuSn.equals("null") ? 0 : Long.valueOf(record.getNpuSn()));
                movePage.putExtra(getString(R.string.key_base_point_no), record.getBasePointNo());
                movePage.putExtra(getString(R.string.key_type), type);
                movePage.putExtra(getString(R.string.key_point_name), pointName);
                movePage.putExtra(getString(R.string.key_adm_code), admCode);
                movePage.putExtra(getString(R.string.key_map_search_move), true);

                startActivity(movePage);
            }
        });
        mLayoutManager = new LinearLayoutManager(PointResultActivity.this);
        recyclerList.setLayoutManager(mLayoutManager);
        recyclerList.setHasFixedSize(true);
        recyclerList.setAdapter(adapter);
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

    @UiThread
    public void getItems() {
        loadingDialog(true);

        final Bundle bundle = getIntent().getExtras();

        String pointSearchUrl = getString(R.string.server_ip) + getString(R.string.url_search_points);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(PointResultActivity.this);
        Request request = new StringRequest(Request.Method.POST, pointSearchUrl, successListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("pageNum", String.valueOf(mPageNum));
                hash.put("blockCount", String.valueOf(BLOCK_COUNT));
                hash.put(getString(R.string.key_bonline), String.valueOf(mBOnline));
                hash.put(getString(R.string.key_adm_code), bundle.getString(getString(R.string.key_adm_code)));
                hash.put(getString(R.string.key_emd_name), bundle.getString(getString(R.string.key_emd_name)));
                hash.put(getString(R.string.key_point_class_code), bundle.getString(getString(R.string.key_point_class_code)));
                hash.put(getString(R.string.key_point_kind_code), bundle.getString(getString(R.string.key_point_kind_code)));
                hash.put(getString(R.string.key_start_date), bundle.getString(getString(R.string.key_start_date)));
                hash.put(getString(R.string.key_end_date), bundle.getString(getString(R.string.key_end_date)));
                hash.put(getString(R.string.key_point_name), bundle.getString(getString(R.string.key_point_name)));
                hash.put(getString(R.string.key_start_point_no), bundle.getString(getString(R.string.key_start_point_no)));
                hash.put(getString(R.string.key_end_point_no), bundle.getString(getString(R.string.key_end_point_no)));
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
                JSONArray pointArr = new JSONArray(obj.getJSONArray("resultList").toString());

                for(int i=0; i< pointArr.length(); i++) {
                    jObject = pointArr.getJSONObject(i);
                    String stdrspotSn = jObject.getString(getString(R.string.key_stdr_spot_sn));
                    String basePointNo = jObject.getString(getString(R.string.key_base_point_no));
                    String pointKindCode = jObject.getString(getString(R.string.key_point_kind_code));
                    String pointSecd = jObject.getString(getString(R.string.key_point_secd));
                    String pointName = jObject.getString(getString(R.string.key_point_name));
                    String mtrStatusCode = jObject.getString(getString(R.string.key_mtr_status_code));
                    String installDate = jObject.getString(getString(R.string.key_install_date));
                    String inqDate = jObject.getString(getString(R.string.key_inq_date));
                    String npuSn = jObject.getString(getString(R.string.key_npu_sn));
                    String admCode = jObject.getString(getString(R.string.key_adm_code));

                    PointResultRecord record = new PointResultRecord(
                            stdrspotSn,
                            basePointNo,
                            pointKindCode,
                            pointSecd,
                            pointName,
                            mtrStatusCode,
                            installDate,
                            inqDate,
                            npuSn,
                            admCode
                    );

                    items.add(record);
                }

                adapter.notifyDataSetChanged();
                String title = String.format("%s\n(총 %s건 중 %s건)", getResources().getString(R.string.title_activity_point_result), obj.getInt("totalCount"), items.size());
                textviewTitle.setText(title);

            } catch (JSONException e) {
                loadingDialog(false);
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
            e.printStackTrace();
        }
    };

    @UiThread
    @Click(R.id.layout_sort_by_install)
    public void sortInstall() {
        if(isInstDESC) {
            isInstDESC = false;
            Collections.sort(items, new InstAscCompare());
        } else {
            isInstDESC = true;
            Collections.sort(items, new InstDescCompare());
        }
        adapter.notifyDataSetChanged();
    }
//    @Click(R.id.ll_map_sort_invest)
//    public void sortInvest() {
//        if(isInvestDESC) {
//            //ib_invest_sort_invest.setBackgroundResource(R.drawable.img_list_arrow_up);
//            isInvestDESC = false;
//            Collections.sort(items, new InqSttusAscCompare());
//        } else {
//            //ib_invest_sort_invest.setBackgroundResource(R.drawable.img_list_arrow_down);
//            isInvestDESC = true;
//            Collections.sort(items, new InqSttusDescCompare());
//        }
//        adapter.notifyDataSetChanged();
//    }
    @Click({R.id.imagebtn_nav_back, R.id.textview_title})
    public void onBack() {
        super.onBackPressed();
    }

/*    @Click(R.id.ib_map_result_nav_home)
    public void onHome(){
        Intent movePage = new Intent(PointResultActivity.this, MainActivity.class);
        movePage.putExtra(getString(R.string.key_bonline), mBOnline);
        movePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(movePage);
        finish();
    }*/
    @UiThread
    public void loadingDialog(boolean view) {
        if(view)
            FineDialog.showProgress(PointResultActivity.this, false, getResources().getString(R.string.str_invest_loading));
        else
            FineDialog.hideProgress();
    }

    //오름차순(ASC)
    static class InstAscCompare implements Comparator<PointResultRecord> {
        @Override
        public int compare(PointResultRecord arg0, PointResultRecord arg1) {
            if(arg0 == null || arg1 == null){
                return -1;
            }

            return arg0.getInstallDate().compareTo(arg1.getInstallDate());
        }
    }
    //내림차순(DESC)
    static class InstDescCompare implements Comparator<PointResultRecord> {
        @Override
        public int compare(PointResultRecord arg0, PointResultRecord arg1) {
            return arg1.getInstallDate().compareTo(arg0.getInstallDate());
        }
    }
    //오름차순(ASC)
    static class InqSttusAscCompare implements Comparator<PointResultRecord> {
        @Override
        public int compare(PointResultRecord arg0, PointResultRecord arg1) {
            return arg0.getInstallDate().compareTo(arg1.getInstallDate());
        }
    }
    //내림차순(DESC)
    static class InqSttusDescCompare implements Comparator<PointResultRecord> {
        @Override
        public int compare(PointResultRecord arg0, PointResultRecord arg1) {
            return arg1.getInstallDate().compareTo(arg0.getInstallDate());
        }
    }
}
