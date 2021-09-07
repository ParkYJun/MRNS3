package com.mrns.invest.innerDetail.install;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
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
import com.mrns.invest.record.RnsNpUnityBf;
import com.mrns.invest.util.EndlessRecyclerOnScrollListener;
import com.mrns.main.R;

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

@EActivity(R.layout.activity_install_history_ncp_list)
public class InstallHistoryListNcpActivity extends AppCompatActivity {

    ArrayList<RnsNpUnityBf> items;
    boolean bOnline;

    String title;

    @ViewById(R.id.titleTextViewSub)
    TextView titleTextViewSub;

    @ViewById(R.id.ll_invest_sort_se)
    LinearLayout ll_invest_sort_se;
    @ViewById(R.id.ib_invest_sort_se)
    ImageButton ib_invest_sort_se;

    @ViewById(R.id.ll_invest_sort_de)
    LinearLayout ll_invest_sort_de;
    @ViewById(R.id.ib_invest_sort_de)
    ImageButton ib_invest_sort_de;

    @ViewById(R.id.ib_invest_nav_back)
    ImageButton ib_invest_nav_back;

    @ViewById(R.id.rv_invest_install)
    RecyclerView rv_invest_install;

    InvestInstallListNcpAdapter adapter;

    boolean isSeDESC = false;
    boolean isDeDESC = false;
    String url;

    boolean mLoadMore = true;
    int pageNum;
    static int blockCount = 10;

    private NetcheckMoniter networkMoniter = null;

    LinearLayoutManager mLayoutManager;

    @AfterViews
    protected void InitView() {
        final long npuSn = getIntent().getLongExtra(getString(R.string.key_npu_sn), 0);
        bOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), false);
        title = getIntent().getStringExtra(getString(R.string.key_title));
        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        pageNum = 1;
        titleTextViewSub.setText(title);
        isSeDESC = false;
        isDeDESC = false;
        items = new ArrayList<>();
        url = getString(R.string.server_ip) + getString(R.string.url_listinstlncphist);

        adapter = new InvestInstallListNcpAdapter(InstallHistoryListNcpActivity.this);
        adapter.setItems(items);
        /*adapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = InstallHistoryDetailActivity_.intent(InstallHistoryListNcpActivity.this)
                        .instl_ih_sn(items.get(position).getInstlIhSn())
                        .get();
                intent.putExtra(getString(R.string.key_bonline), bOnline);
                intent.putExtra(getString(R.string.key_title), title);
                startActivity(intent);
            }

            @Override
            public void onLocationClick(int position, View v) {
            }
        });*/
        mLayoutManager = new LinearLayoutManager(InstallHistoryListNcpActivity.this);
        rv_invest_install.setLayoutManager(mLayoutManager);
        rv_invest_install.setHasFixedSize(true);
        rv_invest_install.setAdapter(adapter);
        rv_invest_install.setItemAnimator(new DefaultItemAnimator());

        rv_invest_install.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (mLoadMore) {
                    pageNum++;
                    requestListInstallPOST(npuSn, pageNum, blockCount);
                }
            }
        });
        requestListInstallPOST(npuSn, pageNum, blockCount);
    }



    @UiThread
    public void notifyData() {
        adapter.notifyDataSetChanged();
    }

    @Click(R.id.ib_invest_nav_back)
    public void onBack() {
        super.onBackPressed();
    }

    @UiThread
    @Click(R.id.ll_invest_sort_se)
    public void sortInstalSe() {
        if (isSeDESC) {
            //ib_invest_sort_se.setBackgroundResource(R.drawable.img_list_arrow_up);
            isSeDESC = false;
            Collections.sort(items, new SeDescCompare());
        } else {
            //ib_invest_sort_se.setBackgroundResource(R.drawable.img_list_arrow_down);
            isSeDESC = true;
            Collections.sort(items, new SeAscCompare());
        }
    }
    @UiThread
    @Click(R.id.ll_invest_sort_de)
    public void sortInstalDe() {
        if (isDeDESC) {
            //ib_invest_sort_de.setBackgroundResource(R.drawable.img_list_arrow_up);
            isDeDESC = false;
            Collections.sort(items, new DeDescCompare());
        } else {
            //ib_invest_sort_de.setBackgroundResource(R.drawable.img_list_arrow_down);
            isDeDESC = true;
            Collections.sort(items, new DeAscCompare());
        }
    }

    //오름차순(ASC)
    private static class DeAscCompare implements Comparator<RnsNpUnityBf> {
        @Override
        public int compare(RnsNpUnityBf arg0, RnsNpUnityBf arg1) {
            // TODO Auto-generated method stub
            return arg0.getLayDe().compareTo(arg1.getLayDe());
        }
    }

    //내림차순(DESC)
    private static class DeDescCompare implements Comparator<RnsNpUnityBf> {
        @Override
        public int compare(RnsNpUnityBf arg0, RnsNpUnityBf arg1) {
            // TODO Auto-generated method stub
            return arg1.getLayDe().compareTo(arg0.getLayDe());
        }
    }

    //오름차순(ASC)
    private static class SeAscCompare implements Comparator<RnsNpUnityBf> {
        @Override
        public int compare(RnsNpUnityBf arg0, RnsNpUnityBf arg1) {
            // TODO Auto-generated method stub
            return arg0.getInqDe().compareTo(arg1.getInqDe());
        }
    }

    //내림차순(DESC)
    private static class SeDescCompare implements Comparator<RnsNpUnityBf> {
        @Override
        public int compare(RnsNpUnityBf arg0, RnsNpUnityBf arg1) {
            // TODO Auto-generated method stub
            return arg1.getInqDe().compareTo(arg0.getInqDe());
        }
    }

    protected void requestListInstallPOST(final long npu_sn, final int pageNum, final int blockCount) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, url, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("npu_sn", String.valueOf(npu_sn));
                hash.put("pageNum", String.valueOf(pageNum));
                hash.put("blockCount", String.valueOf(blockCount));
                return hash;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    protected Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            investInstallJsonPaser(reponse);
        }
    };

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {

        }
    };

    protected void investInstallJsonPaser(String str) {
        JSONObject jObject;
        try {
            //JSONObject set {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONArray list = bDetail.getJSONArray("resultList");
            mLoadMore = !(bDetail.getInt("totPage") == bDetail.getInt("pageNum"));

            for (int i = 0; i < list.length(); i++) {
                jObject = list.getJSONObject(i);  // JSONObject 추출
                RnsNpUnityBf install = new RnsNpUnityBf();
                install.setNpuBfSn(jObject.getLong("npu_bf_sn"));
                install.setLayDe(jObject.getString("lay_de"));
                install.setInstlInsttNm(jObject.getString("instl_instt_nm"));
                items.add(install);
            }
            notifyData();
        } catch (JSONException e){
            Log.e(getString(R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "investInstallJsonPaser done");
        }
    }
}
