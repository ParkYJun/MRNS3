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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.common.NetcheckMoniter;
import com.mrns.downdata.DownloadImageTask;
import com.mrns.invest.record.RnsNpSttusInq;
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

@EActivity(R.layout.activity_invest_history_detail)
public class investHistoryDetailNcpActivity extends AppCompatActivity {
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
    List<ImageView> imageviewList;

    String mUrl;
    private NetcheckMoniter networkMoniter = null;
    long exSttusSn;

    @AfterViews
    protected void InitView() {
        //Network 상태 리시버 등록
        exSttusSn = getIntent().getLongExtra("npsiSn", 0);

        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mUrl = getString(R.string.server_ip) + getString(R.string.url_viewsttusinqncphist);
        textviewSubTitle.setText(getIntent().getStringExtra(getString(R.string.key_title)));
        requestViewHistoryPOST(exSttusSn);
    }

    @UiThread
    protected void setData(RnsNpSttusInq item) {
        String inqDate;
        String strEmpty = getString(R.string.empty);

        if (item.getInqDe() != null && !item.getInqDe().equals("null") && !item.getInqDe().equals(strEmpty)) {
            inqDate = item.getInqDe().substring(0, 10);
        } else {
            inqDate = strEmpty;
        }

        textviewDate.setText(inqDate);
        textviewPosition.setText(StringUtil.nullCheck(item.getInqPsitn()));
        textviewClassification.setText(StringUtil.nullCheck(item.getInqOfcps().equals(strEmpty) ? strEmpty : StringUtil.convertClassification(item.getInqOfcps())));
        textviewName.setText(StringUtil.nullCheck(item.getInqNm()));
        textviewMtrSttus.setText(StringUtil.nullCheck(item.getMtrSttusCd()).equals(strEmpty) ? strEmpty : DBUtil.getCmmnCode("110", item.getMtrSttusCd()));
        textviewMtrSttusEtc.setText(StringUtil.nullCheck(item.getMtrSttusEtc()));
        textviewRtkAble.setText(StringUtil.nullCheck(item.getRtkPosblYn()).equals(strEmpty) ? strEmpty : DBUtil.getCmmnCode("111", item.getRtkPosblYn()));
        textviewRtkAbleEtc.setText(StringUtil.nullCheck(item.getRtkPosblEtc()));
        textviewEtc.setText(StringUtil.nullCheck(item.getPtclmatr()));

        //이미지 불러오기
        String serverIp = getString(R.string.server_ip);
        int urlResourceIds[] = {R.string.req_k_img_ncp, R.string.req_o_img_ncp, R.string.req_p_img_ncp, R.string.req_rog_map_ncp};
        String param = "?sttus_sn=" + exSttusSn;
        boolean isLast = false;

        for (int i = 0; i < urlResourceIds.length; i++) {
            if (i == urlResourceIds.length - 1) {
                isLast = true;
            }

            new DownloadImageTask(isLast, getApplicationContext(), imageviewList.get(i)).execute(serverIp + getString(urlResourceIds[i]) + param);
        }
    }

    @Click(R.id.layout_back_home)
    public void onBack() {
        super.onBackPressed();
    }

    protected void requestViewHistoryPOST(final long SttusSn) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, mUrl, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("sttus_sn", String.valueOf(SttusSn));
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
            setData(investHistroyJsonPaser(reponse));
        }
    };

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {

        }
    };

    protected RnsNpSttusInq investHistroyJsonPaser(String str) {

        RnsNpSttusInq hist = null;

        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");

            hist = new RnsNpSttusInq(
                    data.getLong("npu_sn"),
                    data.getLong("npsi_sn"),
                    data.getString("inq_de"),
                    data.getString("inq_instt_cd"),
                    data.getString("inq_psitn"),
                    data.getString("inq_ofcps"),
                    data.getString("inq_nm"),
                    data.getString("inq_id"),
                    data.getString("inq_mtr_cd"),
                    data.getString("mtr_sttus_cd"),
                    data.getString("rtk_posbl_yn"),
                    data.getString("ptclmatr"),
                    data.getString("inq_mtr_etc"),
                    data.getString("mtr_sttus_etc"),
                    data.getString("rtk_posbl_etc"),
                    data.getString("acptnc_yn"),
                    data.getString("use_yn"),
                    data.getString("sttus_rslt_acptnc_sttus"),
                    data.getString("rog_map_file"),
                    data.getString("k_img_file"),
                    data.getString("o_img_file"),
                    data.getString("msurpoints_lc_drw_file"),
                    data.getString("rog_map"),
                    data.getString("k_img"),
                    data.getString("o_img"),
                    data.getString("msurpoints_lc_drw"),
                    data.getString("inq_sttus"),
                    data.getString("progression"),
                    data.getString("rl_inq_psitn"),
                    data.getString("rl_inq_clsf"),
                    data.getString("rl_inq_nm"),
                    data.getString("rl_inq_id"),
                    data.getString("points_path"));


        } catch (JSONException e){
            Log.e(getString(R.string.jSONException), e.getMessage());
        }finally {
            Log.i(getString(R.string.done), "doMakeDownMapList done");
        }
        return hist;
    }
}
