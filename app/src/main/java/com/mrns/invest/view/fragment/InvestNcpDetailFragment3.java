package com.mrns.invest.view.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.invest.record.RnsNpRslt;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.JsonPaserUtil;
import com.mrns.invest.util.StringUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

@EFragment(R.layout.fragment_invest_detail_ncp_tab_3)
public class InvestNcpDetailFragment3 extends Fragment {

    @ViewById(R.id.scTrgnptCd)
    TextView scTrgnptCd;
    @ViewById(R.id.scPointsX)
    TextView scPointsX;
    @ViewById(R.id.scPointsY)
    TextView scPointsY;
    @ViewById(R.id.scLat)
    TextView scLat;
    @ViewById(R.id.scLon)
    TextView scLon;
    @ViewById(R.id.scH)
    TextView scH;
    @ViewById(R.id.scGravity)
    TextView scGravity;
    @ViewById(R.id.scEh)
    TextView scEh;
    @ViewById(R.id.scMeridianSucha)
    TextView scMeridianSucha;
    @ViewById(R.id.scGeodeticSurvey)
    TextView scGeodeticSurvey;

    long npuSN;
    boolean isGeo;
    String url;


    @AfterViews
    void initView() {
        Bundle bundle = getArguments();
        npuSN = bundle.getLong(getString(R.string.key_npu_sn));
        isGeo = bundle.getBoolean(getString(R.string.key_is_geo));

        if(isGeo) {
            url = getString(R.string.server_ip) + getString(R.string.url_viewpointrslt);
            requestViewUnityPOST(npuSN);
        } else {
            setData(RnsNpRslt.FindByNpuSn(npuSN));
        }
    }
    void setData(RnsNpRslt item) {
        if(item != null) {
            String strEmpty = getString(R.string.empty);

            scTrgnptCd.setText(!StringUtil.nullCheck(item.getTrgnptCd()).isEmpty() ? DBUtil.getCmmnCode("102", item.getTrgnptCd()) : strEmpty);
            scPointsX.setText(item.getPointsX() != null ? String.valueOf(item.getPointsX()) : strEmpty);
            scPointsY.setText(item.getPointsY() != null ? String.valueOf(item.getPointsY()) : strEmpty);
            scLat.setText(StringUtil.nullCheck(item.getLat()));
            scLon.setText(StringUtil.nullCheck(item.getLon()));
            scH.setText(item.getH() != null ? String.valueOf(item.getH()) : strEmpty);
            scGravity.setText(item.getGravity() != null ? String.valueOf(item.getGravity()) : strEmpty);
            scEh.setText(StringUtil.nullCheck(item.getEh()));
            scMeridianSucha.setText(StringUtil.nullCheck(item.getMeridianSucha()));
            scGeodeticSurvey.setText(StringUtil.nullCheck(item.getGeodeticSurvey()));
        }
    }

    protected void requestViewUnityPOST(final long npu_sn) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getActivity());
        Request request = new StringRequest(Request.Method.POST, url, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_npu_sn_json), String.valueOf(npu_sn));
                return hash;
            }
        };
/*        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);*/
        queue.add(request);
    }

    protected Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            setData(JsonPaserUtil.investRnsNpRsltJsonPaser(reponse));
        }
    };

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {

        }
    };
}
