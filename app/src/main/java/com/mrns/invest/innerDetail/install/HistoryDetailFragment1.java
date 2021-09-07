package com.mrns.invest.innerDetail.install;

import android.graphics.Bitmap;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.downdata.DownloadImageTask;
import com.mrns.invest.record.RnsInstlHist;
import com.mrns.invest.util.DBUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_history_detail_fragment1)
public class HistoryDetailFragment1 extends Fragment {

    @ViewById(R.id.scManageNum)
    TextView scManageNum;
    @ViewById(R.id.scPointNum)
    TextView scPointNum;
    @ViewById(R.id.scPointType)
    TextView scPointType;
    @ViewById(R.id.scDosunGrade)
    TextView scDosunGrade;
    @ViewById(R.id.scInstType)
    TextView scInstType;
    @ViewById(R.id.scPageType)
    TextView scPageType;
    @ViewById(R.id.scBizType)
    TextView scBizType;
    @ViewById(R.id.scBizName)
    TextView scBizName;
    @ViewById(R.id.scInstDate)
    TextView scInstDate;
    @ViewById(R.id.scInstOrg)
    TextView scInstOrg;
    @ViewById(R.id.scSoSok)
    TextView scSoSok;
    @ViewById(R.id.scGrade)
    TextView scGrade;
    @ViewById(R.id.scName)
    TextView scName;
    @ViewById(R.id.scPrintNum)
    TextView scPrintNum;
    @ViewsById({R.id.scWhere, R.id.scMap})
    List<ImageView> imageViewList;
    @ViewById(R.id.scBranch)
    TextView scBranch;
    @ViewById(R.id.scSmallOrg)
    TextView scSmallOrg;
    @ViewById(R.id.scOrgSector)
    TextView scOrgSector;
    @ViewById(R.id.scJibun)
    TextView scJibun;
    @ViewById(R.id.scRoadName)
    TextView scRoadName;
    @ViewById(R.id.scNearJibun)
    TextView scNearJibun;
    @ViewById(R.id.scNearRoadName)
    TextView scNearRoadName;
    @ViewById(R.id.scGosiNum)
    TextView scGosiNum;
    @ViewById(R.id.scGosiDate)
    TextView scGosiDate;
    @ViewById(R.id.scKsp)
    TextView scKsp;
    @ViewById(R.id.scCrossTF)
    TextView scCrossTF;
    @ViewById(R.id.scScoreFile)
    TextView scScoreFile;

    RnsInstlHist item;

    Bitmap scMapBitmap;

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
        if (item != null) {
            scManageNum.setText(item.getBasePointNo() != null ? item.getBasePointNo() : "");
            scPointNum.setText(item.getPointsNo() != null ? item.getPointsNo() : "");

            scPointType.setText(item.getPointsKndCd() != null ? DBUtil.getCmmnCode("100", item.getPointsKndCd()) : "");
            scDosunGrade.setText(item.getMlineGradCd() != null ? DBUtil.getCmmnCode("103", item.getMlineGradCd()) : "");
            scInstType.setText(item.getInstlSeCd() != null ? DBUtil.getCmmnCode("104", item.getInstlSeCd()) : "");
            scPageType.setText(item.getMtrCd() != null ? DBUtil.getCmmnCode("109", item.getMtrCd()) : "");
            scBizType.setText(item.getBisSeCd() != null ? DBUtil.getCmmnCode("105", item.getBisSeCd()) : "");
            scBizName.setText(item.getBisNm() != null ? item.getBisNm() : "");
            scInstDate.setText(item.getInstlDe() != null ? item.getInstlDe() : "");

            scInstOrg.setText(item.getInstlInsttCd() != null ? DBUtil.getCmmnCode("115", item.getInstlInsttCd()) : "");

            //scSoSok.setText(item.getInstlPsitn() != null ? DBUtil.getCD("st_kcscist_dept", "dept_nm", "dept_code", item.getInstlPsitn()) : "");
            scSoSok.setText(item.getInstlPsitn() != null ? item.getInstlPsitn() : "");

            scGrade.setText(item.getInstlClsf() != null ? item.getInstlClsf() : "");
            scName.setText(item.getInstlNm() != null ? item.getInstlNm() : "");
            scPrintNum.setText(item.getDrwNo() != null ? item.getDrwNo() : "");


//            scWhere.setText(item.getMsurpointsLcDrw() != null ? item.getMsurpointsLcDrw() : "");


            //이미지들
            String serverIp = getString(R.string.server_ip);
            int urlResourceIds[] = {R.string.req_drw_img, R.string.req_map_img};
            String param = "?instl_ih_sn=" + item.getInstlIhSn();
            boolean isLast = false;

            for (int i = 0; i < urlResourceIds.length; i++) {
                if (i == urlResourceIds.length - 1) {
                    isLast = true;
                }

                new DownloadImageTask(isLast, getContext(), imageViewList.get(i)).execute(serverIp + getString(urlResourceIds[i]) + param);
            }

            scBranch.setText(item.getBrhCd() != null ? DBUtil.getCD("st_kcscist_dept", "dept_nm", "dept_code", item.getBrhCd()) : "");
            scSmallOrg.setText(item.getJgrcCd() != null ? DBUtil.getCD("ST_KCSCIST_CMPTNC_JGRC", "KCSCIST_CODE", "JGRC_CODE", item.getJgrcCd()) : "");

            //scOrgSector.setText(item.getAdmCd() != null ? DBUtil.getCD("ST_LAD_LOCPLC", "LAD_LOCPLC_NM", "LEGALDONG_CODE",item.getAdmCd()) : "");
            scOrgSector.setText(item.getAdmCd() != null ? DBUtil.getCD("TL_SCCO_SIG", "SIG_KOR_NM", "SIG_CD", item.getAdmCd()) : "");
            scJibun.setText(item.getLocplcLnm() != null ? item.getLocplcLnm() : "");
            scRoadName.setText(item.getLocplcRdNm() != null ? item.getLocplcRdNm() : "");
            scNearJibun.setText(item.getNearbyLocplcLnm() != null ? item.getNearbyLocplcLnm() : "");
            scNearRoadName.setText(item.getNearbyLocplcRdNm() != null ? item.getNearbyLocplcRdNm() : "");
            scGosiNum.setText(item.getNotifyNo() != null ? item.getNotifyNo() : "");
            scGosiDate.setText(item.getNotifyDe() != null ? item.getNotifyDe() : "");
            scKsp.setText(item.getKspFile() != null ? item.getKspFile() : "");
            scCrossTF.setText(item.getCroPoint() != null ? item.getCroPoint() : "");
            scScoreFile.setText(item.getRsltDocFile() != null ? item.getRsltDocFile() : "");
        }
    }

    protected void requestViewInstallPOST(final long instl_ih_sn) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        Request request = new StringRequest(Request.Method.POST, url, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<String, String>();
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
        } catch (JSONException e) {
            Log.e(getString(R.string.jSONException), e.getMessage());
        } catch (Exception e) {
            Log.e(getString(R.string.exception), e.getMessage());
        } finally {
            Log.i(getString(R.string.done), "investInstallJsonPaser done");
        }
        return instal;

    }

}
