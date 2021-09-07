package com.mrns.invest.innerDetail.install;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.invest.record.RnsInstlHist;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.StringUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

@EFragment(R.layout.fragment_history_detail_fragment3)
public class HistoryDetailFragment3 extends Fragment{

    @ViewById(R.id.scTrgnpt)
    TextView scTrgnpt;
    @ViewById(R.id.scLat)
    TextView scLat;
    @ViewById(R.id.scLon)
    TextView scLon;
    @ViewById(R.id.scWdeh)
    TextView scWdeh;
    @ViewById(R.id.scH)
    TextView scH;
    @ViewById(R.id.scPointx)
    TextView scPointx;
    @ViewById(R.id.scPointy)
    TextView scPointy;
    @ViewById(R.id.scMerisu)
    TextView scMerisu;
    @ViewById(R.id.scWdhse)
    TextView scWdhse;

    @ViewById(R.id.wdTrgnpt)
    TextView wdTrgnpt;
    @ViewById(R.id.wdLat)
    TextView wdLat;
    @ViewById(R.id.wdLon)
    TextView wdLon;
    @ViewById(R.id.wdPointx)
    TextView wdPointx;
    @ViewById(R.id.wdPointy)
    TextView wdPointy;
    @ViewById(R.id.wdH)
    TextView wdH;

    RnsInstlHist item;

    @FragmentArg("instl_ih_sn")
    long instl_ih_sn;

    String url;

    @AfterViews
    void initView() {
        url = getString(R.string.server_ip) + getString(R.string.url_viewinstlhistt);
        //item = RnsInstlHist.FindByInstlIhSn(instl_ih_sn);
        requestViewInstallPOST(instl_ih_sn);
    }

    @UiThread
    protected void setData(RnsInstlHist item) {
        if(item != null) {
            scTrgnpt.setText(item.getTrgnptCd() != null ? DBUtil.getCmmnCode("102", item.getTrgnptCd()):"");
            scLat.setText(item.getLat() != null ? String.valueOf(item.getLat()):"");
            scLon.setText(item.getLon() != null ? String.valueOf(item.getLon()):"");
            scWdeh.setText(item.getWdEh() != null ? item.getWdEh():"");
            scH.setText(item.getH() != null ? String.valueOf(item.getH()):"");
            scPointx.setText(item.getPointsX() != null ? String.valueOf(item.getPointsX()):"");
            scPointy.setText(item.getPointsY() != null ? String.valueOf(item.getPointsY()):"");
            scMerisu.setText(item.getMeridianSucha() != null ? item.getMeridianSucha():"");
            scWdhse.setText(item.getWdHSeCd() != null ? DBUtil.getCmmnCode("108", item.getWdHSeCd()):"");

            wdTrgnpt.setText(!StringUtil.nullCheck(item.getWdTrgnptCd()).isEmpty() ? DBUtil.getCmmnCode("102", item.getWdTrgnptCd()):"");
            wdLat.setText(item.getWdLat() != null  ? String.valueOf(item.getWdLat()):"");
            wdLon.setText(item.getWdLon() != null  ? String.valueOf(item.getWdLon()):"");
            wdH.setText(item.getWdH() != null  ? String.valueOf(item.getWdH()):"");
            wdPointx.setText(item.getWdPointsX() != null ? String.valueOf(item.getWdPointsX()):"");
            wdPointy.setText(item.getWdPointsY() != null ? String.valueOf(item.getWdPointsY()):"");

        }
    }
    protected void requestViewInstallPOST(final long instl_ih_sn) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        Request request = new StringRequest(Request.Method.POST, url, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("instl_ih_sn", String.valueOf(instl_ih_sn));
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
            setData(investInstallJsonPaser(reponse));
        }
    };

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {

        }
    };
    protected RnsInstlHist investInstallJsonPaser(String str) {
        RnsInstlHist instal = null;
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");

            instal = new RnsInstlHist(
                    data.getLong("instl_ih_sn"),
                    data.getLong(getString(R.string.key_stdr_spot_sn_json)),
                    data.getString(getString(R.string.key_base_point_no_json)),
                    data.getString("points_knd_cd"),
                    data.getString("points_nm"),
                    data.getString("jgrc_cd"),
                    data.getString("brh_cd"),
                    data.getString("points_no"),
                    data.getString("mline_grad_cd"),
                    data.getString("mtr_cd"),
                    data.getString("instl_se_cd"),
                    data.getString("bis_se_cd"),
                    data.getString("bis_nm"),
                    data.getString("instl_de"),
                    data.getString("instl_instt_cd"),
                    data.getString("instl_psitn"),
                    data.getString("instl_clsf"),
                    data.getString("instl_nm"),
                    data.getString("drw_no"),
                    data.getString("msurpoints_lc_drw"),
                    data.getString("msurpoints_lc_map"),
                    data.getString("adm_cd"),
                    data.getString("locplc_lnm"),
                    data.getString("locplc_rd_nm"),
                    data.getString("nearby_locplc_lnm"),
                    data.getString("nearby_locplc_rd_nm"),
                    data.getString("notify_no"),
                    data.getString("notify_de"),
                    data.getString("rslt_doc_file"),
                    data.getString("surv_mth"),
                    data.getString("cal_mth"),
                    data.getString("cal_psitn"),
                    data.getString("cal_clsf"),
                    data.getString("cal_nm"),
                    data.getString("mline_nm"),
                    data.getString("ksp_file"),
                    data.getString("cro_point"),
                    data.getString("use_bhf_points"),
                    data.getString("begin_points"),
                    data.getString("begin_mach_points"),
                    data.getString("arrv_points"),
                    data.getString("arrv_mach_points"),
                    data.getString("before_points"),
                    data.getString("after_points"),
                    data.getString("dstnc"),
                    data.getString("azimuth"),
                    data.getString("dist_x"),
                    data.getString("dist_y"),
                    data.getString("trgnpt_cd"),
                    data.getString("points_x"),
                    data.getString("points_y"),
                    data.getString("lat"),
                    data.getString("lon"),
                    data.getString("h"),
                    data.getString("wd_h_se_cd"),
                    data.getString("wd_eh"),
                    data.getString("meridian_sucha"),
                    data.getString("instl_rslt_acptnc_yn"),
                    data.getString("instl_rslt_acptnc_de"),
                    data.getString("instl_rslt_acptnc_jgrc"),
                    data.getString("instl_rslt_acptnc_nm"),
                    data.getString("instl_rslt_acptnc_rtrn_resn"),
                    data.getString("wd_trgnpt_cd"),
                    data.getString("wd_points_x"),
                    data.getString("wd_points_y"),
                    data.getString("wd_lat"),
                    data.getString("wd_lon"),
                    data.getString("wd_h")
            );
        } catch (JSONException e){
            Log.e(getString(R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "investInstallJsonPaser done");
        }
        return instal;
    }


}
