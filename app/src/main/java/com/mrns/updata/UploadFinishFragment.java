package com.mrns.updata;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.main.R;
import com.mrns.sqllite.DBHelper;
import com.mrns.sqllite.DatabaseManager;
import com.mrns.utils.CommonUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UploadFinishFragment extends Fragment {

    boolean bOnline;
    private Intent movePage;
    ListView finishList;
    FinishList_Item_Adapter fAdapter;

    SQLiteDatabase db;
    DBHelper helper;

    int pageNo = 1;
    int pageSize = 10;
    int offsetNum = 0;
    boolean lastitemVisibleFlag = false;
    int listCount = 0;
    int totPage = 1;

    String investFinishListUrl = "";
    String userId;
    String deptCode;
    int socketTimeout = 10000;
    CommonUtils mCommonUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
        return inflater.inflate( R.layout.activity_upload_tab_finish, container, false );
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        mCommonUtils = new CommonUtils(getContext());

        //List set
        finishList = (ListView) view.findViewById(R.id.FinishList);
        fAdapter = new FinishList_Item_Adapter(view.getContext());

        SharedPreferences preSession = getContext().getSharedPreferences("preSession", Activity.MODE_PRIVATE);
        userId = preSession.getString(getString(R.string.key_inq_user_id), getString(R.string.empty));
        deptCode = preSession.getString("dept_code", "");

        //리스트 호출(local sqlite)
//        doMakeFinishList(pageNo);

        //리스트 호출 JSON webserver
        doMakeFinishListWeb(pageNo);

        finishList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FinishList_Item fItem = (FinishList_Item) fAdapter.getItem(position);

                //getData
                int finishSeq = fItem.getSeq();
                movePage = UploadFinishDetailActivity_.intent(view.getContext()).get();
                movePage.putExtra(getString(R.string.key_bonline), bOnline);
                movePage.putExtra("finishSeq", finishSeq);
                startActivity(movePage);
            }
        });

        finishList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    pageNo = pageNo + 1;
                    doMakeFinishListWeb(pageNo);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastitemVisibleFlag = (listCount > 0 && (firstVisibleItem + visibleItemCount) == totalItemCount && pageNo < totPage);
            }
        });
    }

    void doMakeFinishListWeb(final int pNo) {
        investFinishListUrl = getString(R.string.server_ip) + getString(R.string.req_finishlist);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getContext());
        Request request = new StringRequest(Request.Method.POST, investFinishListUrl, successListener, failListener) {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hash = new HashMap<>();
                hash.put("dept_code", deptCode);
                hash.put("pageNum", String.valueOf(pNo));
                hash.put("blockCount", String.valueOf(pageSize));
                return hash;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    private Response.Listener<String> successListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String reponse)
        {
            JSONObject jObject;
            try {
                JSONObject jObj = new JSONObject(reponse);
                JSONObject obj = jObj.getJSONObject("listXmlReturnVO");
                pageSize = obj.getInt("blockCount");
                totPage = obj.getInt("totPage");

                //JSONArray Set
                JSONArray listArr = new JSONArray(obj.getJSONArray("resultList").toString());
                listCount = listArr.length();
                for(int i=0; i< listCount; i++) {

                    jObject = listArr.getJSONObject(i);

                    String points_knd_cd = jObject.getString("points_knd_cd");
                    String points_nm = jObject.getString("points_nm");
                    String mtr_sttus_cd = jObject.getString("mtr_sttus_cd");
                    String inq_de = jObject.getString("inq_de");
                    String inq_sttus = "3";
                    int sttus_bf_sn = jObject.getInt("sttus_bf_sn");

                    Resources fRes = getResources();
                    fAdapter.addItem(new FinishList_Item(
                            getColorCode(points_knd_cd), points_nm, getMTRSTTUS(mtr_sttus_cd), inq_de, getViewStu(inq_sttus), sttus_bf_sn, fRes.getDrawable(R.drawable.btn_map_point_n)));
                }

            } catch (JSONException e) {
                e.getMessage();
            } finally {
                Log.i("done", "successListener done");
            }


            //Data Binding
            finishList.setAdapter(fAdapter);

            //ListView Position Location Set
            finishList.post(new Runnable() {
                @Override
                public void run() {
                    int pos;
                    if (pageNo == 1) {
                        pos = 0;
                    } else {
                        pos = (pageNo - 1) * pageSize;
                    }

                    finishList.setSelection(pos);
                }
            });

        }
    };

    protected Response.ErrorListener failListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError e) {
        e.getCause();
        }
    };

    void doMakeFinishList(int pno){

        try {
            helper = new DBHelper(getContext(), getString(R.string.db_full_name), null, 1);
            DatabaseManager.initializeInstance(helper);
            db = DatabaseManager.getInstance().openDatabase();

            offsetNum = (pno -1) * pageSize;

            //get totPage
            String countSql = "SELECT sttus_bf_sn FROM RNS_STTUS_INQ_BEFORE WHERE inq_sttus = 3";
            Cursor tresult = db.rawQuery(countSql, null);
            int totCount = tresult.getCount();
            tresult.close();

            if((totCount % pageSize) == 0){
                totPage = (totCount / pageSize);
            }else{
                totPage = (totCount / pageSize) + 1;
            }

            String sql = "SELECT  " +
                    "A.sttus_bf_sn,  " +
                    "A.points_knd_cd,  " +
                    "A.points_nm,  " +
                    "A.mtr_sttus_cd,  " +
                    "A.inq_de,  " +
                    "A.inq_sttus,  " +
                    "FROM RNS_STTUS_INQ_BEFORE A, RNS_STDRSPOT_UNITY B  " +
                    "WHERE  " +
                    "A.stdrspot_sn = B.stdrspot_sn  " +
                    "AND inq_sttus = 3  " +
                    "ORDER BY A.sttus_bf_sn, A.points_nm , A.points_knd_cd " +
                    "LIMIT " + pageSize + " OFFSET " + offsetNum;

            Cursor result = db.rawQuery(sql, null);
            listCount = result.getCount();

            Resources fRes = getResources();

            while (result.moveToNext()) {
                int sttus_bf_sn = result.getInt(0);
                String points_knd_cd = result.getString(1);
                String points_nm = result.getString(2);
                String mtr_sttus_cd = result.getString(3);
                String inq_de = result.getString(4);
                if (inq_de != null) inq_de = inq_de.substring(0, 10);
                String inq_sttus = result.getString(5);

                fAdapter.addItem(new FinishList_Item(
                        getColorCode(points_knd_cd), points_nm, getMTRSTTUS(mtr_sttus_cd), inq_de, getViewStu(inq_sttus), sttus_bf_sn, fRes.getDrawable(R.drawable.btn_map_point_n)));
            }

            result.close();

        }catch (NullPointerException e) {
            Log.e(getString(R.string.nullPointerException), e.getMessage());
        }finally {
            DatabaseManager.getInstance().closeDatabase();
            Log.i(getString(R.string.done), "doMakeFinishList done");
        }

        //Data Binding
        finishList.setAdapter(fAdapter);

        //ListView Position Location Set
        finishList.post(new Runnable() {
            @Override
            public void run() {
                int pos;
                if (pageNo == 1) {
                    pos = 0;
                } else {
                    pos = (pageNo - 1) * pageSize;
                }

                finishList.setSelection(pos);
            }
        });
    }

    public String getSDPath() {
        File extSt = Environment.getExternalStorageDirectory();
        return extSt.getAbsolutePath();
    }

    private String getMTRSTTUS(String sttus){
        return mCommonUtils.getMtrSttus(sttus, false);
    }

    private String getViewStu(String sttus){
        return mCommonUtils.getInqSttus(sttus);
    }

    private Drawable getColorCode(String points_knd){
        return getResources().getDrawable(mCommonUtils.getPointKndRes(points_knd));
    }
}
