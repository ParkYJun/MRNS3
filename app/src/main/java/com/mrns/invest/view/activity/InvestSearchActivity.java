package com.mrns.invest.view.activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mrns.common.AppController;
import com.mrns.common.Common;
import com.mrns.entity.StdrSpot.InvestListEntity;
import com.mrns.invest.record.TlSccoCtprv;
import com.mrns.invest.record.TlSccoSig;
import com.mrns.invest.util.InsertUtil;
import com.mrns.main.R;
import com.mrns.request.GsonRequest;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;


@EActivity(R.layout.activity_invest_search)
public class InvestSearchActivity extends AppCompatActivity {

    boolean bOnline;

    ArrayList<TlSccoCtprv> sidoList;
    ArrayList<TlSccoSig> sigunguList;
    ArrayList<String> sigunguStringList;

    ArrayAdapter<String> sigunguAdapter;
    String[] pkindCd;


    @ViewById(R.id.ib_invest_search_nav_back)
    ImageButton ib_invest_search_nav_back;

    @ViewById(R.id.sp_invest_search_sido)
    Spinner sp_invest_search_sido;
    @ViewById(R.id.sp_invest_search_sigungu)
    Spinner sp_invest_search_sigungu;
    @ViewById(R.id.sp_invest_search_pkind)
    Spinner sp_invest_search_pkind;

    @ViewById(R.id.et_invest_sdate)
    EditText et_invest_sdate;
    @ViewById(R.id.et_invest_edate)
    EditText et_invest_edate;

    @ViewById(R.id.et_invest_spname)
    EditText et_invest_spname;
    @ViewById(R.id.et_invest_epname)
    EditText et_invest_epname;

    @ViewById(R.id.et_invest_search_pointname)
    EditText et_invest_search_pointname;

    @ViewById(R.id.btn_invest_search_search)
    Button btn_invest_search_search;



    @AfterViews
    protected void InitView() {
        bOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), false);
        //시도
        sidoList = TlSccoCtprv.getResArray(InvestSearchActivity.this);
        sp_invest_search_sido.setAdapter(new ArrayAdapter<>(InvestSearchActivity.this, android.R.layout.simple_spinner_dropdown_item,
                TlSccoCtprv.ToStringArrayList(InvestSearchActivity.this)));
        sp_invest_search_sido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sigunguList = TlSccoSig.getResArray(InvestSearchActivity.this, sidoList.get(position).getCtprvnCd());
                TlSccoSig.ToStringArrayList(InvestSearchActivity.this, sidoList.get(position).getCtprvnCd(), sigunguStringList);
                sigunguAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //시군구
        sigunguList = TlSccoSig.getResArray(InvestSearchActivity.this, sidoList.get(0).getCtprvnCd());
        sigunguStringList = new ArrayList<>();
        TlSccoSig.ToStringArrayList(InvestSearchActivity.this, sidoList.get(0).getCtprvnCd(),sigunguStringList);
        sigunguAdapter = new ArrayAdapter<>(InvestSearchActivity.this, android.R.layout.simple_spinner_dropdown_item, sigunguStringList);
        sp_invest_search_sigungu.setAdapter(sigunguAdapter);


        //기준점 종류
        pkindCd = getResources().getStringArray(R.array.pKind_array_cd);
        sp_invest_search_pkind.setPrompt(getResources().getString(R.string.str_invest_search_promt));
        sp_invest_search_pkind.setAdapter(new ArrayAdapter<>(InvestSearchActivity.this, android.R.layout.simple_spinner_dropdown_item,
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.pKind_array)))));

        RequestRecommendPOST(Common.URL_INVEST_LIST);
    }

    @Click(R.id.et_invest_sdate)
    public void onSDateClack() {
        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH);
        int day  = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(InvestSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String msg = String.format("%d.%d.%d", year, monthOfYear+1, dayOfMonth);
                et_invest_sdate.setText(msg);
            }
        }, year, month, day).show();
    }

    @Click(R.id.et_invest_edate)
    public void onEDateClack() {
        GregorianCalendar calendar = new GregorianCalendar();
        int year = calendar.get(Calendar.YEAR);
        int month= calendar.get(Calendar.MONTH);
        int day  = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(InvestSearchActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String msg = String.format("%d.%d.%d", year, monthOfYear+1, dayOfMonth);
                et_invest_edate.setText(msg);
            }
        }, year, month, day).show();
    }

    @Click(R.id.btn_invest_search_search)
    public void onSearch() {
        String admCd = sigunguList.get(sp_invest_search_sigungu.getSelectedItemPosition()).getSigCd();
        String pointsKndCd = pkindCd[sp_invest_search_pkind.getSelectedItemPosition()];
        String sdate = et_invest_sdate.getText().toString().replace(".", "-");
        String edate = et_invest_edate.getText().toString().replace(".", "-");
        String spname = et_invest_spname.getText().toString();
        String epname = et_invest_epname.getText().toString();
        String pointNm = et_invest_search_pointname.getText().toString();

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(InvestSearchActivity.this);
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(getString(R.string.key_adm_code), admCd);
        editor.putString(getString(R.string.key_point_kind_code), pointsKndCd);
        editor.putString(getString(R.string.key_start_date), sdate);
        editor.putString(getString(R.string.key_end_date), edate);
        editor.putString(getString(R.string.key_start_point_name), spname);
        editor.putString(getString(R.string.key_end_point_name), epname);
        editor.putString(getString(R.string.key_point_name), pointNm);
        editor.apply();

        startActivity(new InsertUtil(getApplicationContext(), bOnline).getResultIntent(false));
    }

    @Click(R.id.ib_invest_search_nav_back)
    public void onBack() {
        super.onBackPressed();
    }

    //test connection
    public void RequestRecommendPOST(final String URL) {
        GsonRequest<InvestListEntity> investReq =
                new GsonRequest<InvestListEntity>(Request.Method.POST,URL,InvestListEntity.class,
                        null,SuccessRecomment(),ErrorRecommend() ) {
            @Override
            protected void deliverResponse(InvestListEntity response) {
                super.deliverResponse(response);
            }
        };
        AppController.getInstance().addToRequestQueue(investReq);
    }

    private Response.Listener<InvestListEntity> SuccessRecomment() {
        return new Response.Listener<InvestListEntity>() {
            @Override
            public void onResponse(InvestListEntity response) {
                Log.d("eined","success");
            }
        };
    }


    private Response.ErrorListener ErrorRecommend() {
        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("eined","error parsing");
            }
        };
    }
    
    



}
