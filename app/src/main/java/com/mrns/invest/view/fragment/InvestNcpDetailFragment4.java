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
import com.mrns.invest.record.RnsNpSubPath;
import com.mrns.invest.record.RnsNpTraverRslt;
import com.mrns.invest.util.JsonPaserUtil;
import com.mrns.invest.util.StringUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

@EFragment(R.layout.fragment_invest_detail_ncp_tab_4)
public class InvestNcpDetailFragment4 extends Fragment {

    //기존은 지역좌표로 변경
    @ViewById(R.id.scGeodeticSurvey)
    TextView scGeodeticSurvey;
    @ViewById(R.id.scGpointsNm)
    TextView scGpointsNm;
    @ViewById(R.id.scLat)
    TextView scLat;
    @ViewById(R.id.scLon)
    TextView scLon;
    @ViewById(R.id.scEh)
    TextView scEh;
    @ViewById(R.id.scAzimuth)
    TextView scAzimuth;
    @ViewById(R.id.scDirectionAngle)
    TextView scDirectionAngle;
    @ViewById(R.id.scDstnc)
    TextView scDstnc;
    @ViewById(R.id.scGeodeticSurveySub)
    TextView scGeodeticSurveySub;
    @ViewById(R.id.scSubPointsNm)
    TextView scSubPointsNm;
    @ViewById(R.id.scSubPointsPath)
    TextView scSubPointsPath;
    @ViewById(R.id.scRelativeHeight)
    TextView scRelativeHeight;
    @ViewById(R.id.scLatSub)
    TextView scLatSub;
    @ViewById(R.id.scLonSub)
    TextView scLonSub;

    long npuSn;
    boolean isGeo;

    String url;
    String url2;

    @AfterViews
    void initView() {
        Bundle bundle = getArguments();
        npuSn = bundle.getLong(getString(R.string.key_npu_sn));
        isGeo = bundle.getBoolean(getString(R.string.key_is_geo));
        if(isGeo) {
            url = getString(R.string.server_ip) + getString(R.string.url_viewpointtraverrslt);
            url2 = getString(R.string.server_ip) + getString(R.string.url_viewpointsub);
            requestViewUnityPOST(npuSn);
            requestViewUnityPOST2(npuSn);
        } else {
            setData(RnsNpTraverRslt.FindByNpuSn(npuSn));
            setData2(RnsNpSubPath.FindByNpuSn(npuSn));
        }
    }
    void setData(RnsNpTraverRslt item) {
        if(item != null) {
            scGeodeticSurvey.setText(StringUtil.nullCheck(item.getGeodeticSurvey()));
            scGpointsNm.setText(StringUtil.nullCheck(item.getGPointsNm()));
            scLat.setText(StringUtil.nullCheck(item.getLat()));
            scLon.setText(StringUtil.nullCheck(item.getLon()));
            scEh.setText(StringUtil.nullCheck(item.getEh()));
            scAzimuth.setText(StringUtil.nullCheck(item.getAzimuth()));
            scDirectionAngle.setText(StringUtil.nullCheck(item.getDirectionAngle()));
            scDstnc.setText(StringUtil.nullCheck(item.getDstnc()));
        }
    }
    void setData2(RnsNpSubPath item) {
        if(item != null) {
            //기존
            scGeodeticSurveySub.setText(StringUtil.nullCheck(item.getGeodeticSurvey()));
            scSubPointsNm.setText(StringUtil.nullCheck(item.getSubPointsNm()));
            scSubPointsPath.setText(StringUtil.nullCheck(item.getSubPointsPath()));
            scRelativeHeight.setText(item.getRelativeHeight() != null  ? String.valueOf(item.getRelativeHeight()) : getString(R.string.empty));
            scLatSub.setText(StringUtil.nullCheck(item.getLat()));
            scLonSub.setText(StringUtil.nullCheck(item.getLon()));
        }
    }
    protected void requestViewUnityPOST(final long npu_sn) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getActivity());
        Request request = new StringRequest(Request.Method.POST, url, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("npu_sn", String.valueOf(npu_sn));
                return hash;
            }
        };
/*        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);*/
        queue.add(request);
    }
    protected void requestViewUnityPOST2(final long npu_sn) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getActivity());
        Request request = new StringRequest(Request.Method.POST, url2, successListener2, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("npu_sn", String.valueOf(npu_sn));
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
            setData(JsonPaserUtil.investRnsNpTraverRsltJsonPaser(reponse));
            setData2(JsonPaserUtil.investRnsNpSubPathJsonPaser(reponse));
        }
    };
    protected Response.Listener<String> successListener2 = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            //setData(JsonPaserUtil.investRnsNpTraverRsltJsonPaser(reponse));
            setData2(JsonPaserUtil.investRnsNpSubPathJsonPaser(reponse));
        }
    };
    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {

        }
    };
}
