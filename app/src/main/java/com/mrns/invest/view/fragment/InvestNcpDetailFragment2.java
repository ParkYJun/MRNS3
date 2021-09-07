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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.downdata.DownloadImageTask;
import com.mrns.invest.record.RnsNpUnity;
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

@EFragment(R.layout.fragment_invest_detail_ncp_tab_2)
public class InvestNcpDetailFragment2 extends Fragment {
    @ViewById(R.id.scPointsKndCd)
    TextView scPointsKndCd;
    @ViewById(R.id.scPointsNm)
    TextView scPointsNm;
    @ViewById(R.id.scPointsSttusCd)
    TextView scPointsSttusCd;
    @ViewById(R.id.scMtrCd)
    TextView scMtrCd;
    @ViewById(R.id.scInqDe)
    TextView scInqDe;
    @ViewById(R.id.scLayDe)
    TextView scLayDe;
    @ViewById(R.id.scInstlInsttNm)
    TextView scInstlInsttNm;
    @ViewById(R.id.scRsltMgtNm)
    TextView scRsltMgtNm;
    @ViewById(R.id.scSttusInqNm)
    TextView scSttusInqNm;
    @ViewById(R.id.scMapNm)
    TextView scMapNm;
    @ViewById(R.id.scFullAddr)
    TextView scFullAddr;
    @ViewById(R.id.scFullRoadAddr)
    TextView scFullRoadAddr;
    @ViewById(R.id.scPointsPath)
    TextView scPointsPath;
    @ViewById(R.id.scTraverseTablePath)
    TextView scTraverseTablePath;
    @ViewById(R.id.scNotifyNo)
    TextView scNotifyNo;
    @ViewById(R.id.scNotifyDe)
    TextView scNotifyDe;
    @ViewById(R.id.scObserveNm)
    TextView scObserveNm;
    @ViewById(R.id.scObserveDe)
    TextView scObserveDe;

    @ViewsById({R.id.scWhere, R.id.scMap})
    List<ImageView> imageViewList;

    boolean mIsGeo;


    @AfterViews
    void initView() {
        Bundle bundle = getArguments();
        long npuSn = bundle.getLong(getString(R.string.key_npu_sn));
        mIsGeo = bundle.getBoolean(getString(R.string.key_is_geo));

        if(mIsGeo) {
            requestViewUnityPOST(npuSn);
            setImages(npuSn);
        } else {
            setData(RnsNpUnity.FindByNpuSn(npuSn));
        }
    }

    @UiThread
    void setImages(long npuSn) {
        String serverIp = getString(R.string.server_ip);
        int urlResoureceIds[] = {R.string.url_nc_detail_unity_rog_map, R.string.url_nc_detail_detail_map};
        String param = "?npu_sn=" + npuSn;
        boolean isLast = false;

        for (int i = 0; i < urlResoureceIds.length; i++) {
            if (i == urlResoureceIds.length - 1) {
                isLast = true;
            }

            new DownloadImageTask(isLast, getContext(), imageViewList.get(i)).execute(serverIp + getString(urlResoureceIds[i]) + param);
        }
    }

