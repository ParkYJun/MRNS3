package com.mrns.updata;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mrns.common.NetcheckMoniter;
import com.mrns.invest.record.RnsNpUnity;
import com.mrns.invest.util.DBUtil;
import com.mrns.main.R;
import com.mrns.map.MapWebActivity;
import com.mrns.sqllite.DBHelper;
import com.mrns.sqllite.DatabaseManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import uk.co.senab.photoview.PhotoViewAttacher;

@EActivity(R.layout.activity_upload_work_detail)
public class UploadWorkDetailNcpActivity extends AppCompatActivity {

    SQLiteDatabase db;
    DBHelper helper;

    String USER_TABLE_NAME = "RNS_NP_STTUS_INQ_BF";

    int exWorkSeq;
    boolean exBOnline;

    //UI set
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
    @ViewById(R.id.textview_location_detail_title)
    TextView textviewLocationDetailTitle;  //소재지상세정보 메뉴명
    @ViewById(R.id.textview_location_detail)
    TextView textviewLocationDetail;  //소재지상세정보
    @ViewById(R.id.textview_etc)
    TextView textviewEtc;   //특이사항
    @ViewById(R.id.imageview_kimg)
    ImageView imageviewKImg;        //근경
    @ViewById(R.id.imageview_oimg)
    ImageView imageviewOImg;      //원경
    @ViewById(R.id.imageview_pimg)
    ImageView imageviewPImg;      //측정점위치설명도
    @ViewById(R.id.imageview_roughmap)
    ImageView imageviewRoughmap;	    //약도
    @ViewById(R.id.btn_inquire_status)
    Button btnInquireStatus;    //조사상태

    boolean ckFlag = false;

    HashMap<String, String> params;
    private Handler mHandler;
    private Runnable mRunnable;

    private static AsyncHttpClient client;
    private ProgressDialog mProgressDialog;
    RequestParams requestParams;
    final int DEFAULT_TIMEOUT = 20 * 1000;
    private RnsNpUnity item;

    private NetcheckMoniter networkMoniter = null;

    /**
     * 저장된 약도 이미지 확대/축소를 위한 라이브러리
     */
    PhotoViewAttacher mRogmapAttacher;

