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

@EFragment(R.layout.fragment_history_detail_fragment2)
public class HistoryDetailFragment2 extends Fragment {

    @ViewById(R.id.scSurveyMethod)
    TextView scSurveyMethod;
    @ViewById(R.id.scCalcMethod)
    TextView scCalcMethod;
    @ViewById(R.id.scDosunName)
    TextView scDosunName;
    @ViewById(R.id.scUseDatum)
    TextView scUseDatum;
    @ViewById(R.id.scStartDatum)
    TextView scStartDatum;
    @ViewById(R.id.scDestPoint)
    TextView scDestPoint;
    @ViewById(R.id.scDestMechanical)
    TextView scDestMechanical;
    @ViewById(R.id.scAzimuth)
    TextView scAzimuth;
    @ViewById(R.id.scDistance)
    TextView scDistance;

    @ViewById(R.id.scBeforepoint)
    TextView scBeforepoint;
    @ViewById(R.id.scAfterPoint)
    TextView scAfterPoint;

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
            scSurveyMethod.setText(item.getSurvMth() != null ? DBUtil.getCmmnCode("106", item.getSurvMth()) : "");
            scCalcMethod.setText(item.getCalMth() != null ? DBUtil.getCmmnCode("107", item.getCalMth()):"");
            scDosunName.setText(item.getMlineNm() != null ? item.getMlineNm():"");
            scUseDatum.setText(item.getUseBhfPoints() != null ? item.getUseBhfPoints():"");
            scStartDatum.setText(item.getBeginPoints() != null ? item.getBeginPoints():"");
            scDestPoint.setText(item.getArrvPoints() != null ? item.getArrvPoints():"");
            scDestMechanical.setText(item.getArrvMachPoints() != null ? item.getArrvMachPoints():"");
            scBeforepoint.setText(item.getBeforePoints() != null ? item.getBeforePoints():"");
            scAfterPoint.setText(item.getAfterPoints() != null ? item.getAfterPoints():"");
            scAzimuth.setText(item.getAzimuth() != null ? item.getAzimuth():"");
            scDistance.setText(item.getDstnc() != null ? String.valueOf(item.getDstnc()):"");
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