    void setImages(RnsNpUnity item) {
        String imageList[] = {item.getRogMapFile(), item.getDetailMapFile()};

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


    void setData(RnsNpUnity item) {
        if(item != null) {
            String strEmpty = getString(R.string.empty);

            scPointsKndCd.setText(!StringUtil.nullCheck(item.getPointsKndCd()).isEmpty() ? DBUtil.getCmmnCode("100", item.getPointsKndCd()) : strEmpty);
            scPointsNm.setText(StringUtil.nullCheck(item.getPointsNm()));
            scPointsSttusCd.setText(!StringUtil.nullCheck(item.getPointsSttusCd()).isEmpty() ? DBUtil.getCmmnCode("110", item.getPointsSttusCd()) : strEmpty);
            scMtrCd.setText(!StringUtil.nullCheck(item.getMtrCd()).isEmpty() ? DBUtil.getCmmnCode("109", item.getMtrCd()) : strEmpty);
            scInqDe.setText(!StringUtil.nullCheck(item.getInqDe()).isEmpty() ? item.getInqDe().substring(0, 10) : strEmpty);
            scLayDe.setText(!StringUtil.nullCheck(item.getLayDe()).isEmpty() ? item.getLayDe().substring(0, 10) : strEmpty);
            scInstlInsttNm.setText(StringUtil.nullCheck(item.getInstlInsttNm()));
            scRsltMgtNm.setText(StringUtil.nullCheck(item.getRsltMgtNm()));
            scSttusInqNm.setText(StringUtil.nullCheck(item.getSttusInqNm()));
            scMapNm.setText(StringUtil.nullCheck(item.getMapNm()));
            scFullAddr.setText(StringUtil.nullCheck(item.getFullAddr()));
            scFullRoadAddr.setText(StringUtil.nullCheck(item.getFullRoadAddr()));
            scPointsPath.setText(StringUtil.nullCheck(item.getPointsPath()));
            scTraverseTablePath.setText(StringUtil.nullCheck(item.getTraverseTablePath()));
            scNotifyNo.setText(StringUtil.nullCheck(item.getNotifyNo()));
            scNotifyDe.setText(!StringUtil.nullCheck(item.getNotifyDe()).isEmpty() ? item.getNotifyDe().substring(0, 10) : strEmpty);
            scObserveNm.setText(StringUtil.nullCheck(item.getObserveNm()));
            scObserveDe.setText(!StringUtil.nullCheck(item.getObserveDe()).isEmpty() ? item.getObserveDe().substring(0, 10) : strEmpty);


            /*scBizType.setText(!StringUtil.nullCheck(item.getBisSeCd()).isEmpty() ? DBUtil.getCmmnCode("105", item.getBisSeCd()) : "");
            scInstDate.setText(!StringUtil.nullCheck(item.getInstlDe()).isEmpty() ? item.getInstlDe().substring(0, 10) : "");
            scSoSok.setText(!StringUtil.nullCheck(item.getInstlPsitn()).isEmpty() ? DBUtil.getCD("st_kcscist_dept", "dept_nm", "dept_code", item.getInstlPsitn()) : "");*/

//            scWhere.setText(!StringUtil.nullCheck(item.getMsurpointsLcDrw()).isEmpty() ? item.getMsurpointsLcDrw() : "");
            if (!mIsGeo) {
                setImages(item);
            }
        }
           /* scBranch.setVisibility(View.GONE);
            scSmallOrg.setVisibility(View.GONE);
            scOrgSector.setVisibility(View.GONE);
            scJibun.setVisibility(View.GONE);
            scRoadName.setVisibility(View.GONE);
            scNearJibun.setVisibility(View.GONE);
            scNearRoadName.setVisibility(View.GONE);*/

            /*scBranch.setText(!StringUtil.nullCheck(item.getBrhCd()).isEmpty() ? DBUtil.getCD("st_kcscist_dept", "dept_nm", "dept_code", item.getBrhCd()) : "");
            scSmallOrg.setText(!StringUtil.nullCheck(item.getJgrcCd()).isEmpty() ? DBUtil.getCD("ST_KCSCIST_CMPTNC_JGRC", "KCSCIST_CODE", "JGRC_CODE", item.getJgrcCd()) : "");
            //scOrgSector.setText(item.getAdmCd()).isEmpty() ? DBUtil.getCD("ST_LAD_LOCPLC", "LAD_LOCPLC_NM", "LEGALDONG_CODE",item.getAdmCd()) : "");
            scOrgSector.setText(!StringUtil.nullCheck(item.getAdmCd()).isEmpty() ? DBUtil.getCD("TL_SCCO_SIG", "SIG_KOR_NM", "SIG_CD",item.getAdmCd()) : "");
            scJibun.setText(!StringUtil.nullCheck(item.getLocplcLnm()).isEmpty() ? item.getLocplcLnm() : "");
            scRoadName.setText(!StringUtil.nullCheck(item.getLocplcRdNm()).isEmpty() ? item.getLocplcRdNm() : "");
            scNearJibun.setText(!StringUtil.nullCheck(item.getNearbyLocplcLnm()).isEmpty() ? item.getNearbyLocplcLnm() : "");
            scNearRoadName.setText(!StringUtil.nullCheck(item.getNearbyLocplcRdNm()).isEmpty() ? item.getNearbyLocplcRdNm() : "");
            scGosiNum.setText(!StringUtil.nullCheck(item.getNotifyNo()).isEmpty() ? item.getNotifyNo() : "");
            scGosiDate.setText(!StringUtil.nullCheck(item.getNotifyDe()).isEmpty() ? item.getNotifyDe() : "");
            scKsp.setText(!StringUtil.nullCheck(item.getKspFile()).isEmpty() ? item.getKspFile() : "");
            scCrossTF.setText(!StringUtil.nullCheck(item.getCroPoint()).isEmpty() ? item.getCroPoint() : "");
            scScoreFile.setText(!StringUtil.nullCheck(item.getRsltDocFile()).isEmpty() ? item.getRsltDocFile() : "");*/
    }
    protected void requestViewUnityPOST(final long npu_sn) {
        String url = getString(R.string.server_ip) + getString(R.string.url_viewpointunity);
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

    protected Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse) {
            setData(JsonPaserUtil.investNcpUnityJsonPaser(reponse));
        }
    };

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {

        }
    };

}
