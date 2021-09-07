package com.mrns.invest.view.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.invest.record.RnsStdrspotUnity;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.JsonPaserUtil;
import com.mrns.invest.util.StringUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;
@EFragment(R.layout.fragment_invest_detail_tab_4)
public class InvestDetailFragment4 extends Fragment {

    //기존은 지역좌표로 변경
    @ViewById(R.id.scTrgnpt)
    TextView scTrgnpt;
    @ViewById(R.id.scLat)
    TextView scLat;
    @ViewById(R.id.scLon)
    TextView scLon;
    @ViewById(R.id.scPointx)
    TextView scPointx;
    @ViewById(R.id.scPointy)
    TextView scPointy;
    @ViewById(R.id.scH)
    TextView scH;

    //세계좌표 추가
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

    @ViewById(R.id.scWdeh)
    TextView scWdeh;
    @ViewById(R.id.scMerisu)
    TextView scMerisu;
    @ViewById(R.id.scWdhse)
    TextView scWdhse;

    RnsStdrspotUnity item;

    long stdrspotSn;
    boolean isGeo;

    String url;


    @AfterViews
    void initView() {
        Bundle bundle = getArguments();
        stdrspotSn = bundle.getLong(getString(R.string.key_stdr_spot_sn));
        isGeo = bundle.getBoolean(getString(R.string.key_is_geo));
        if(isGeo) {
            url = getString(R.string.server_ip) + getString(R.string.url_viewpoint);
            requestViewUnityPOST(stdrspotSn);
        } else {
            setData(RnsStdrspotUnity.FindByStdrspotSn(stdrspotSn));
        }
    }
    void setData(RnsStdrspotUnity item) {
        if(item != null) {
            String strEmpty = getString(R.string.empty);

            //기존
            scTrgnpt.setText(!StringUtil.nullCheck(item.getTrgnptCd()).isEmpty() ? DBUtil.getCmmnCode("102", item.getTrgnptCd()) : strEmpty);
            scLat.setText(StringUtil.nullCheck(item.getLat()));
            scLon.setText(StringUtil.nullCheck(item.getLon()));
            scH.setText(item.getH() != null  ? String.valueOf(item.getH()) : strEmpty);
            scPointx.setText(item.getPointsX() != null ? String.valueOf(item.getPointsX()) : strEmpty);
            scPointy.setText(item.getPointsY() != null ? String.valueOf(item.getPointsY()) : strEmpty);

            //추가
            wdTrgnpt.setText(!StringUtil.nullCheck(item.getWdTrgnptCd()).isEmpty() ? DBUtil.getCmmnCode("102", item.getWdTrgnptCd()) : strEmpty);
            wdLat.setText(StringUtil.nullCheck(item.getWdLat()));
            wdLon.setText(StringUtil.nullCheck(item.getWdLon()));
            wdH.setText(item.getWdH() != null  ? String.valueOf(item.getWdH()) : strEmpty);
            wdPointx.setText(item.getWdPointsX() != null ? String.valueOf(item.getWdPointsX()) : strEmpty);
            wdPointy.setText(item.getWdPointsY() != null ? String.valueOf(item.getWdPointsY()) : strEmpty);

            scWdeh.setText(StringUtil.nullCheck(item.getWdEh()));
            scMerisu.setText(StringUtil.nullCheck(item.getMeridianSucha()));
            scWdhse.setText(StringUtil.nullCheck(item.getWdHSeCd()));
        }
    }
    protected void requestViewUnityPOST(final long stdrspot_sn) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getActivity());
        Request request = new StringRequest(Request.Method.POST, url, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put(getString(R.string.key_stdr_spot_sn_json), String.valueOf(stdrspot_sn));
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
            setData(JsonPaserUtil.investUnityJsonPaser(reponse));
        }
    };

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {

        }
    };
}
