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
@EFragment(R.layout.fragment_invest_detail_tab_3)
public class InvestDetailFragment3 extends Fragment {

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

            scSurveyMethod.setText(!StringUtil.nullCheck(item.getSurvMth()).isEmpty() ? DBUtil.getCmmnCode("106", item.getSurvMth()) : strEmpty);
            scCalcMethod.setText(!StringUtil.nullCheck(item.getCalMth()).isEmpty() ? DBUtil.getCmmnCode("107", item.getCalMth()): strEmpty);
            scDosunName.setText(StringUtil.nullCheck(item.getMlineNm()));
            scUseDatum.setText(StringUtil.nullCheck(item.getUseBhfPoints()));
            scStartDatum.setText(StringUtil.nullCheck(item.getBeginPoints()));
            scDestPoint.setText(StringUtil.nullCheck(item.getArrvPoints()));
            scDestMechanical.setText(StringUtil.nullCheck(item.getArrvMachPoints()));
            scBeforepoint.setText(StringUtil.nullCheck(item.getBeforePoints()));
            scAfterPoint.setText(StringUtil.nullCheck(item.getAfterPoints()));
            scAzimuth.setText(StringUtil.nullCheck(item.getAzimuth()));
            scDistance.setText(item.getDstnc() != null ? String.valueOf(item.getDstnc()) : strEmpty);
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
