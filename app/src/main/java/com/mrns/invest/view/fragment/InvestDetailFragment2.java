package com.mrns.invest.view.fragment;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import com.mrns.invest.record.RnsStdrspotUnity;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.EmulatorPath;
import com.mrns.invest.util.JsonPaserUtil;
import com.mrns.invest.util.StringUtil;
import com.mrns.main.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.ViewsById;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@EFragment(R.layout.fragment_invest_detail_tab_2)
public class InvestDetailFragment2 extends Fragment {

    @ViewById(R.id.scNumber)
    TextView scNumber;
    @ViewById(R.id.scPointNumber)
    TextView scPointNumber;
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

    long stdrspotSn;
    boolean mIsGeo;

    String url;


    @AfterViews
    void initView() {
        Bundle bundle = getArguments();
        stdrspotSn = bundle.getLong(getString(R.string.key_stdr_spot_sn));
        mIsGeo = bundle.getBoolean(getString(R.string.key_is_geo));

        if(mIsGeo) {
            url = getString(R.string.server_ip) + getString(R.string.url_viewpoint);
            requestViewUnityPOST(stdrspotSn);
            setImage(stdrspotSn);
        } else {
            setData(RnsStdrspotUnity.FindByStdrspotSn(stdrspotSn));
        }
    }

    @UiThread
    void setImage(long stdrspotSn) {
        String serverIp = getString(R.string.server_ip);
        int urlResoureceIds[] = {R.string.url_detail_msurpoints_lc_drw, R.string.url_detail_msurpoints_lc_map};
        String param = "?stdrspot_sn=" + stdrspotSn;
        boolean isLast = false;

        for (int i = 0; i < urlResoureceIds.length; i++) {
            if (i == urlResoureceIds.length - 1) {
                isLast = true;
            }

            new DownloadImageTask(isLast, getContext(), imageViewList.get(i)).execute(serverIp + getString(urlResoureceIds[i]) + param);
        }
    }

    void setImage(RnsStdrspotUnity item) {
        String imageList[] = {item.getMsurpointsLcDrw(), item.getMsurpointsLcMap()};

        for (int i = 0; i < imageList.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(EmulatorPath.getPath() + "/MRNS/" + imageList[i]);
            if (null == bitmap || null == imageList[i] || imageList[i].equals(getString(R.string.str_null)) || null == imageList[i]) {
                AssetManager manager = getActivity().getAssets();
                try {
                    InputStream open = manager.open(getString(R.string.no_image_file));
                    bitmap = BitmapFactory.decodeStream(open);
                    imageViewList.get(i).setImageBitmap(bitmap);
                } catch (IOException e) {
                    return;
                }finally {
                    Log.i("done", "done");
                }
            } else {
                imageViewList.get(i).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                double height = bitmap.getHeight();
                double width = bitmap.getWidth();

                if (width < 1000) {
                    double adjValue = 1000 / width;

                    bitmap = Bitmap.createScaledBitmap(bitmap, (int)(width * adjValue), (int)(height * adjValue), true);
                }
                imageViewList.get(i).setImageBitmap(bitmap);
            }
        }
}

    void setData(RnsStdrspotUnity item) {
        if(item != null) {
            String strEmpty = getString(R.string.empty);

            scNumber.setText(StringUtil.nullCheck(item.getBasePointNo()));
            scPointNumber.setText(StringUtil.nullCheck(item.getPointsNo()));

            scPointType.setText(!StringUtil.nullCheck(item.getPointsKndCd()).isEmpty() ? DBUtil.getCmmnCode("100", item.getPointsKndCd()) : strEmpty);
            scDosunGrade.setText(!StringUtil.nullCheck(item.getMlineGradCd()).isEmpty() ? DBUtil.getCmmnCode("103", item.getMlineGradCd()) : strEmpty);
            scInstType.setText(!StringUtil.nullCheck(item.getInstlSeCd()).isEmpty() ? DBUtil.getCmmnCode("104",item.getInstlSeCd()) : strEmpty);
            scPageType.setText(!StringUtil.nullCheck(item.getMtrCd()).isEmpty() ? DBUtil.getCmmnCode("109",item.getMtrCd()) : strEmpty);
            scBizType.setText(!StringUtil.nullCheck(item.getBisSeCd()).isEmpty() ? DBUtil.getCmmnCode("105", item.getBisSeCd()) : strEmpty);
            scBizName.setText(StringUtil.nullCheck(item.getBisNm()));
            scInstDate.setText(!StringUtil.nullCheck(item.getInstlDe()).isEmpty() ? item.getInstlDe().substring(0, 10) : strEmpty);

            scInstOrg.setText(!StringUtil.nullCheck(item.getInstlInsttCd()).isEmpty() ? DBUtil.getCmmnCode("115", item.getInstlInsttCd()) : strEmpty);
            scSoSok.setText(StringUtil.nullCheck(item.getInstlPsitn()));
            scGrade.setText(!StringUtil.nullCheck(item.getInstlClsf()).isEmpty() ? DBUtil.getCmmnCode("150", item.getInstlClsf()) : strEmpty);
            scName.setText(StringUtil.nullCheck(item.getInstlNm()));
            scPrintNum.setText(StringUtil.nullCheck(item.getDrwNo()));
//            scWhere.setText(!StringUtil.nullCheck(item.getMsurpointsLcDrw()).isEmpty() ? item.getMsurpointsLcDrw() : "");

            scBranch.setText(!StringUtil.nullCheck(item.getBrhCd()).isEmpty() ? DBUtil.getCD("st_kcscist_dept", "dept_nm", "dept_code", item.getBrhCd()) : strEmpty);
            scSmallOrg.setText(!StringUtil.nullCheck(item.getJgrcCd()).isEmpty() ? DBUtil.getCD("ST_KCSCIST_CMPTNC_JGRC", "KCSCIST_CODE", "JGRC_CODE", item.getJgrcCd()) : strEmpty);
            //scOrgSector.setText(item.getAdmCd()).isEmpty() ? DBUtil.getCD("ST_LAD_LOCPLC", "LAD_LOCPLC_NM", "LEGALDONG_CODE",item.getAdmCd()) : "");
            scOrgSector.setText(!StringUtil.nullCheck(item.getAdmCd()).isEmpty() ? DBUtil.getCD("TL_SCCO_SIG", "SIG_KOR_NM", "SIG_CD",item.getAdmCd()) : strEmpty);
            scJibun.setText(StringUtil.nullCheck(item.getLocplcLnm()));
            scRoadName.setText(StringUtil.nullCheck(item.getLocplcRdNm()));
            scNearJibun.setText(StringUtil.nullCheck(item.getNearbyLocplcLnm()));
            scNearRoadName.setText(StringUtil.nullCheck(item.getNearbyLocplcRdNm()));
            scGosiNum.setText(StringUtil.nullCheck(item.getNotifyNo()));
            scGosiDate.setText(StringUtil.nullCheck(item.getNotifyDe()));
            scKsp.setText(StringUtil.nullCheck(item.getKspFile()));
            scCrossTF.setText(StringUtil.nullCheck(item.getCroPoint()));
            scScoreFile.setText(StringUtil.nullCheck(item.getRsltDocFile()));

            if (!mIsGeo) {
                setImage(item);
            }
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
