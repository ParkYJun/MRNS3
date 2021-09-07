package com.mrns.invest.view.fragment;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.downdata.DownloadImageTask;
import com.mrns.invest.record.RnsNpSttusInq;
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EFragment(R.layout.fragment_invest_detail_tab_1)
public class InvestNcpDetailFragment1 extends Fragment {
    @ViewById(R.id.btn_back_home)
    ImageButton btnBackHome;
    @ViewById(R.id.textview_title)
    TextView textviewTitle;    //타이틀
    @ViewById(R.id.textview_date)
    TextView textviewDate;       //조사년월일
    @ViewById(R.id.textview_position)
    TextView textviewPosition;    //소속
    @ViewById(R.id.textview_classification)
    TextView textviewClassification;     //직급
    @ViewById(R.id.textview_name)
    TextView textviewName;       //성명
    @ViewById(R.id.textview_mtr_quality)
    TextView textviewMtrQuality; //표지재질
    @ViewById(R.id.textview_mtr_quality_etc)
    TextView textviewMtrQualityEtc; //표지재질 기타
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

    boolean mIsGeo;

    @AfterViews
    void initView() {
        long npuSn = getArguments().getLong(getString(R.string.key_npu_sn));
        mIsGeo = getArguments().getBoolean(getString(R.string.key_is_geo));

        if (mIsGeo) {
            requestViewSttusInqPOST(npuSn);
            setImages(npuSn);
        } else {
            setData(RnsNpSttusInq.FindByNpuSn(npuSn));
        }
    }

    @UiThread
    void setImages(long npuSn) {
        String serverIp = getString(R.string.server_ip);
        int urlResoureceIds[] = {R.string.url_nc_detail_k_img, R.string.url_nc_detail_o_img, R.string.url_nc_detail_msurpoints_lc_drw, R.string.url_nc_detail_rog_map};
        String param = "?npu_sn=" + npuSn;
        boolean isLast = false;

        for (int i = 0; i < urlResoureceIds.length; i++) {
            if (i == urlResoureceIds.length - 1) {
                isLast = true;
            }

            new DownloadImageTask(isLast, getContext(), imageViewList.get(i)).execute(serverIp + getString(urlResoureceIds[i]) + param);
        }
    }

    void setImages(RnsNpSttusInq item) {
        String imageList[] = {item.getKImg(), item.getOImg(), item.getMsurpointsLcDrw(), item.getRogMap()};

        for (int i = 0; i < imageViewList.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(EmulatorPath.getPath() + File.separator + getString(R.string.mrns) + File.separator + imageList[i]);

            if (null == bitmap || null == imageList[i] || imageList[i].isEmpty() || imageList.equals(getString(R.string.str_null))) {
                AssetManager manager = getActivity().getAssets();
                try {
                    InputStream open = manager.open(getString(R.string.no_image_file));
                    bitmap = BitmapFactory.decodeStream(open);
                    imageViewList.get(i).setImageBitmap(bitmap);
                } catch (IOException e) {
                    Log.e("", "이미지 없음");
                    return;
                } finally {
                    Log.i("done", "done");
                }
            } else {
                imageViewList.get(i).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                double height = bitmap.getHeight();
                double width = bitmap.getWidth();

                if (width < 1000) {
                    double adjValue = 1000 / width;

                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width * adjValue), (int) (height * adjValue), true);
                }
                imageViewList.get(i).setImageBitmap(bitmap);
            }
        }
    }

    void setData(RnsNpSttusInq item) {
        if (item != null) {
            String strEmpty = getString(R.string.empty);

            textviewDate.setText(!StringUtil.nullCheck(item.getInqDe()).isEmpty() ? item.getInqDe().substring(0, 10) : strEmpty);
            textviewPosition.setText(!StringUtil.nullCheck(item.getInqPsitn()).isEmpty() ? DBUtil.getCD("st_kcscist_dept", "dept_nm", "dept_code", item.getInqPsitn()) : strEmpty);
            textviewClassification.setText(StringUtil.convertClassification(StringUtil.nullCheck(item.getInqOfcps())));
            textviewName.setText(!StringUtil.nullCheck(item.getInqNm()).isEmpty() ? DBUtil.getCD("st_user", "user_nm", "empuno", item.getInqNm()) : strEmpty);
            textviewMtrQuality.setText(!StringUtil.nullCheck(item.getInqMtrCd()).isEmpty() ? DBUtil.getCmmnCode("109", item.getInqMtrCd()) : strEmpty);
            textviewMtrQualityEtc.setText(StringUtil.nullCheck(item.getInqMtrEtc()));
            textviewMtrSttus.setText(!StringUtil.nullCheck(item.getMtrSttusCd()).isEmpty() ? DBUtil.getCmmnCode("110", item.getMtrSttusCd()) : strEmpty);
            textviewMtrSttusEtc.setText(StringUtil.nullCheck(item.getMtrSttusEtc()));
            textviewRtkAble.setText(!StringUtil.nullCheck(item.getRtkPosblYn()).isEmpty() ? DBUtil.getCmmnCode("111", item.getRtkPosblYn()) : strEmpty);
            textviewRtkAbleEtc.setText(StringUtil.nullCheck(item.getRtkPosblEtc()));
            textviewEtc.setText(StringUtil.nullCheck(item.getPtclmatr()));

            if (!mIsGeo) {
                setImages(item);
            }

        } else {

            AssetManager manager = getActivity().getAssets();
            InputStream open = null;
            try {
                open = manager.open(getString(R.string.no_image_file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeStream(open);
            imageViewList.get(0).setImageBitmap(bitmap);
            imageViewList.get(1).setImageBitmap(bitmap);
            imageViewList.get(2).setImageBitmap(bitmap);
            imageViewList.get(3).setImageBitmap(bitmap);
        }
    }

    protected void requestViewSttusInqPOST(final long npu_sn) {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = getString(R.string.server_ip) + getString(R.string.url_viewpointsttus);
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
        setData(JsonPaserUtil.investRnsNpSttusInqJsonPaser(reponse));
        }
    };

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {

        }
    };

    /*@Click(R.id.layout_classification_and_name)
    public void onEditUserInfo() {
        View view = View.inflate(getContext(), R.layout.layout_edit_user_info, null);
        final Spinner spinnerClassfication = (Spinner) view.findViewById(R.id.spinner_classification);
        final EditText editTextName = (EditText)view.findViewById(R.id.editbox_name);

        spinnerClassfication.setFocusable(true);
        final ArrayList<String> arrayClassification = new ArrayList<>(Arrays.asList(DBUtil.getCmmnCodeStringArray("150")));
        spinnerClassfication.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, arrayClassification));
        String strClassification = textviewClassification.getText().toString();

        for (int i = 0; i < arrayClassification.size(); i++) {
            if (strClassification.equals(arrayClassification.get(i))) {
                spinnerClassfication.setSelection(i);
                break;
            }
        }

        editTextName.setText(textviewName.getText().toString());

        new AlertDialog.Builder(getContext())
                .setView(view)
                .setTitle("사용자 정보 변경")
                .setPositiveButton(getString(R.string.insert_save), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textviewClassification.setText(arrayClassification.get((int)spinnerClassfication.getSelectedItemId()));
                        textviewName.setText(editTextName.getText().toString());
                    }
                })
                .setNegativeButton(getString(R.string.str_invest_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }*/
}
