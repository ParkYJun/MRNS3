package com.mrns.updata;

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
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
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
import com.mrns.invest.record.RnsSttusInqBefore;
import com.mrns.invest.util.DBUtil;
import com.mrns.main.R;
import com.mrns.map.MapWebActivity;
import com.mrns.sqllite.DBHelper;
import com.mrns.sqllite.DatabaseManager;
import com.mrns.utils.CommonUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_upload_finish_detail)
public class UploadFinishDetailActivity extends AppCompatActivity {

    SQLiteDatabase db;
    DBHelper helper;

    String rogImgUrl;
    String kImgUrl;
    String oImgUrl;
    String msurpointsLcDrwUrl;

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
    @ViewById(R.id.imageview_kimg)
    ImageView imageviewKImg;        //근경
    @ViewById(R.id.imageview_oimg)
    ImageView imageviewOImg;      //원경
    @ViewById(R.id.imageview_pimg)
    ImageView imageviewPImg;      //측정점위치설명도
    @ViewById(R.id.imageview_roughmap)
    ImageView imageviewRoughmap;	    //약도

    int exFinishSeq;
    boolean exBOnline;

    String points_knd_cd;
    String base_point_no;
    long stdrspot_sn;
    String investFinishDetailUrl = "";
    int socketTimeout = 100000;
    private NetcheckMoniter networkMoniter = null;
    CommonUtils mCommonUtils;

