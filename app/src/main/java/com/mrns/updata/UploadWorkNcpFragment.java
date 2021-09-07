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
import com.mrns.updata.adapter.UploadWorkListNcpAdapter;
import com.mrns.updata.adapter.listener.ClickListener;
import com.mrns.updata.record.UploadWorkNcpRecord;
import com.mrns.utils.CommonUtils;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UploadWorkNcpFragment extends Fragment {

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
    ArrayList<UploadWorkNcpRecord> items;
    UploadWorkListNcpAdapter adapter;
    CheckBox upload_ckall;
    ArrayList<Boolean> arr;
    UploadWorkNcpRecord record;
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


        adapter = new UploadWorkListNcpAdapter(getContext());
        adapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                movePage = UploadWorkDetailNcpActivity_.intent(v.getContext()).get();
                movePage.putExtra(getString(R.string.key_bonline), bOnline);
                movePage.putExtra("workSeq", (int) items.get(position).getNpuSn());
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
            String countSql = "SELECT npsi_bf_sn FROM RNS_NP_STTUS_INQ_BF WHERE inq_sttus = 2";
            Cursor tresult = db.rawQuery(countSql, null);
            int totCount = tresult.getCount();
            tresult.close();

            if((totCount % pageSize) == 0){
                totPage = (totCount / pageSize);
            }else{
                totPage = (totCount / pageSize) + 1;
            }

            String sql = "SELECT  " +
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
                    "BF.rl_inq_id, " +  //31
                    "BF.inq_sttus, " +  //31
                    "UNITY.POINTS_KND_CD, + " +    //32
                    "UNITY.POINTS_NM " +        //33
                    "FROM RNS_NP_STTUS_INQ_BF BF, RNS_NP_UNITY UNITY " +
                    "WHERE  " +
                    "inq_sttus = 2  " +
                    "AND BF.NPU_SN = UNITY.NPU_SN " +
                    "ORDER BY npsi_bf_sn;";

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
                String inqSttus = result.getString(32);
                String pointKindCode = result.getString(33);
                String pointName = result.getString(34);
                jobt = new JSONObject();

                //params set
                jobt.put("npsi_bf_sn", String.valueOf(npsiBfSn));
                jobt.put("npu_sn", String.valueOf(npuSn));
                jobt.put("inq_de", getNullString(inqDe));
                jobt.put("inq_instt_cd", getNullString(inqInsttCd));
                jobt.put("inq_psitn", getNullString(inqPsitn));
                jobt.put("inq_psitn_nm", getNullString(inqPsitnNm));
                jobt.put("inq_ofcps", getNullString(inqOfcps));
                jobt.put("inq_ofcps_nm", getNullString(inqOfcpsNm));
                jobt.put("inq_nm", getNullString(inqNm));
                jobt.put("inq_id", getNullString(inqId));
                jobt.put("inq_mtr_cd", getNullString(inqMtrCd));
                jobt.put("inq_mtr_nm", getNullString(inqMtrNm));
                jobt.put("mtr_sttus_cd", getNullString(mtrSttusCd));
                jobt.put("mtr_sttus_nm", getNullString(mtrSttusNm));
                jobt.put("rtk_posbl_yn", getNullString(rtkPosblYn));
                jobt.put("rtk_posbl_yn_nm", getNullString(rtkPosblYnNm));
                jobt.put("ptclmatr", getNullString(ptclmatr));
                jobt.put("inq_mtr_etc", getNullString(inqMtrEtc));
                jobt.put("mtr_sttus_etc", getNullString(mtrSttusEtc));
                jobt.put("rtk_posbl_etc", getNullString(rtkPosblEtc));
                jobt.put("k_img", getNullString(kImg));
                jobt.put("o_img", getNullString(oImg));
                jobt.put("msurpoints_lc_drw", getNullString(msurpointsLcDrw));
                jobt.put("rog_map", getNullString(rogMap));
                jobt.put("sttus_rslt_acptnc_sttus", getNullString(sttusRsltAcptncSttus));
                jobt.put("rl_inq_psitn", getNullString(rlInqPsitn));
                jobt.put("rl_inq_psitn_nm", getNullString(rlInqPsitnNm));
                jobt.put("rl_inq_clsf", getNullString(rlInqClsf));
                jobt.put("rl_inq_clsf_nm", getNullString(rlInqClsfNm));
                jobt.put("rl_inq_nm", getNullString(rlInqNm));
                jobt.put("rl_inq_id", getNullString(rlInqId));
                jobt.put("points_path", getNullString(pointsPath));
                jobt.put("point_kind_code", getNullString(pointKindCode));
                jobt.put("point_name", getNullString(pointName));

                jListArr.put(jobt);
                record = new UploadWorkNcpRecord(
                        npuSn,
                        pointKindCode,
                        pointName,
                        mtrSttusCd,
                        inqDe,
                        inqSttus,
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
            updateCnt = db.update("RNS_NP_STTUS_INQ_BF", values, "npu_sn=" + stdrspot_sn, null);
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
            String arrayKeys[] = getResources().getStringArray(R.array.array_upload_work_detail_ncp);

            for (String key : arrayKeys) {
                if (key.equals("inq_intt_cd") || key.equals("inq_mtr_etc") || key.equals("sttus_rslt_acptnc_sttus)")) {

                } else {
                    params.put(key, uploadParam.get(key));
                }
            }

            String strPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + getString(R.string.mrns) + File.separator;
            String fileName;

            fileName = (String)uploadParam.get("k_img");

            if (!fileName.equals("")) {
                File k_img_file = new File(strPath + fileName);
                params.put("kImgFile", k_img_file);
            }

            fileName = (String)uploadParam.get("o_img");

            if (!fileName.equals("")) {
                File o_img_file = new File(strPath + fileName);
                params.put("oImgFile", o_img_file);
            }

            fileName = (String)uploadParam.get("msurpoints_lc_drw");

            if (!fileName.equals("")) {
                File msurpoints_lc_drw_file = new File(strPath + fileName);
                params.put("msurpointsLcDrwFile", msurpoints_lc_drw_file);
            }

            fileName = (String)uploadParam.get("rog_map");

            if (!fileName.equals("")) {
                File rog_map_file = new File(strPath + fileName);
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
        }

    }

    void FileUpload(){
        /*client  = new AsyncHttpClient();
        client.setURLEncodingEnabled(false);
        client.setResponseTimeout(socketTimeout);*/

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
        final String url  = getString(R.string.server_ip) + getString(R.string.req_uploadInvestNcp);
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
