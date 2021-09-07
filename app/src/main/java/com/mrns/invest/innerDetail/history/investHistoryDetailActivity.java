package com.mrns.invest.innerDetail.history;

import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.appcompat.app.AppCompatActivity;
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
import com.mrns.common.NetcheckMoniter;
import com.mrns.downdata.DownloadImageTask;
import com.mrns.invest.record.RnsSttusInqHist;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.StringUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(com.mrns.main.R.layout.activity_invest_history_detail)
public class investHistoryDetailActivity extends AppCompatActivity {
    @ViewById(R.id.textview_sub_title)
    TextView textviewSubTitle;
    @ViewById(R.id.textview_date)
    TextView textviewDate;       //조사년월일
    @ViewById(R.id.textview_position)
    TextView textviewPosition;    //소속
    @ViewById(R.id.textview_classification)
    TextView textviewClassification;     //직급
    @ViewById(R.id.textview_name)
    TextView textviewName;       //성명
    @ViewById(R.id.textview_mtr_sttus)
    TextView textviewMtrSttus; //표지상태
    @ViewById(R.id.textview_mtr_sttus_etc)
    TextView textviewMtrSttusEtc; //표지의상태 기타
    @ViewById(R.id.textview_rtk_able)
    TextView textviewRtkAble;  //위성측량가능여부
    @ViewById(R.id.textview_rtk_able_etc)
    TextView textviewRtkAbleEtc; //위성측량가능여부 기타
    @ViewById(R.id.textview_location_detail)
    TextView textviewLocationDetail;  //소재지상세정보
    @ViewById(R.id.textview_etc)
    TextView textviewEtc;   //특이사항
    @ViewsById({R.id.imageview_kimg, R.id.imageview_oimg, R.id.imageview_pimg, R.id.imageview_roughmap})
    List<ImageView> imageViewList;

    String mUrl;
    private NetcheckMoniter networkMoniter = null;
    long exSttusSn;

    @AfterViews
    protected void InitView() {
        //Network 상태 리시버 등록
        exSttusSn = getIntent().getLongExtra("SttusSn", 0);

        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mUrl = getString(com.mrns.main.R.string.server_ip) + getString(com.mrns.main.R.string.url_viewsttusinqhist);
        textviewSubTitle.setText(getIntent().getStringExtra(getString(com.mrns.main.R.string.key_title)));
        requestViewHistoryPOST(exSttusSn);
    }

    @UiThread
    protected void setData(RnsSttusInqHist item) {
        String inqDate;
        String strEmpty = getString(com.mrns.main.R.string.empty);

        if (item.getInqDe() != null && !item.getInqDe().equals("null") && !item.getInqDe().equals(strEmpty)) {
            inqDate = item.getInqDe().substring(0, 10);
        } else {
            inqDate = strEmpty;
        }

        textviewDate.setText(inqDate);
        textviewPosition.setText(StringUtil.nullCheck(item.getInqPsitn()));
        textviewClassification.setText(StringUtil.nullCheck(item.getInqClsf().equals(strEmpty) ? strEmpty : StringUtil.convertClassification(item.getInqClsf())));
        textviewName.setText(StringUtil.nullCheck(item.getInqNm()));
        textviewMtrSttus.setText(StringUtil.nullCheck(item.getMtrSttusCd()).equals(strEmpty) ? strEmpty : DBUtil.getCmmnCode("110", item.getMtrSttusCd()));
        textviewMtrSttusEtc.setText(StringUtil.nullCheck(item.getMtrSttusEtc()));
        textviewRtkAble.setText(StringUtil.nullCheck(item.getRtkPosblYn()).equals(strEmpty) ? strEmpty : DBUtil.getCmmnCode("111", item.getRtkPosblYn()));
        textviewRtkAbleEtc.setText(StringUtil.nullCheck(item.getRtkPosblEtc()));
        textviewEtc.setText(StringUtil.nullCheck(item.getPtclmatr()));

        String serverIp = getString(R.string.server_ip);
        int urlResoureceIds[] = {R.string.req_k_img, R.string.req_o_img, R.string.req_p_img, R.string.req_rog_map};
        String param = "?sttus_sn=" + exSttusSn;
        boolean isLast = false;

        for (int i = 0; i < urlResoureceIds.length; i++) {
            if (i == urlResoureceIds.length - 1) {
                isLast = true;
            }

            new DownloadImageTask(isLast, getBaseContext(), imageViewList.get(i)).execute(serverIp + getString(urlResoureceIds[i]) + param);
        }
    }

    @Click(com.mrns.main.R.id.layout_back_home)
    public void onBack() {
        super.onBackPressed();
    }

    protected void requestViewHistoryPOST(final long SttusSn) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, mUrl, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("sttus_sn", String.valueOf(SttusSn));
                return hash;
            }
        };
        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(com.mrns.main.R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    protected Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            setData(investHistroyJsonPaser(reponse));
        }
    };

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {

        }
    };

    protected RnsSttusInqHist investHistroyJsonPaser(String str) {

        RnsSttusInqHist hist = null;

        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");

            hist = new RnsSttusInqHist(
                    data.getLong("sttus_sn"),
                    data.getLong(getString(com.mrns.main.R.string.key_stdr_spot_sn_json)),
                    data.getString(getString(com.mrns.main.R.string.key_base_point_no_json)),
                    data.getString("points_knd_cd"),
                    data.getString("points_nm"),
                    data.getString("jgrc_cd"),
                    data.getString("brh_cd"),
                    data.getString("inq_de"),
                    data.getString("inq_instt_cd"),
                    data.getString("inq_psitn"),
                    data.getString("inq_clsf"),
                    data.getString("inq_nm"),
                    data.getString("inq_id"),
                    data.getString("inq_mtr_cd"),
                    data.getString("mtr_sttus_cd"),
                    data.getString("rtk_posbl_yn"),
                    data.getString("ptclmatr"),
                    data.getString("rog_map"),
                    data.getString("k_img"),
                    data.getString("o_img"),
                    data.getString("msurpoints_lc_drw"),
                    data.getString("sttus_rslt_acptnc_yn"),
                    data.getString("sttus_rslt_acptnc_de"),
                    data.getString("sttus_rslt_acptnc_jgrc"),
                    data.getString("sttus_rslt_acptnc_nm"),
                    data.getString("sttus_rslt_acptnc_rtrn_resn"),
                    data.getString("inq_mtr_etc"),
                    data.getString("mtr_sttus_etc"),
                    data.getString("rtk_posbl_etc"));


        } catch (JSONException e){
            Log.e(getString(com.mrns.main.R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(com.mrns.main.R.string.done), "doMakeDownMapList done");
        }
        return hist;
    }
}