    @AfterViews
    protected void InitView() {
        mCommonUtils = new CommonUtils(getApplicationContext());
        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        exBOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), exBOnline);
        exFinishSeq = getIntent().getIntExtra("finishSeq", exFinishSeq);

        makeDetailViewWeb();
        //makeDetailView();
    }

    @Click(R.id.layout_back_home)
    public void onBackHome() {
        btnBackHome.setImageResource(R.drawable.nav_btn_back_p);
        finish();
    }

    @Click(R.id.btn_view_location)
    public void onLocation() {
        Intent movePage = new Intent(UploadFinishDetailActivity.this, MapWebActivity.class);
        movePage.putExtra(getString(R.string.key_mode), 1);
        movePage.putExtra(getString(R.string.key_type), getString(R.string.key_point_type_for_map_query));
        movePage.putExtra(getString(R.string.key_fid), base_point_no);
        movePage.putExtra(getString(R.string.key_stdr_spot_sn_json), stdrspot_sn);
        startActivity(movePage);
    }

    void makeDetailViewWeb() {
        investFinishDetailUrl = getString(R.string.server_ip) + getString(R.string.req_finishdetail);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        Request request = new StringRequest(Request.Method.POST, investFinishDetailUrl, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("sttus_bf_sn", String.valueOf(exFinishSeq));
                return hash;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response)
        {
            RnsSttusInqBefore before;
            before = investBeforeArrJsonPaser(response);

            try {
                textviewTitle.setText(getString(R.string.title_points, before.getPointsNm(), mCommonUtils.getPointsKind(before.getPointsKndCd())));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            textviewDate.setText(nullCkStr(before.getInqDe()));
            textviewPosition.setText(nullCkStr(before.getInqPsitn()));
            textviewClassification.setText(nullCkStr(before.getInqClsf()));
            textviewName.setText(nullCkStr(before.getInqNm()));
            textviewMtrQuality.setText(nullCkStr(DBUtil.getCmmnCode("109", before.getInqMtrCd())));
            textviewMtrQualityEtc.setText(nullCkStr(before.getInqMtrEtc()));
            textviewMtrSttus.setText(getMTRSTTUS(nullCkStr(before.getMtrSttusCd())));//mtr_sttus_nm
            textviewMtrSttusEtc.setText(nullCkStr(before.getMtrSttusEtc()));
            textviewLocationDetail.setText(nullCkStr(before.getLocplcDetail()));
            textviewEtc.setText(nullCkStr(before.getPtclmatr()));
            textviewRtkAble.setText(getPtclma(nullCkStr(before.getRtkPosblYn())));//rtk_posbl_yn_nm
            textviewRtkAbleEtc.setText(nullCkStr(before.getRtkPosblEtc()));

            //약도 불러오기
            rogImgUrl = getString(R.string.server_ip) + getString(R.string.req_uploadrimg);
            rogImgUrl+= "?sttus_bf_sn=" + exFinishSeq;
            new DownloadImageTask(imageviewRoughmap).execute(rogImgUrl,"r");

            //근경 불러오기
            kImgUrl = getString(R.string.server_ip) + getString(R.string.req_uploadkimg);
            kImgUrl+= "?sttus_bf_sn=" + exFinishSeq;
            new DownloadImageTask(imageviewKImg).execute(kImgUrl,"k");

            //원경 불러오기
            oImgUrl = getString(R.string.server_ip) + getString(R.string.req_uploadoimg);
            oImgUrl+= "?sttus_bf_sn=" + exFinishSeq;
            new DownloadImageTask(imageviewOImg).execute(oImgUrl,"o");

            //측정점 위치설명도 불러오기
            msurpointsLcDrwUrl = getString(R.string.server_ip) + getString(R.string.req_uploadpimg);
            msurpointsLcDrwUrl += "?sttus_bf_sn=" + exFinishSeq;
            new DownloadImageTask(imageviewPImg).execute(msurpointsLcDrwUrl, "p");
        }
    };

    private String getPtclma(String sttus){
        return mCommonUtils.getPtclma(sttus);
    }

    private String getMTRSTTUS(String sttus){
        return mCommonUtils.getMtrSttus(sttus, false);
    }

    private String nullCkStr(String str){
        if(str.equals("null")) {
            str ="-";
        }

        return str;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String argStr;
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            argStr = urls[1];
            Bitmap mIcon11 = null;

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                if(mIcon11 == null){
                    noImage(argStr);
                }
            } catch (Exception e) {
                noImage(argStr);
                Log.e(getString(R.string.exception), e.getMessage());
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            bmImage.setImageBitmap(result);
        }
    }

    protected void noImage(final String modeStr){
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        BitmapDrawable img = (BitmapDrawable)getResources().getDrawable(R.drawable.noimage);

                        if (modeStr.equalsIgnoreCase("o")) {
                            imageviewOImg.setImageDrawable(img);
                        } else if (modeStr.equalsIgnoreCase("k")) {
                            imageviewKImg.setImageDrawable(img);
                        } else if (modeStr.equalsIgnoreCase("r")) {
                            imageviewRoughmap.setImageDrawable(img);
                        } else if (modeStr.equalsIgnoreCase("p")) {
                            imageviewPImg.setImageDrawable(img);
                        }
                    }
                });
            }
        }).start();

    }

    public static RnsSttusInqBefore investBeforeArrJsonPaser(String str) {
        try {
            JSONObject jObj = new JSONObject(str);
            JSONObject bDetail = jObj.getJSONObject("listXmlReturnVO");
            JSONObject data = bDetail.getJSONObject("resultVO");
            return new RnsSttusInqBefore(
                    data.getLong("sttus_bf_sn"),
                    data.getLong("sttus_inq_stdr_points_list"),
                    data.getLong("stdrspot_sn"),
                    data.getString("base_point_no"),
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
                    data.getString("sttus_rslt_acptnc_sttus"),
                    data.getString("sttus_rslt_acptnc_cancel"),
                    data.getString("mtr_sttus_cd"),
                    data.getString("inq_sttus"),
                    data.getString("inq_mtr_etc"),
                    data.getString("mtr_sttus_etc"),
                    data.getString("rtk_posbl_etc"),
                    data.getString("locplc_detail"),
                    data.getString("progression"),
                    data.getString("rl_inq_psitn"),
                    data.getString("rl_inq_clsf"),
                    data.getString("rl_inq_nm"),
                    data.getString("rl_inq_id")
            );
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
            return null;
        }finally {
            Log.i("done", "investBeforeJsonPaser done");
        }
    }

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {
        }
    };

    void makeDetailView() {
        try{
            helper = new DBHelper(UploadFinishDetailActivity.this, getString(R.string.db_full_name), null, 1);
            DatabaseManager.initializeInstance(helper);
            db = DatabaseManager.getInstance().openDatabase();

            String sql = "SELECT " +
                    "points_nm, " +
                    "inq_psitn, " +
                    "inq_clsf, " +
                    "inq_nm, " +
                    "inq_de, " +
                    "ptclmatr, " +
                    "(SELECT CODE_VALUE FROM ST_CMMN_CODE_DTLS WHERE CODE = points_knd_cd AND CODE_CLCD=100) points_knd_nm, " +
                    "(SELECT CODE_VALUE FROM ST_CMMN_CODE_DTLS WHERE CODE = mtr_sttus_cd AND CODE_CLCD=110) mtr_sttus_nm, " +
                    "(SELECT CODE_VALUE FROM ST_CMMN_CODE_DTLS WHERE CODE = rtk_posbl_yn AND CODE_CLCD=111) rtk_posbl_yn_nm, " +
                    "k_img, " +
                    "o_img, " +
                    "msurpoints_lc_drw, " +
                    "rog_map, " +
                    "mtr_sttus_etc, " +
                    "rtk_posbl_etc, " +
                    "locplc_detail,  " +
                    "(SELECT CODE_VALUE FROM ST_CMMN_CODE_DTLS WHERE CODE = inq_clsf AND CODE_CLCD=150) inq_clsf_nm " +
                    "FROM RNS_STTUS_INQ_BEFORE " +
                    "WHERE " +
                    "sttus_bf_sn = " + exFinishSeq;

            Cursor result = db.rawQuery(sql, null);

            while (result.moveToNext()){
                String pointsNm = result.getString(0);
                String inqPsitn = result.getString(1);
                String inqClsf = result.getString(2);
                String inqNm = result.getString(3);
                String inqDe = result.getString(4);
                if(inqDe != null) inqDe = inqDe.substring(0,10);
                String ptclmatr = result.getString(5);
                String pointsKndNm = result.getString(6);
                String mtrSttusNm = result.getString(7);
                String rtkPosblYnNm = result.getString(8);
                String kImg = result.getString(9);
                String oImg = result.getString(10);
                String msurpointsLcDrw = result.getString(11);
                String rogMap = result.getString(12);
                String mtrSttusEtc = result.getString(13);
                String rtkPosblEtc = result.getString(14);
                String locplcDetail = result.getString(15);
                String inqClsfNm = result.getString(16);

                textviewTitle.setText(getString(R.string.title_points, pointsNm, pointsKndNm));
                textviewDate.setText(inqDe);
                textviewPosition.setText(inqPsitn);
                textviewClassification.setText(inqClsfNm);
                textviewName.setText(inqNm);
                textviewMtrSttus.setText(mtrSttusNm);
                textviewMtrSttusEtc.setText(mtrSttusEtc);
                textviewEtc.setText(ptclmatr);
                textviewRtkAble.setText(rtkPosblYnNm);
                textviewRtkAbleEtc.setText(rtkPosblEtc);
                textviewLocationDetail.setText(locplcDetail);

                putImgView(imageviewKImg, 50, kImg);
                putImgView(imageviewOImg, 50, oImg);
                putImgView(imageviewPImg, 50, msurpointsLcDrw);
                putImgView(imageviewRoughmap, 50, rogMap);
            }

            result.close();

        }catch (NullPointerException e) {
            Log.e("NullPointerException", e.getMessage());
        }finally {
            DatabaseManager.getInstance().closeDatabase();
            Log.i("done", "makeDetailView done");
        }
    }

    public void putImgView(ImageView img, int pixel, String filename) {
        BitmapFactory.Options bitopt=new BitmapFactory.Options();
        bitopt.inSampleSize=1;

        String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/MRNS/" + filename;
        File imageFile = new File(filepath);

        if(imageFile.exists()) {
            FileInputStream fis = null;

            try{
                fis = new FileInputStream(imageFile);
            }catch (FileNotFoundException e){
                Log.e("FileNotFoundException", e.getMessage());
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
