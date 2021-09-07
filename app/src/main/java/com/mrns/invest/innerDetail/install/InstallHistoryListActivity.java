package com.mrns.invest.innerDetail.install;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.mrns.invest.adapter.listener.ClickListener;
import com.mrns.invest.record.RnsInstlHist;
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

@EActivity(R.layout.activity_install_history_list)
public class InstallHistoryListActivity extends AppCompatActivity {

    ArrayList<RnsInstlHist> items;
    boolean bOnline;
    long stdrspotSn;
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

    InvestInstallListAdapter adapter;

    boolean isSeDESC = false;
    boolean isDeDESC = false;
    String url;

    int pageNum;
    boolean mLoadMore = true;
    static int blockCount = 10;

    private NetcheckMoniter networkMoniter = null;

    LinearLayoutManager mLayoutManager;

    @AfterViews
    protected void InitView() {
        stdrspotSn = getIntent().getLongExtra(getString(R.string.key_stdr_spot_sn), 0);
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
        url = getString(R.string.server_ip) + getString(R.string.url_listinstlhist);

        adapter = new InvestInstallListAdapter(InstallHistoryListActivity.this);
        adapter.setItems(items);
        adapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = InstallHistoryDetailActivity_.intent(InstallHistoryListActivity.this)
                        .instl_ih_sn(items.get(position).getInstlIhSn())
                        .get();
                intent.putExtra(getString(R.string.key_bonline), bOnline);
                intent.putExtra(getString(R.string.key_title), title);
                startActivity(intent);
            }

            @Override
            public void onLocationClick(int position, View v) {
            }
        });
        mLayoutManager = new LinearLayoutManager(InstallHistoryListActivity.this);
        rv_invest_install.setLayoutManager(mLayoutManager);
        rv_invest_install.setHasFixedSize(true);
        rv_invest_install.setAdapter(adapter);
        rv_invest_install.setItemAnimator(new DefaultItemAnimator());

        rv_invest_install.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (mLoadMore) {
                    pageNum++;
                    requestListInstallPOST(stdrspotSn, pageNum, blockCount);
                }

            }
        });
        requestListInstallPOST(stdrspotSn, pageNum, blockCount);
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
    private static class DeAscCompare implements Comparator<RnsInstlHist> {
        @Override
        public int compare(RnsInstlHist arg0, RnsInstlHist arg1) {
            // TODO Auto-generated method stub
            return arg0.getInstlDe().compareTo(arg1.getInstlDe());
        }
    }

    //내림차순(DESC)
    private static class DeDescCompare implements Comparator<RnsInstlHist> {
        @Override
        public int compare(RnsInstlHist arg0, RnsInstlHist arg1) {
            // TODO Auto-generated method stub
            return arg1.getInstlDe().compareTo(arg0.getInstlDe());
        }
    }
    //오름차순(ASC)
    private static class SeAscCompare implements Comparator<RnsInstlHist> {
        @Override
        public int compare(RnsInstlHist arg0, RnsInstlHist arg1) {
            // TODO Auto-generated method stub
            return arg0.getInstlSeCd().compareTo(arg1.getInstlSeCd());
        }
    }

    //내림차순(DESC)
    private static class SeDescCompare implements Comparator<RnsInstlHist> {
        @Override
        public int compare(RnsInstlHist arg0, RnsInstlHist arg1) {
            // TODO Auto-generated method stub
            return arg1.getInstlSeCd().compareTo(arg0.getInstlSeCd());
        }
    }

    protected void requestListInstallPOST(final long stdrspot_sn, final int pageNum, final int blockCount) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, url, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_stdr_spot_sn_json), String.valueOf(stdrspot_sn));
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
                RnsInstlHist instal = new RnsInstlHist(
                        jObject.getLong("instl_ih_sn"),
                        jObject.getLong(getString(R.string.key_stdr_spot_sn_json)),
                        jObject.getString(getString(R.string.key_base_point_no_json)),
                        jObject.getString("points_knd_cd"),
                        jObject.getString("points_nm"),
                        jObject.getString("jgrc_cd"),
                        jObject.getString("brh_cd"),
                        jObject.getString("points_no"),
                        jObject.getString("mline_grad_cd"),
                        jObject.getString("mtr_cd"),
                        jObject.getString("instl_se_cd"),
                        jObject.getString("bis_se_cd"),
                        jObject.getString("bis_nm"),
                        jObject.getString("instl_de"),
                        jObject.getString("instl_instt_cd"),
                        jObject.getString("instl_psitn"),
                        jObject.getString("instl_clsf"),
                        jObject.getString("instl_nm"),
                        jObject.getString("drw_no"),
                        jObject.getString("msurpoints_lc_drw"),
                        jObject.getString("msurpoints_lc_map"),
                        jObject.getString("adm_cd"),
                        jObject.getString("locplc_lnm"),
                        jObject.getString("locplc_rd_nm"),
                        jObject.getString("nearby_locplc_lnm"),
                        jObject.getString("nearby_locplc_rd_nm"),
                        jObject.getString("notify_no"),
                        jObject.getString("notify_de"),
                        jObject.getString("rslt_doc_file"),
                        jObject.getString("surv_mth"),
                        jObject.getString("cal_mth"),
                        jObject.getString("cal_psitn"),
                        jObject.getString("cal_clsf"),
                        jObject.getString("cal_nm"),
                        jObject.getString("mline_nm"),
                        jObject.getString("ksp_file"),
                        jObject.getString("cro_point"),
                        jObject.getString("use_bhf_points"),
                        jObject.getString("begin_points"),
                        jObject.getString("begin_mach_points"),
                        jObject.getString("arrv_points"),
                        jObject.getString("arrv_mach_points"),
                        jObject.getString("before_points"),
                        jObject.getString("after_points"),
                        jObject.getString("dstnc"),
                        jObject.getString("azimuth"),
                        jObject.getString("dist_x"),
                        jObject.getString("dist_y"),
                        jObject.getString("trgnpt_cd"),
                        jObject.getString("points_x"),
                        jObject.getString("points_y"),
                        jObject.getString("lat"),
                        jObject.getString("lon"),
                        jObject.getString("h"),
                        jObject.getString("wd_h_se_cd"),
                        jObject.getString("wd_eh"),
                        jObject.getString("meridian_sucha"),
                        jObject.getString("instl_rslt_acptnc_yn"),
                        jObject.getString("instl_rslt_acptnc_de"),
                        jObject.getString("instl_rslt_acptnc_jgrc"),
                        jObject.getString("instl_rslt_acptnc_nm"),
                        jObject.getString("instl_rslt_acptnc_rtrn_resn"),
                        jObject.getString("wd_trgnpt_cd"),
                        jObject.getString("wd_points_x"),
                        jObject.getString("wd_points_y"),
                        jObject.getString("wd_lat"),
                        jObject.getString("wd_lon"),
                        jObject.getString("wd_h")
                );
                items.add(instal);
            }
            notifyData();
        } catch (JSONException e){
            Log.e(getString(R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "investInstallJsonPaser done");
        }
    }
}
