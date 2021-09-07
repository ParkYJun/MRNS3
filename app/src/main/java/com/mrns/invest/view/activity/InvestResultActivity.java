package com.mrns.invest.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mrns.invest.adapter.InvestResultListAdapter;
import com.mrns.invest.adapter.listener.ClickListener;
import com.mrns.invest.record.InvestResultRecord;
import com.mrns.invest.record.RnsNpUnity;
import com.mrns.invest.record.RnsStdrspotUnity;
import com.mrns.invest.util.EndlessRecyclerOnScrollListener;
import com.mrns.invest.util.FineDialog;
import com.mrns.invest.util.KeyboardUtil;
import com.mrns.main.MainActivity;
import com.mrns.main.R;
import com.mrns.map.MapWebActivity;

import com.mrns.updata.UploadWorkDetailActivity_;
import com.mrns.updata.UploadWorkDetailNcpActivity_;
import com.orm.SugarRecord;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

@EActivity(R.layout.activity_invest_result)
public class InvestResultActivity extends AppCompatActivity {

    ArrayList<InvestResultRecord> items;
    boolean bOnline;
    String mode;

    private String mPointKindCode;
    private String mAdmCode;
    private String mStartPointName;
    private String mEndPointName;
    private String mStartDate;
    private String mEndDate;
    private String mInqStatus;
    private String mInqDeSorting;
    private String mPointName;

