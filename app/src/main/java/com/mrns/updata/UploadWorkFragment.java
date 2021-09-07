package com.mrns.updata;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.mrns.main.R;
import com.mrns.sqllite.DBHelper;
import com.mrns.sqllite.DatabaseManager;
import com.mrns.updata.adapter.UploadWorkListAdapter;
import com.mrns.updata.adapter.listener.ClickListener;
import com.mrns.updata.record.UploadWorkRecord;
import com.mrns.utils.CommonUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UploadWorkFragment extends Fragment {

    boolean bOnline;
    RecyclerView workList;
    WorkList_Item_Adapter wAdapter;
    TextView btn_upload;
    private Intent movePage;

    SQLiteDatabase db;
    DBHelper helper;
    CommonUtils mCommonUtils;

    int pageNo = 1;
    int pageSize = 8;
    int offsetNum = 0;
    int listCount = 0;
    int tmpCnt = 0;
    int totPage = 0;

    int socketTimeout = 300000;
    final int DEFAULT_TIMEOUT = 20 * 1000;

    private static AsyncHttpClient client;
    JSONArray jListArr;
    int sCnt = 0;
    RequestParams params;

    //RecyclerView 변경 로직
    ArrayList<UploadWorkRecord> items;
    UploadWorkListAdapter adapter;
    CheckBox upload_ckall;
    ArrayList<Boolean> arr;
    UploadWorkRecord record;
    int ckCount = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.activity_upload_tab_work, container, false );
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mCommonUtils = new CommonUtils(getContext());

        SharedPreferences preSession = getContext().getSharedPreferences("preSession", Activity.MODE_PRIVATE);

        upload_ckall = (CheckBox) view.findViewById(R.id.upload_ckall);
        upload_ckall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upload_ckall.isChecked()){
                    doMakeUploadList(true);
                }else {
                    doMakeUploadList(false);
                }

            }
        });

        btn_upload = (TextView) view.findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ckCount == 0) {
                    Toast.makeText(getContext(), getString(R.string.uploadError), Toast.LENGTH_SHORT).show();
                }else {
                    mProgressDialog = new ProgressDialog(getContext());
                    mProgressDialog.setMessage("현황조사 자료를 업로드 중입니다.");
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();

                    sCnt = 0;
                    try {
                        investUpload();
                    } catch (FileNotFoundException e) {
                        Log.e(getString(R.string.fileNotFoundException), e.getMessage());
                    } finally {
                        Log.i(getString(R.string.done), "investUpload done");
                    }
                }


            }// end onClick
        });// end setOnClickListener

        //List set
        workList = (RecyclerView) view.findViewById(R.id.work_list);


        adapter = new UploadWorkListAdapter(getContext());
        adapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                movePage = UploadWorkDetailActivity_.intent(v.getContext()).get();
                movePage.putExtra(getString(R.string.key_bonline), bOnline);
                movePage.putExtra("workSeq", (int) items.get(position).getSttusBfSn());
                startActivity(movePage);
            }

            @Override
            public void onCheckBoxClick(int position, View v) {

                boolean bflag = arr.get(position);
                if(bflag){
                    items.get(position).setIsSelectecd(false);
                }else{
                    items.get(position).setIsSelectecd(true);
                }

                arr.set(position, items.get(position).isSelectecd());

                ckCount= 0;
                for(int i=0; i<items.size(); i++) {
                    if(items.get(i).isSelectecd()) ckCount ++;
                }

                if(items.size() == ckCount) {
                    upload_ckall.setChecked(true);
                }else {
                    upload_ckall.setChecked(false);
                }
            }


        });
        workList.setLayoutManager(new LinearLayoutManager(getContext()));
        workList.setHasFixedSize(true);
        workList.setAdapter(adapter);
        workList.setItemAnimator(new DefaultItemAnimator());

        doMakeUploadList(true);

    }

    public String getSDPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    void doMakeUploadList(boolean flagCkall) {
        try{
            helper = new DBHelper(getContext(), getString(R.string.db_full_name), null, 1);
            DatabaseManager.initializeInstance(helper);
            db = DatabaseManager.getInstance().openDatabase();

            offsetNum = (pageNo -1) * pageSize;

            //get totPage
            String countSql = "SELECT sttus_bf_sn FROM RNS_STTUS_INQ_BEFORE WHERE inq_sttus = 2";
            Cursor tresult = db.rawQuery(countSql, null);
            int totCount = tresult.getCount();
            tresult.close();

            if((totCount % pageSize) == 0){
                totPage = (totCount / pageSize);
            }else{
                totPage = (totCount / pageSize) + 1;
            }

            String sql = "SELECT  " +
                    "sttus_bf_sn,  " +
                    "base_point_no,  " +
                    "points_knd_cd,  " +
                    //"(SELECT CODE_VALUE FROM ST_CMMN_CODE_DTLS WHERE CODE = points_knd_cd) points_knd,  " +
                    "points_knd_cd,  " +
                    "points_nm,  " +
                    "mtr_sttus_cd,  " +
                    "inq_de,  " +
                    "inq_sttus,  " +
                    "inq_psitn, " +
                    "inq_clsf, " +
                   "inq_nm, " +
                    "inq_mtr_cd, " +
                    "rtk_posbl_yn, " +
                   "ptclmatr, " +
                   "k_img, " +
                    "o_img, " +
                    "msurpoints_lc_drw, " +
                    "rog_map, " +
                   "inq_mtr_etc, " +
                   "mtr_sttus_etc, " +
                   "rtk_posbl_etc, " +
                    "locplc_detail,"+
                  "inq_id, " +
                  "sttus_inq_stdr_points_list, " +
                  "stdrspot_sn, " +
                  "jgrc_cd, " +
                  "brh_cd, " +
                  "inq_instt_cd ," +
                  "rl_inq_psitn,  " +
                  "rl_inq_clsf,  " +
                  "rl_inq_nm,  " +
                  "rl_inq_id " +
                    "FROM RNS_STTUS_INQ_BEFORE " +
                    "WHERE  " +
                    "inq_sttus = 2  " +
                    "ORDER BY sttus_bf_sn, points_nm, points_knd_cd;";

            Cursor result = db.rawQuery(sql, null);
            listCount = result.getCount();

            if(flagCkall) {
                ckCount = result.getCount();
            }else{
                ckCount = 0;
            }

            arr = new ArrayList<>(listCount);

            jListArr = new JSONArray();

            items = new ArrayList<>();
            adapter.setItems(items);

            int i = 0;
            JSONObject jobt;

            while (result.moveToNext()){
                int sttus_bf_sn = result.getInt(0);
                String base_point_no = result.getString(1);
                String points_knd = result.getString(2);
                String points_knd_cd = result.getString(3);
                String points_nm = result.getString(4);
                String mtr_sttus_cd = result.getString(5);
                String inq_de = result.getString(6);
                if(inq_de != null) inq_de = inq_de.substring(0,10);
                String inq_sttus = result.getString(7);
                String inq_psitn = result.getString(8);
                String inq_clsf = result.getString(9);
                String inq_nm = result.getString(10);
                String inq_mtr_cd = result.getString(11);
                String rtk_posbl_yn = result.getString(12);
                String ptclmatr = result.getString(13);
                String k_img = result.getString(14);
                String o_img = result.getString(15);
                String msurpoints_lc_drw = result.getString(16);
                String rog_map = result.getString(17);
                String inq_mtr_etc = result.getString(18);
                String mtr_sttus_etc = result.getString(19);
                String rtk_posbl_etc = result.getString(20);
                String locplc_detail = result.getString(21);
                String inq_id = result.getString(22);
                int sttus_inq_stdr_points_list = result.getInt(23);
                int stdrspot_sn = result.getInt(24);
                String jgrc_cd = result.getString(25);
                String brh_cd = result.getString(26);
                String inq_instt_cd = result.getString(27);
                String rl_inq_psitn = result.getString(28);
                String rl_inq_clsf = result.getString(29);
                String rl_inq_nm = result.getString(30);
                String rl_inq_id = result.getString(31);
                jobt = new JSONObject();

                //params set
                jobt.put("sttus_bf_sn", sttus_bf_sn);
                jobt.put(getString(R.string.key_base_point_no_json), getNullString(base_point_no));
                jobt.put("points_knd_cd", getNullString(points_knd_cd));
                jobt.put("points_nm", getNullString(points_nm));
                jobt.put("mtr_sttus_cd", getNullString(mtr_sttus_cd));
                jobt.put("inq_de", getNullString(inq_de));
                jobt.put("inq_sttus", getNullString(inq_sttus));
                jobt.put("inq_psitn", getNullString(inq_psitn));
                jobt.put("inq_clsf", getNullString(inq_clsf));
                jobt.put("inq_nm", getNullString(inq_nm));
                jobt.put("inq_mtr_cd", getNullString(inq_mtr_cd));
                jobt.put("rtk_posbl_yn", getNullString(rtk_posbl_yn));
                jobt.put("ptclmatr", getNullString(ptclmatr));
                jobt.put("k_img", getNullString(k_img));
                jobt.put("o_img", getNullString(o_img));
                jobt.put("msurpoints_lc_drw", getNullString(msurpoints_lc_drw));
                jobt.put("rog_map", getNullString(rog_map));
                jobt.put("inq_mtr_etc", getNullString(inq_mtr_etc));
                jobt.put("mtr_sttus_etc", getNullString(mtr_sttus_etc));
                jobt.put("rtk_posbl_etc", getNullString(rtk_posbl_etc));
                jobt.put("locplc_detail", getNullString(locplc_detail));
                jobt.put("inq_id", getNullString(inq_id));
                jobt.put("sttus_inq_stdr_points_list", sttus_inq_stdr_points_list);
                jobt.put(getString(R.string.key_stdr_spot_sn_json), stdrspot_sn);
                jobt.put("jgrc_cd", getNullString(jgrc_cd));
                jobt.put("brh_cd", getNullString(brh_cd));
                jobt.put("inq_instt_cd", getNullString(inq_instt_cd));
                jobt.put("rl_inq_psitn", getNullString(rl_inq_psitn));
                jobt.put("rl_inq_clsf", getNullString(rl_inq_clsf));
                jobt.put("rl_inq_nm", getNullString(rl_inq_nm));
                jobt.put("rl_inq_id", getNullString(rl_inq_id));

                jListArr.put(jobt);

                record = new UploadWorkRecord(
                        sttus_bf_sn,
                        points_knd,
                        points_nm,
                        mtr_sttus_cd,
                        inq_de,
                        inq_sttus,
                        flagCkall
                );
                items.add(record);

                arr.add(i, flagCkall);
                i++;

            }//end while
            adapter.notifyDataSetChanged();

            result.close();

        }catch (NullPointerException e) {
            Log.e(getString(R.string.nullPointerException), e.getMessage());
        }catch (JSONException e) {
            Log.e(getString(R.string.jSONException), e.getMessage());
        }
        finally {
            DatabaseManager.getInstance().closeDatabase();
            Log.i(getString(R.string.done), "doMakeUploadList done");
        }
    }


    public int getStatusComplete(int stdrspot_sn) {
        helper = new DBHelper(getContext(), getString(R.string.db_full_name), null, 1);
        DatabaseManager.initializeInstance(helper);
        db = DatabaseManager.getInstance().openDatabase();

        ContentValues values = new ContentValues();
        values.put("inq_sttus", 3);

        int updateCnt = 0;
        try {
            updateCnt = db.update("RNS_STTUS_INQ_BEFORE", values, "stdrspot_sn=" + stdrspot_sn, null);
        }catch (NullPointerException e) {
            Log.e(getString(R.string.nullPointerException), e.getMessage());
        }finally {
            DatabaseManager.getInstance().closeDatabase();
            Log.i(getString(R.string.done), "getStatusComplete done");
        }

        return updateCnt;

    }

    JSONObject uploadParam;
    private ProgressDialog mProgressDialog;
    void investUpload() throws FileNotFoundException {
        params = new RequestParams();

        for(int i=sCnt; i< arr.size(); i++) {
            if(arr.get(i)){
                sCnt = i;
                break;
            }
        }


        ///////////여기부터!!!!!!!!!!!!!!!!!!!
        try {
            uploadParam = jListArr.getJSONObject(sCnt);
            String arrayKeys[] = getResources().getStringArray(R.array.array_upload_work_detail);

            for (String key : arrayKeys) {
                params.put(key, uploadParam.get(key));
            }

            String strPath = getSDPath() + File.separator + getString(R.string.mrns) + File.separator;

            File k_img_file = new File(strPath + uploadParam.getString("k_img"));
            File o_img_file = new File(strPath + uploadParam.getString("o_img"));
            File msurpoints_lc_drw_file = new File(strPath + uploadParam.getString("msurpoints_lc_drw"));
            File rog_map_file = new File(strPath + uploadParam.getString("rog_map"));

            if(k_img_file.exists()){
                params.put("kImgFile", k_img_file);
            }

            if(o_img_file.exists()){
                params.put("oImgFile", o_img_file);
            }

            if (msurpoints_lc_drw_file.exists()) {
                params.put("msurpointsLcDrwFile", msurpoints_lc_drw_file);
            }

            if(rog_map_file.exists()){
                params.put("rogMapFile", rog_map_file);
            }

        } catch (JSONException e) {
            mProgressDialog.dismiss();
            Log.e(getString(R.string.jSONException), e.getMessage());
        } catch (Exception e) {
            Log.e(getString(R.string.exception), e.getMessage());
        }finally {
            sCnt ++;
            FileUpload();
            Log.i(getString(R.string.done), "investUpload done");
        }

    }

    void FileUpload(){
       /* client  = new AsyncHttpClient();
        client.setURLEncodingEnabled(false);
        client.setResponseTimeout(socketTimeout);
       */
        client  = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        client.setURLEncodingEnabled(false);
        /*
         * params
         * @url
         * @RequestParam params
         * @AsyncHttpResponseHandler responseHandler
         *
         */
        final String url  = getString(R.string.server_ip) + getString(R.string.req_uploadInvest);
        client.post(url, params, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                if(statusCode == 200) {
                    try {
                        JSONObject obt = response.getJSONObject("xmlReturnVO");
                        int seq = obt.getInt("result");
                        //현황조사 상태 변경하기
                        int cnt = getStatusComplete(seq);

                        tmpCnt++;

                        if(tmpCnt == ckCount) {//jListArr.length()
                            Handler mHandler = new Handler();
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressDialog.dismiss();
                                    Toast.makeText(getContext(), getString(R.string.uploadComplete), Toast.LENGTH_SHORT).show();
                                    Intent movePage = new Intent(getContext(), UploadActivity.class);
                                    movePage.putExtra(getString(R.string.key_bonline), true);
                                    startActivity(movePage);
                                }
                            });
                        }
                        if (sCnt <= ckCount){//jListArr.length()
                        //    if (sCnt <= ckCount){//jListArr.length()

                            try {
                                investUpload();

                            } catch (FileNotFoundException e) {
                                Log.e(getString(R.string.fileNotFoundException), e.getMessage());
                            } finally {
                                Log.i(getString(R.string.done), "investUpload done");
                            }
                        }

                    } catch (JSONException e) {
                        Log.e(getString(R.string.jSONException), e.getMessage());
                    } finally {
                        Log.i(getString(R.string.done), "onSuccess done");
                    }
                }else {
                    mProgressDialog.dismiss();
                    Intent movePage = new Intent(getContext(), UploadActivity.class);
                    movePage.putExtra(getString(R.string.key_bonline), true);
                    startActivity(movePage);
                    Toast.makeText(getContext(), "Server Error!!!!!!", Toast.LENGTH_LONG).show();
                }
            }//end onSuccess

            @Override
            public void onFailure(int statusCode, Header[] headers, String tt,  Throwable e){
                mProgressDialog.dismiss();
                Intent movePage = new Intent(getContext(), UploadActivity.class);
                movePage.putExtra(getString(R.string.key_bonline), true);
                startActivity(movePage);
                Toast.makeText(getContext(), "Internal Server Error!!!!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void  onFailure(int statusCode, Header[]headers, Throwable e, JSONObject obj){
                mProgressDialog.dismiss();
                Intent movePage = new Intent(getContext(), UploadActivity.class);
                movePage.putExtra(getString(R.string.key_bonline), true);
                startActivity(movePage);
                Toast.makeText(getContext(), "Internal Server Error!!!!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }

        });
    }

    public String getNullString (String str) {

        return str +"";

    }
}