    @AfterViews
    protected void InitView() {
        textviewLocationDetailTitle.setText(R.string.insert_point_route);
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        exBOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), exBOnline);
        exWorkSeq = getIntent().getIntExtra("workSeq", exWorkSeq);

        item = RnsNpUnity.FindByNpuSn(exWorkSeq);

        makeDetailView();
    }

    @Click(R.id.btn_back_home)
    public void onBackHome() {
        btnBackHome.setImageResource(R.drawable.nav_btn_back_p);
        finish();
    }

    @Click(R.id.btn_inquire_status)
    public void onInquireStatus() {
        changeStatus();
    }

    @Click(R.id.btn_view_location)
    public void onViewLocation() {
        String npuSn = params.get(getString(R.string.key_npu_sn_json));
        Intent movePage = new Intent(UploadWorkDetailNcpActivity.this, MapWebActivity.class);
        movePage.putExtra(getString(R.string.key_mode), 1);
        movePage.putExtra(getString(R.string.key_type), params.get("point_secd").equals("0") ? getString(R.string.key_national_point_type_for_map_query) : getString(R.string.key_common_point_type_for_map_query));
        movePage.putExtra(getString(R.string.key_fid), npuSn);
        movePage.putExtra(getString(R.string.key_npu_sn), Long.valueOf(npuSn));
        startActivity(movePage);
    }

    @Click(R.id.btn_transmission)
    public void onTransmission() {
        InvestDetailUpload();
    }

    void InvestDetailUpload() {
        mProgressDialog = new ProgressDialog(UploadWorkDetailNcpActivity.this);
        mProgressDialog.setMessage(getString(R.string.information_uploading));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        try{
            requestParams = new RequestParams();

            String keyArray[] = getResources().getStringArray(R.array.array_upload_work_detail_ncp);

            for (String key : keyArray) {
                requestParams.put(key, params.get(key));
            }

            String strPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getString(R.string.mrns) + File.separator;
            String fileName;

            fileName = (String)params.get("k_img");

            if (!fileName.equals("")) {
                File k_img_file = new File(strPath + fileName);
                requestParams.put("kImgFile", k_img_file);
            }
            fileName = (String)params.get("o_img");

            if (!fileName.equals("")) {
                File o_img_file = new File(strPath + fileName);
                requestParams.put("oImgFile", o_img_file);
            }

            fileName = (String)params.get("msurpoints_lc_drw");

            if (!fileName.equals("")) {
                File msurpoints_lc_drw_file = new File(strPath + fileName);
                requestParams.put("msurpointsLcDrwFile", msurpoints_lc_drw_file);
            }

            fileName = (String)params.get("rog_map");

            if (!fileName.equals("")) {
                File rog_map_file = new File(strPath + fileName);
                requestParams.put("rogMapFile", rog_map_file);
            }



        } catch (FileNotFoundException e) {
            Log.e(getString(R.string.fileNotFoundException), e.getMessage());
            mProgressDialog.dismiss();
        } finally {
            FileUploadDetail();
            Log.i(getString(R.string.done), "InvestDetailUpload done");
        }


    }

    void FileUploadDetail() {
        client  = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.setURLEncodingEnabled(false);

        String url  = getString(R.string.server_ip) + getString(R.string.req_uploadInvestNcp);
        try {
            client.post(url, requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (statusCode == 200) {

                        try {
                            JSONObject obt = response.getJSONObject("xmlReturnVO");
                            int seq = obt.getInt("result");

                            updateInvestStatus(3);

                            Handler mHandler = new Handler();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(UploadWorkDetailNcpActivity.this, getString(R.string.uploadComplete), Toast.LENGTH_SHORT).show();
                                    Intent movePage = new Intent(UploadWorkDetailNcpActivity.this, UploadActivity.class);
                                    movePage.putExtra(getString(R.string.key_bonline), true);
                                    startActivity(movePage);
                                }
                            });

                        } catch (JSONException e) {
                            Log.e(getString(R.string.jSONException), e.getMessage());
                            mProgressDialog.dismiss();
                        }
                    } else {
                        Toast.makeText(UploadWorkDetailNcpActivity.this, getString(R.string.server_error), Toast.LENGTH_LONG).show();
                    }
                }//end onSuccess

                @Override
                public void  onFailure(int statusCode, Header[]headers, Throwable e, JSONObject obj){
                    mProgressDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(UploadWorkDetailNcpActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage(getString(R.string.server_error) + "\n다시 전송버튼을 눌러 주세요.");
                    alert.show();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String tt, Throwable e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(UploadWorkDetailNcpActivity.this, getString(R.string.server_error), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried
                }


            });
        }catch (Exception e) {
            mProgressDialog.dismiss();
            Log.d("Exception: ", e.getMessage());
        }

    }

    public void changeStatus(){
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.question_change_status))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String textBtn;
                        int colorBtn;
                        int status;

                        if (ckFlag) {
                            textBtn = getString(R.string.inquire_status_complete);
                            colorBtn = getResources().getColor(R.color.yellow);
                            ckFlag = false;
                            status = 2;
                        } else {
                            textBtn = getString(R.string.inquire_status_ongoing);
                            colorBtn = getResources().getColor(R.color.textWhite);
                            ckFlag = true;
                            status = 1;
                        }

                        btnInquireStatus.setText(textBtn);
                        btnInquireStatus.setTextColor(colorBtn);
                        btnInquireStatus.setTextSize(16);
                        updateInvestStatus(status);

                        Toast.makeText(UploadWorkDetailNcpActivity.this, getString(R.string.goToChageStatus), Toast.LENGTH_SHORT).show();

                        mRunnable = new Runnable() {
                            @Override
                            public void run() {
                                Intent movePage = new Intent(UploadWorkDetailNcpActivity.this, UploadActivity.class);
                                movePage.putExtra(getString(R.string.key_bonline), true);
                                startActivity(movePage);
                            }
                        };

                        mHandler = new Handler();
                        mHandler.postDelayed(mRunnable, 1500);

                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    public int updateInvestStatus(int status){
        int returnValue = 0;

        try{
            helper = new DBHelper(UploadWorkDetailNcpActivity.this, getString(R.string.db_full_name), null, 1);
            DatabaseManager.initializeInstance(helper);
            db = DatabaseManager.getInstance().openDatabase();

            ContentValues values = new ContentValues();
            values.put("inq_sttus", status);

            returnValue = db.update(USER_TABLE_NAME, values, "npu_sn="+ exWorkSeq, null);

        }catch (NullPointerException e) {
            Log.e(getString(R.string.nullPointerException), e.getMessage());
        }finally {
            DatabaseManager.getInstance().closeDatabase();
            Log.i(getString(R.string.done), "updateInvestStatus done");
        }

        return returnValue;
    }

    void makeDetailView() {
        try{
            helper = new DBHelper(UploadWorkDetailNcpActivity.this, getString(R.string.db_full_name), null, 1);
            DatabaseManager.initializeInstance(helper);
            db = DatabaseManager.getInstance().openDatabase();

            String sql = "SELECT " +
                        "BF.npsi_bf_sn, " +        //0
                        "BF.npu_sn, " +            //1
                        "BF.inq_de, " +            //2
                        "BF.inq_instt_cd, " +      //3
                        "BF.inq_psitn, " +         //4
                        "(select dept_nm from ST_KCSCIST_DEPT where DEPT_CODE = BF.inq_psitn and use_yn = 'Y') as inq_psitn_nm, " +        //5
                        "BF.inq_ofcps, " +         //6
                        "(SELECT CODE_VALUE FROM ST_CMMN_CODE_DTLS WHERE CODE = BF.inq_ofcps AND CODE_CLCD=150) AS inq_ofcps_nm, " +       //7
                        "BF.inq_nm, " +            //8
                        "BF.inq_id, " +            //9
                        "BF.inq_mtr_cd, " +        //10
                        "(SELECT CODE_VALUE FROM ST_CMMN_CODE_DTLS WHERE CODE = BF.inq_mtr_cd AND CODE_CLCD=109) inq_mtr_nm, " +       //11
                        "BF.mtr_sttus_cd, " +      //12
                        "(SELECT CODE_VALUE FROM ST_CMMN_CODE_DTLS WHERE CODE = BF.mtr_sttus_cd AND CODE_CLCD=110) mtr_sttus_nm, " +       //13
                        "BF.rtk_posbl_yn, " +      //14
                        "(SELECT CODE_VALUE FROM ST_CMMN_CODE_DTLS WHERE CODE = BF.rtk_posbl_yn AND CODE_CLCD=111) rtk_posbl_yn_nm, " +        //15
                        "BF.ptclmatr, " +          //16
                        "BF.inq_mtr_etc, " +       //17
                        "BF.mtr_sttus_etc, " +     //18
                        "BF.rtk_posbl_etc, " +     //19
                        "BF.k_img, " +             //20
                        "BF.o_img, " +             //21
                        "BF.msurpoints_lc_drw, " + //22
                        "BF.rog_map, " +           //23
                        "BF.points_path, " +           //24
                        "BF.sttus_rslt_acptnc_sttus, " +       //25
                        "BF.rl_inq_psitn, " +          //26
                        "(SELECT dept_nm from ST_KCSCIST_DEPT where DEPT_CODE = BF.rl_inq_psitn and use_yn = 'Y') as rl_inq_psitn_nm, " +      //27
                        "BF.rl_inq_clsf, " +       //28
                        "(SELECT CODE_VALUE FROM ST_CMMN_CODE_DTLS WHERE CODE = BF.rl_inq_clsf AND CODE_CLCD=150) AS rl_inq_clsf_nm, " +       //29
                        "BF.rl_inq_nm, " +     //30
                        "BF.rl_inq_id,  " +  //31
                        "UNITY.points_secd " +    //32
                    "FROM RNS_NP_STTUS_INQ_BF BF, RNS_NP_UNITY UNITY " +
                    "WHERE " +
                    "BF.NPU_SN = UNITY.NPU_SN " +
                    " AND BF.npu_sn = " + exWorkSeq;

            Cursor result = db.rawQuery(sql, null);

            params = new HashMap<>();

            while (result.moveToNext()){
                int npsiBfSn = result.getInt(0);
                int npuSn = result.getInt(1);
                String inqDe = result.getString(2);
                if(inqDe != null) inqDe = inqDe.substring(0,10);
                String inqInsttCd = result.getString(3);
                String inqPsitn = result.getString(4);
                String inqPsitnNm = result.getString(5);
                String inqOfcps = result.getString(6);
                String inqOfcpsNm = result.getString(7);
                String inqNm = result.getString(8);
                String inqId = result.getString(9);
                String inqMtrCd = result.getString(10);
                String inqMtrNm = result.getString(11);
                String mtrSttusCd = result.getString(12);
                String mtrSttusNm = result.getString(13);
                String rtkPosblYn = result.getString(14);
                String rtkPosblYnNm = result.getString(15);
                String ptclmatr = result.getString(16);
                String inqMtrEtc = result.getString(17);
                String mtrSttusEtc = result.getString(18);
                String rtkPosblEtc = result.getString(19);
                String kImg = result.getString(20);
                String oImg = result.getString(21);
                String msurpointsLcDrw = result.getString(22);
                String rogMap = result.getString(23);
                String pointsPath = result.getString(24);
                String sttusRsltAcptncSttus = result.getString(25);
                String rlInqPsitn = result.getString(26);
                String rlInqPsitnNm = result.getString(27);
                String rlInqClsf = result.getString(28);
                String rlInqClsfNm = result.getString(29);
                String rlInqNm = result.getString(30);
                String rlInqId = result.getString(31);
                String pointSecd = result.getString(32);

                String pointKind = item.getPointsKndCd() != null ? DBUtil.getCmmnCode(getString(R.string.code_point_kind), item.getPointsKndCd()) : getString(R.string.empty);
                String pointNm = item.getPointsNm() != null ? item.getPointsNm() : getString(R.string.empty);
                textviewTitle.setText(getString(R.string.title_points, pointNm, pointKind));

                textviewDate.setText(inqDe);
                textviewPosition.setText(rlInqPsitn); //실제 조사자
                textviewClassification.setText(rlInqClsfNm);    //실제조사자
                textviewName.setText(rlInqNm);            //실제조사자
                textviewMtrQuality.setText(inqMtrNm);
                textviewMtrQualityEtc.setText(inqMtrEtc);
                textviewMtrSttus.setText(mtrSttusNm);
                textviewMtrSttusEtc.setText(mtrSttusEtc);
                textviewRtkAble.setText(rtkPosblYnNm);
                textviewRtkAbleEtc.setText(rtkPosblEtc);
                textviewLocationDetail.setText(pointsPath);
                textviewEtc.setText(ptclmatr);

                putImgView(imageviewKImg, 50, kImg);
                putImgView(imageviewOImg, 50, oImg);
                putImgView(imageviewPImg, 50, msurpointsLcDrw);
                putImgView(imageviewRoughmap, 50, rogMap);

                if(null!=imageviewRoughmap.getDrawable())
                {
                    mRogmapAttacher = new PhotoViewAttacher(imageviewRoughmap);//약도이미지 확대/축소 라이브러리 등록
                }

                //params set
                params.put("npsi_bf_sn", String.valueOf(npsiBfSn));
                params.put("npu_sn", String.valueOf(npuSn));
                params.put("inq_de", inqDe);
                params.put("inq_instt_cd", inqInsttCd);
                params.put("inq_psitn", inqPsitn);
                params.put("inq_psitn_nm", inqPsitnNm);
                params.put("inq_ofcps", inqOfcps);
                params.put("inq_ofcps_nm", inqOfcpsNm);
                params.put("inq_nm", inqNm);
                params.put("inq_id", inqId);
                params.put("inq_mtr_cd", inqMtrCd);
                params.put("inq_mtr_nm", inqMtrNm);
                params.put("mtr_sttus_cd", mtrSttusCd);
                params.put("mtr_sttus_nm", mtrSttusNm);
                params.put("rtk_posbl_yn", rtkPosblYn);
                params.put("rtk_posbl_yn_nm", rtkPosblYnNm);
                params.put("ptclmatr", ptclmatr);
                params.put("inq_mtr_etc", inqMtrEtc);
                params.put("mtr_sttus_etc", mtrSttusEtc);
                params.put("rtk_posbl_etc", rtkPosblEtc);
                params.put("k_img", kImg);
                params.put("o_img", oImg);
                params.put("msurpoints_lc_drw", msurpointsLcDrw);
                params.put("rog_map", rogMap);
                params.put("sttus_rslt_acptnc_sttus", inqInsttCd);
                params.put("rl_inq_psitn", rlInqPsitn);
                params.put("rl_inq_psitn_nm", rlInqPsitnNm);
                params.put("rl_inq_clsf", rlInqClsf);
                params.put("rl_inq_clsf_nm", rlInqClsfNm);
                params.put("rl_inq_nm", rlInqNm);
                params.put("rl_inq_id", rlInqId);
                params.put("points_path", pointsPath);
                params.put("point_secd", pointSecd);
                params.put("sttus_rslt_acptnc_sttus", sttusRsltAcptncSttus);

                //params.put("points_path", kImgFile);
                //params.put("points_path", rogMapFile);
                //params.put("points_path", oImgFile);
                //params.put("points_path", msurpointsLcDrwFile);

               // params.put("inq_sttus", inqSttus);

            }

            result.close();

        }catch (NullPointerException e) {
            Log.e(getString(R.string.nullPointerException), e.getMessage());
        }finally {
            DatabaseManager.getInstance().closeDatabase();
            Log.i(getString(R.string.done), "makeDetailView done");
        }
    }

    public void putImgView(ImageView img, int pixel, String filename) {
        BitmapFactory.Options bitopt=new BitmapFactory.Options();
        bitopt.inSampleSize=1;

        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MRNS/" + filename;
        File imageFile = new File(filepath);

        if(filename != null && imageFile.exists()) {
            FileInputStream fis = null;

            try{
                fis = new FileInputStream(imageFile);
            }catch (FileNotFoundException e){
                Log.e(getString(R.string.fileNotFoundException), e.getMessage());
            }

            Bitmap bi = BitmapFactory.decodeStream(fis);
            if(bi!=null) {
                img.setImageBitmap(setRoundCorner(bi, pixel));
            }
        }else {
            try {
                InputStream open = getAssets().open("noimage.png");
                Bitmap noImage = BitmapFactory.decodeStream(open);
                img.setImageBitmap(noImage);
            } catch (IOException e) {
                Log.e("", "이미지 없음");
            }
        }
    }

    public static Bitmap setRoundCorner(Bitmap bitmap, int pixel) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, pixel, pixel, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}