    @ViewById(R.id.layout_point_class)
    LinearLayout layoutPointClass;
    @ViewById(R.id.tv_invest_result_title)
    TextView tv_invest_result_title;
    @ViewById(R.id.ib_invest_result_nav_back)
    ImageButton ib_invest_result_nav_back;
    @ViewById(R.id.ib_invest_result_nav_home)
    ImageButton ib_invest_result_nav_home;
    @ViewById(R.id.ll_invest_sort_install)
    LinearLayout ll_invest_sort_install;
    @ViewById(R.id.ib_invest_sort_install)
    ImageButton ib_invest_sort_install;
    @ViewById(R.id.ll_invest_sort_invest)
    LinearLayout ll_invest_sort_invest;
    @ViewById(R.id.ib_invest_sort_invest)
    ImageButton ib_invest_sort_invest;
    @ViewById(R.id.rv_invest_result)
    RecyclerView rv_invest_result;
    @ViewById(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;
    @ViewById(R.id.sp_invest_search_point_class)
    Spinner sp_invest_search_point_class;
    @ViewById(R.id.sp_invest_search_pkind)
    Spinner sp_invest_search_pkind;
    @ViewById(R.id.sp_invest_search_sttus)
    Spinner sp_invest_search_sttus;
    @ViewById(R.id.sp_invest_search_inqde)
    Spinner sp_invest_search_inqde;
    @ViewById(R.id.et_invest_search_pointname)
    EditText et_invest_search_pointname;
    @ViewById(R.id.btn_invest_search_search)
    Button btn_invest_search_search;
    @ViewById(R.id.mToolbar)
    Toolbar mToolbar;

    boolean isInstDESC = false;
    boolean isInvestDESC = false;

    String[] pkindCd;
    String[] sttusCd;
    String[] sortingCd;

    InvestResultListAdapter adapter;

    boolean isStd;

    protected final static int PAGE_COUNT = 10;
    int pageNum;

    @AfterViews
    protected void InitView() {
        isStd = getApplicationContext().getSharedPreferences(getString(R.string.classCheckPref), MODE_PRIVATE).getBoolean(getString(R.string.classCheckKey), true);

        Bundle bundle = getIntent().getExtras();
        bOnline = bundle.getBoolean(getString(R.string.key_bonline));
        mode = bundle.getString(getString(R.string.key_mode));
        mPointKindCode = bundle.getString(getString(R.string.key_point_kind_code));
        mAdmCode = bundle.getString(getString(R.string.key_adm_code));
        mStartPointName = bundle.getString(getString(R.string.key_start_point_name));
        mEndPointName = bundle.getString(getString(R.string.key_end_point_name));
        mStartDate = bundle.getString(getString(R.string.key_start_date));
        mEndDate = bundle.getString(getString(R.string.key_end_date));
        mInqStatus = bundle.getString(getString(R.string.key_inquire_status));
        mInqDeSorting = bundle.getString(getString(R.string.key_inquire_descent_sorting));
        mPointName = bundle.getString(getString(R.string.key_point_name));

        pageNum = 0;
        isInstDESC = false;
        isInvestDESC = false;
        items = new ArrayList<>();

        adapter = new InvestResultListAdapter(InvestResultActivity.this);
        adapter.setItems(items);
        adapter.setOnItemClickListener(new ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if(position >= 0) {
                    Intent intent;
                    String InqSttus = items.get(position).getInq_sttus();

                    if(InqSttus.equalsIgnoreCase("2")){
                        if(bOnline){
                            if (isStd){
                                intent = new Intent(InvestResultActivity.this, UploadWorkDetailActivity_.class);
                                intent.putExtra(getString(R.string.key_bonline), bOnline);
                                intent.putExtra("workSeq", (int) items.get(position).getSttusBfSn());
                                startActivity(intent);
                            } else {
                                intent = new Intent(InvestResultActivity.this, UploadWorkDetailNcpActivity_.class);
                                intent.putExtra(getString(R.string.key_bonline), bOnline);
                                intent.putExtra("workSeq", (int) items.get(position).getStdrspotSn());
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(InvestResultActivity.this, "오프라인 모드에서는 [조사완료 보기]는 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        if (isStd) {
                            intent = new Intent(InvestResultActivity.this, InvestDetailActivity_.class);
                            intent.putExtra(getString(R.string.key_bonline), bOnline);
                            intent.putExtra(getString(R.string.key_stdr_spot_sn), items.get(position).getStdrspotSn());
                            startActivity(intent);
                        } else {
                            intent = new Intent(InvestResultActivity.this, InvestDetailNcpActivity_.class);
                            intent.putExtra(getString(R.string.key_bonline), bOnline);
                            intent.putExtra(getString(R.string.key_npu_sn), items.get(position).getStdrspotSn()); //npu_sn 임.
                            startActivity(intent);
                        }
                    }
                }
            }

            @Override
            public void onLocationClick(int position, View v) {
                if(bOnline){
                    SugarRecord item;

                    if (isStd) {
                        item = RnsStdrspotUnity.FindByStdrspotSn(items.get(position).getStdrspotSn());
                    } else {
                        item = RnsNpUnity.FindByNpuSn(items.get(position).getStdrspotSn());
                    }

                    if (item != null) {
                        String type;
                        String fid;
                        long key;
                        Intent movePage = new Intent(InvestResultActivity.this, MapWebActivity.class);

                        if (isStd) {
                            type = getString(R.string.key_point_type_for_map_query);
                            key = ((RnsStdrspotUnity)item).getStdrspotSn();
                            fid = ((RnsStdrspotUnity)item).getBasePointNo();
                        } else {
                            type = ((RnsNpUnity)item).getPointsSecd().equals("0") ? getString(R.string.key_national_point_type_for_map_query) : getString(R.string.key_common_point_type_for_map_query);
                            key = ((RnsNpUnity)item).getNpuSn();
                            fid = String.valueOf(key);
                        }
                        movePage.putExtra(getString(R.string.key_type), type);
                        movePage.putExtra(getString(R.string.key_fid), fid);
                        movePage.putExtra(getString(R.string.key_mode), 1);
                        movePage.putExtra(getString(R.string.key_bonline), bOnline);
                        movePage.putExtra(getString(R.string.key_stdr_spot_sn), key);
                        movePage.putExtra(getString(R.string.key_npu_sn), key);
                        movePage.putExtra(getString(R.string.key_find_route), true);
                        startActivity(movePage);
                    }
                }else{
                    Toast.makeText(InvestResultActivity.this, "오프라인 모드에서는 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        LinearLayoutManager llm =  new LinearLayoutManager(InvestResultActivity.this);
        rv_invest_result.setLayoutManager(llm);
        rv_invest_result.setHasFixedSize(true);
        rv_invest_result.setAdapter(adapter);
        rv_invest_result.setItemAnimator(new DefaultItemAnimator());

        rv_invest_result.addOnScrollListener(new EndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                pageNum++;
                getItems();
            }
        });

        //기준점 종류
        sp_invest_search_pkind.setPrompt(getResources().getString(R.string.str_invest_search_promt));

        if (isStd) {
            layoutPointClass.setVisibility(View.GONE);
            pkindCd = getResources().getStringArray(R.array.pKind_array_cd);
            sp_invest_search_pkind.setAdapter(new ArrayAdapter<>(InvestResultActivity.this, android.R.layout.simple_spinner_dropdown_item,
                    new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.pKind_array)))));
        } else {
            layoutPointClass.setVisibility(View.VISIBLE);
            sp_invest_search_point_class.setAdapter(new ArrayAdapter<>(InvestResultActivity.this, android.R.layout.simple_spinner_dropdown_item,
                    new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.point_class_array)))));
            pkindCd = getResources().getStringArray(R.array.national_point_kind_array_code);
            sp_invest_search_pkind.setAdapter(new ArrayAdapter<>(InvestResultActivity.this, android.R.layout.simple_spinner_dropdown_item,
                    new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.national_point_kind_array)))));
        }

        //조사상태
        sttusCd = getResources().getStringArray(R.array.array_inq_sttus_cd);
        sp_invest_search_sttus.setPrompt(getResources().getString(R.string.str_invest_search_promt));
        sp_invest_search_sttus.setAdapter(new ArrayAdapter<>(InvestResultActivity.this, android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_mtr_sttus)))));

        //조사상태
        sortingCd = getResources().getStringArray(R.array.sorting_cd);
        sp_invest_search_inqde.setPrompt(getResources().getString(R.string.str_invest_search_promt));
        sp_invest_search_inqde.setAdapter(new ArrayAdapter<>(InvestResultActivity.this, android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.sorting)))));

        pageNum = 0;
        getCount();
        getItems();

        sp_invest_search_point_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sp_invest_search_pkind.setPrompt(getResources().getString(R.string.str_invest_search_promt));

                if (0 == position) {
                    pkindCd = getResources().getStringArray(R.array.national_point_kind_array_code);
                    sp_invest_search_pkind.setAdapter(new ArrayAdapter<>(InvestResultActivity.this, android.R.layout.simple_spinner_dropdown_item,
                            new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.national_point_kind_array)))));
                } else {
                    pkindCd = getResources().getStringArray(R.array.common_point_kind_array_code);
                    sp_invest_search_pkind.setAdapter(new ArrayAdapter<>(InvestResultActivity.this, android.R.layout.simple_spinner_dropdown_item,
                            new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.common_point_kind_array)))));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @UiThread
    public void getItems() {
        loadingDialog(true);
        String sql = "";

        if(isStd){
             sql = getSearchSql(mPointKindCode, mInqStatus, mAdmCode, mStartPointName, mEndPointName , mStartDate, mEndDate, mInqDeSorting, mPointName);
        } else {
            sql = getSearchSqlNcp(mPointKindCode, mInqStatus, mAdmCode, mStartPointName, mEndPointName , mStartDate, mEndDate, mInqDeSorting, mPointName);
        }

        Cursor cursor = SugarRecord.getSugarDataBase().rawQuery(sql, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            InvestResultRecord record = new InvestResultRecord(
                    cursor.getLong(0),
                    cursor.getLong(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
//                    cursor.getString(4).substring(0, 10),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getString(10)
            );
            items.add(record);
            cursor.moveToNext();
        }
        cursor.close();
        adapter.notifyDataSetChanged();
        loadingDialog(false);
    }
    @UiThread
    public void getCount() {
        String countSql;

        if (isStd) {
            countSql = getCountSqlSttus(mPointKindCode, mInqStatus, mAdmCode, mStartPointName, mEndPointName, mStartDate, mEndDate, mInqDeSorting, mPointName);
        } else {
            countSql = getCountSqlNcp(mPointKindCode, mInqStatus, mAdmCode, mStartPointName, mEndPointName, mStartDate, mEndDate, mInqDeSorting, mPointName);
        }

        Cursor cursor = SugarRecord.getSugarDataBase().rawQuery(countSql, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        String title = String.format("%s (총 %s건)", getResources().getString(R.string.title_activity_invest_result), count);
        tv_invest_result_title.setText(title);

        if(count < 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(InvestResultActivity.this);
            builder.setTitle("");
            //builder.setMessage("현황조사 자료가 없습니다.\n온라인 모드에서 현황조사 자료를 다운받으세요.").setCancelable(
            builder.setMessage("검색 된 자료가 없습니다.").setCancelable(
                    false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

    }
    @UiThread
    @Click(R.id.ll_invest_sort_install)
    public void sortInstall() {
        if (isInstDESC) {
            //ib_invest_sort_install.setBackgroundResource(R.drawable.img_list_arrow_up);
            isInstDESC = false;
            Collections.sort(items, new InstAscCompare());
        } else {
            //ib_invest_sort_install.setBackgroundResource(R.drawable.img_list_arrow_down);
            isInstDESC = true;
            Collections.sort(items, new InstDescCompare());
        }
        adapter.notifyDataSetChanged();
    }

    @UiThread
    @Click(R.id.ll_invest_sort_invest)
    public void sortInvest() {
        if (isInvestDESC) {
            //ib_invest_sort_invest.setBackgroundResource(R.drawable.img_list_arrow_up);
            isInvestDESC = false;
            Collections.sort(items, new InqSttusAscCompare());
        } else {
            //ib_invest_sort_invest.setBackgroundResource(R.drawable.img_list_arrow_down);
            isInvestDESC = true;
            Collections.sort(items, new InqSttusDescCompare());
        }
        adapter.notifyDataSetChanged();
    }
    @Click(R.id.btn_invest_search_search)
    public  void search() {
        pageNum = 0;
        items.clear();
        mPointKindCode = pkindCd[sp_invest_search_pkind.getSelectedItemPosition()];
        mInqStatus = sttusCd[sp_invest_search_sttus.getSelectedItemPosition()];
        mInqDeSorting = sortingCd[sp_invest_search_inqde.getSelectedItemPosition()];
        mPointName = et_invest_search_pointname.getText().toString();
        mDrawerLayout.closeDrawer(Gravity.END);
        KeyboardUtil.hideKeyboard(et_invest_search_pointname,InvestResultActivity.this);
        getItems();
        getCount();
    }

    @Click(R.id.ib_invest_result_nav_back)
    public void onBack() {
        Intent movePage = new Intent(InvestResultActivity.this, MainActivity.class);
        movePage.putExtra(getString(R.string.key_bonline), bOnline);
        movePage.putExtra(getString(R.string.key_mode), mode);
        startActivity(movePage);
//        super.onBackPressed();
    }

    //! @brief 기준점 상세조회 화면으로 이동
    @Click(R.id.layout_back_home)
    protected void onBackHome() {
        Intent movePage = new Intent(InvestResultActivity.this, MainActivity.class);
        movePage.putExtra(getString(R.string.key_bonline), bOnline);
        movePage.putExtra(getString(R.string.key_mode), mode);
        startActivity(movePage);
    }


    @Click(R.id.tv_invest_result_title)
    public void onTitleBack() {
        Intent movePage = new Intent(InvestResultActivity.this, MainActivity.class);
        movePage.putExtra(getString(R.string.key_bonline), bOnline);
        startActivity(movePage);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.END)) {
            mDrawerLayout.closeDrawer(Gravity.END);
        } else {
            super.onBackPressed();
        }

    }

    @Click(R.id.ib_invest_result_nav_home)
    public void onHome() {
        mDrawerLayout.openDrawer(Gravity.END);
        /*
        Intent movePage = new Intent(InvestResultActivity.this, MainActivity.class);
        movePage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(movePage);
        finish();
        */
    }

    @UiThread
    public void loadingDialog(boolean view) {
        if (view)
            FineDialog.showProgress(InvestResultActivity.this, false, getResources().getString(R.string.str_invest_loading));
        else
            FineDialog.hideProgress();
    }

    public String getSearchSql(String pointsKndCd,
                               String inqSttus,
                               String admCd,
                               String sPointsNm,
                               String ePointsNm,
                               String sInqDe,
                               String eInqDe,
                               String inqDeSorting,
                               String pointsNm
    ) {
        //설치일자 ---> 조사일자 변경으로 INQ_DE 변경
        String sql = "SELECT " +
                "A.sttus_bf_sn, " +
                "B.stdrspot_sn, " +
                "B.points_knd_cd, " +
                "B.points_nm, " +
                "B.mtr_sttus_cd, " +
                "B.inq_de, " +           //"B.instl_de, " +
                "ifnull(A.inq_sttus, 0) as inq_sttus, " +
                "B.points_x, " +
                "B.points_y, " +
                "A.mtr_sttus_cd, " +
                "A.inq_de " +
                "FROM RNS_STDRSPOT_UNITY B LEFT JOIN RNS_STTUS_INQ_BEFORE A " +
                "ON B.stdrspot_sn = A.stdrspot_sn " +
                "WHERE 1 = 1 " +
                "%s %s %s;";

        String where = "";
        if (!pointsKndCd.isEmpty()) {
            where = where + String.format("AND B.points_knd_cd  = %s ", pointsKndCd);
        }
        if (!inqSttus.isEmpty()) {
            if (inqSttus.equals("0")) {
                where = where + String.format("AND A.stdrspot_sn IS NULL");
            } else {
                where = where + String.format("AND A.inq_sttus  = %s ", inqSttus);
            }
        }
        if (!admCd.isEmpty()) {
            where = where + String.format("AND B.adm_cd like \'%s%%\'  ", admCd);
        }
        if (!sPointsNm.isEmpty() && !ePointsNm.isEmpty()) {
            where = where + String.format("AND B.points_nm BETWEEN %s AND %s ", sPointsNm, ePointsNm);
        } else if (ePointsNm.isEmpty() && !sPointsNm.isEmpty()) {
            where = where + String.format("AND B.points_nm >= %s ", sPointsNm);
        } else if (sPointsNm.isEmpty() && !ePointsNm.isEmpty()) {
            where = where + String.format("AND B.points_nm <= %s ", ePointsNm);
        }
        if (!pointsNm.isEmpty()) {
            where = where + String.format("AND B.points_nm like \'%s%%\'  ", pointsNm);
        }
        if (!sInqDe.isEmpty() && !eInqDe.isEmpty()) {
            where = where + String.format("AND B.inq_de BETWEEN \'%s\' AND \'%s\'", sInqDe, eInqDe);
        } else if (eInqDe.isEmpty() && !sInqDe.isEmpty()) {
            where = where + String.format("AND B.inq_de >= \'%s\'", sInqDe);
        } else if (sInqDe.isEmpty() && !eInqDe.isEmpty()) {
            where = where + String.format("AND B.inq_de <= \'%s\'", eInqDe);
        }
        String orderby = "ORDER BY ";
        if(inqDeSorting.isEmpty()) {
            orderby = orderby + "CAST(B.points_no as INTEGER), B.points_nm , B.stdrspot_sn, B.points_knd_cd";
        } else if (inqDeSorting.equalsIgnoreCase("ASC")){
            orderby = orderby + "B.instl_de ASC";
        } else {
            orderby = orderby + "B.instl_de DESC";
        }
        String limit = String.format("LIMIT %s OFFSET %s", PAGE_COUNT, pageNum * PAGE_COUNT);
        return String.format(sql, where, orderby, limit);
    }
    public String getSearchSqlNcp(String pointsKndCd,
                               String inqSttus,
                               String admCd,
                               String sPointsNm,
                               String ePointsNm,
                               String sInqDe,
                               String eInqDe,
                               String inqDeSorting,
                               String pointsNm
    ) {
        //설치일자 ---> 조사일자 변경으로 INQ_DE 변경

        String sql =  " SELECT C.NPSI_BF_SN, A.npu_sn, A.points_knd_cd, A.points_nm, A.mtr_cd, A.inq_de,    "+
                        "       ifnull(c.inq_sttus,'0') as inq_sttus,  B.points_x, B.points_y , C.mtr_sttus_cd, C.inq_de  "+
                        "FROM RNS_NP_UNITY A    "+
                        "LEFT OUTER JOIN  RNS_NP_RSLT B ON A.NPU_SN = B.NPU_SN  "+
                        "LEFT OUTER JOIN RNS_NP_STTUS_INQ_BF C ON A.NPU_SN = C.NPU_SN   "+
                        "WHERE 1 = 1 " +
                     "%s %s %s;";
        String where = "";
        if (!pointsKndCd.isEmpty()) {
            where = where + String.format("AND A.points_knd_cd  = %s ", pointsKndCd);
        }
        if (!inqSttus.isEmpty()) {
            where = where + String.format("AND C.inq_sttus  = %s ", inqSttus);
        }
        if (!admCd.isEmpty()) {
            where = where + String.format("AND A.adm_cd like \'%s%%\'  ", admCd);
        }
        if (!sPointsNm.isEmpty() && !ePointsNm.isEmpty()) {
            where = where + String.format("AND A.points_nm BETWEEN %s AND %s ", sPointsNm, ePointsNm);
        } else if (ePointsNm.isEmpty() && !sPointsNm.isEmpty()) {
            where = where + String.format("AND A.points_nm >= %s ", sPointsNm);
        } else if (sPointsNm.isEmpty() && !ePointsNm.isEmpty()) {
            where = where + String.format("AND A.points_nm <= %s ", ePointsNm);
        }
        if (!pointsNm.isEmpty()) {
            where = where + String.format("AND A.points_nm like \'%s%%\'  ", pointsNm);
        }
        if (!sInqDe.isEmpty() && !eInqDe.isEmpty()) {
            where = where + String.format("AND A.inq_de BETWEEN \'%s\' AND \'%s\'", sInqDe, eInqDe);
        } else if (eInqDe.isEmpty() && !sInqDe.isEmpty()) {
            where = where + String.format("AND A.inq_de >= \'%s\'", sInqDe);
        } else if (sInqDe.isEmpty() && !eInqDe.isEmpty()) {
            where = where + String.format("AND A.inq_de <= \'%s\'", eInqDe);
        }
        String orderby = "ORDER BY ";
        if(inqDeSorting.isEmpty()) {
            orderby = orderby + "CAST(A.npu_sn as INTEGER), A.points_nm , A.npu_sn, A.points_knd_cd";
        } else if (inqDeSorting.equalsIgnoreCase("ASC")){
            orderby = orderby + "A.inq_de ASC";
        } else {
            orderby = orderby + "A.inq_de DESC";
        }
        String limit = String.format("LIMIT %s OFFSET %s", PAGE_COUNT, pageNum * PAGE_COUNT);
        return String.format(sql, where, orderby, limit);
    }
    public String getCountSqlSttus(String pointsKndCd, String inqSttus, String admCd, String sPointsNm, String ePointsNm, String sInqDe, String eInqDe, String inqDeSorting,String pointNm) {
        String sql = "SELECT " +
                "count(*) " +
                "FROM RNS_STDRSPOT_UNITY B LEFT JOIN RNS_STTUS_INQ_BEFORE A " +
                "ON B.stdrspot_sn = A.stdrspot_sn " +
                "WHERE 1 = 1 " +
                "%s " +
                "%s;";

        String where = "";
        if (!pointsKndCd.isEmpty()) {
            where = where + String.format("AND B.points_knd_cd  = %s ", pointsKndCd);
        }
        if (!inqSttus.isEmpty()) {
            if (inqSttus.equals("0")) {
                where = where + String.format("AND A.stdrspot_sn IS NULL");
            } else {
                where = where + String.format("AND A.inq_sttus  = %s ", inqSttus);
            }
        }
        if (!admCd.isEmpty()) {
            where = where + String.format("AND B.adm_cd like \'%s%%\'  ", admCd);
        }
        if (!sPointsNm.isEmpty() && !ePointsNm.isEmpty()) {
            where = where + String.format("AND B.points_nm BETWEEN %s AND %s ", sPointsNm, ePointsNm);
        } else if (ePointsNm.isEmpty() && !sPointsNm.isEmpty()) {
            where = where + String.format("AND B.points_nm >= %s ", sPointsNm);
        } else if (sPointsNm.isEmpty() && !ePointsNm.isEmpty()) {
            where = where + String.format("AND B.points_nm <= %s ", ePointsNm);
        }
        if (!pointNm.isEmpty()) {
            where = where + String.format("AND B.points_nm like \'%s%%\'  ", pointNm);
        }
        if (!sInqDe.isEmpty() && !eInqDe.isEmpty()) {
            where = where + String.format("AND B.inq_de BETWEEN \'%s\' AND \'%s\'", sInqDe, eInqDe);
        } else if (eInqDe.isEmpty() && !sInqDe.isEmpty()) {
            where = where + String.format("AND B.inq_de >= \'%s\'", sInqDe);
        } else if (sInqDe.isEmpty() && !eInqDe.isEmpty()) {
            where = where + String.format("AND B.inq_de <= \'%s\'", eInqDe);
        }
        String orderby = "ORDER BY ";
        if(inqDeSorting.isEmpty()) {
            orderby = orderby + "CAST(B.points_no as INTEGER) ASC";
        } else if (inqDeSorting.equalsIgnoreCase("ASC")){
            orderby = orderby + "B.instl_de ASC, B.stdrspot_sn ASC";
        } else {
            orderby = orderby + "B.instl_de DESC, B.stdrspot_sn ASC";
        }

        return String.format(sql, where, orderby);
    }
    public String getCountSqlNcp(String pointsKndCd, String inqSttus, String admCd, String sPointsNm, String ePointsNm, String sInqDe, String eInqDe, String inqDeSorting,String pointNm) {
        String sql = " SELECT COUNT(*)   "+
                "FROM RNS_NP_UNITY A    "+
                "LEFT OUTER JOIN  RNS_NP_RSLT B ON A.NPU_SN = B.NPU_SN  "+
                "LEFT OUTER JOIN RNS_NP_STTUS_INQ_BF C ON A.NPU_SN = C.NPU_SN   "+
                "WHERE 1 = 1 " +
                "%s " +
                "%s;";

        String where = "";
        if (!pointsKndCd.isEmpty()) {
            where = where + String.format("AND A.points_knd_cd  = %s ", pointsKndCd);
        }
        if (!inqSttus.isEmpty()) {
            where = where + String.format("AND C.inq_sttus  = %s ", inqSttus);
        }
        if (!admCd.isEmpty()) {
            where = where + String.format("AND A.adm_cd like \'%s%%\'  ", admCd);
        }
        if (!sPointsNm.isEmpty() && !ePointsNm.isEmpty()) {
            where = where + String.format("AND A.points_nm BETWEEN %s AND %s ", sPointsNm, ePointsNm);
        } else if (ePointsNm.isEmpty() && !sPointsNm.isEmpty()) {
            where = where + String.format("AND A.points_nm >= %s ", sPointsNm);
        } else if (sPointsNm.isEmpty() && !ePointsNm.isEmpty()) {
            where = where + String.format("AND A.points_nm <= %s ", ePointsNm);
        }
        if (!pointNm.isEmpty()) {
            where = where + String.format("AND A.points_nm like \'%s%%\'  ", pointNm);
        }
        if (!sInqDe.isEmpty() && !eInqDe.isEmpty()) {
            where = where + String.format("AND A.inq_de BETWEEN \'%s\' AND \'%s\'", sInqDe, eInqDe);
        } else if (eInqDe.isEmpty() && !sInqDe.isEmpty()) {
            where = where + String.format("AND A.inq_de >= \'%s\'", sInqDe);
        } else if (sInqDe.isEmpty() && !eInqDe.isEmpty()) {
            where = where + String.format("AND A.inq_de <= \'%s\'", eInqDe);
        }
        String orderby = "ORDER BY ";
        if(inqDeSorting.isEmpty()) {
            orderby = orderby + "CAST(A.npu_sn as INTEGER) ASC";
        } else if (inqDeSorting.equalsIgnoreCase("ASC")){
            orderby = orderby + "A.instl_de ASC, A.npu_sn ASC";
        } else {
            orderby = orderby + "A.instl_de DESC, A.npu_sn ASC";
        }

        return String.format(sql, where, orderby);
    }

    //오름차순(ASC)
    static class InstAscCompare implements Comparator<InvestResultRecord> {
        @Override
        public int compare(InvestResultRecord arg0, InvestResultRecord arg1) {
            // TODO Auto-generated method stub
            return arg0.getInqDe().compareTo(arg1.getInqDe());
        }
    }

    //내림차순(DESC)
    static class InstDescCompare implements Comparator<InvestResultRecord> {
        @Override
        public int compare(InvestResultRecord arg0, InvestResultRecord arg1) {
            // TODO Auto-generated method stub
            return arg1.getInqDe().compareTo(arg0.getInqDe());
        }
    }

    //오름차순(ASC)
    static class InqSttusAscCompare implements Comparator<InvestResultRecord> {
        @Override
        public int compare(InvestResultRecord arg0, InvestResultRecord arg1) {
            // TODO Auto-generated method stub
            return arg0.getInq_sttus().compareTo(arg1.getInq_sttus());
        }
    }

    //내림차순(DESC)
    static class InqSttusDescCompare implements Comparator<InvestResultRecord> {
        @Override
        public int compare(InvestResultRecord arg0, InvestResultRecord arg1) {
            // TODO Auto-generated method stub
            return arg1.getInq_sttus().compareTo(arg0.getInq_sttus());
        }
    }
}



