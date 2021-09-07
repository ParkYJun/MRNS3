package com.mrns.map;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mrns.common.NetcheckMoniter;
import com.mrns.invest.record.CodeRecordList;
import com.mrns.invest.util.JsonPaserUtil;
import com.mrns.main.MainActivity;
import com.mrns.main.R;
import com.mrns.map.fragment.BuildSearchFragment;
import com.mrns.map.fragment.DoroSearchFragment;
import com.mrns.map.fragment.JibunSearchFragment;
import com.mrns.map.fragment.PointSearchFragment;
import com.mrns.utils.BaseTabActivity;
import com.neokree.materialtabs.MaterialTab;
import com.neokree.materialtabs.MaterialTabHost;
import com.neokree.materialtabs.MaterialTabListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

@EActivity(R.layout.activity_map_search)
public class MapSearchActivity extends BaseTabActivity {
    private boolean bOnline;
    @ViewById(R.id.tabhost)
    MaterialTabHost mTabhost;
    @ViewById(R.id.pager)
    ViewPager mapPager;
    @ViewById(R.id.spinner_sido)
    Spinner spinnerSido;
    @ViewById(R.id.spinner_sig)
    Spinner spinnerSig;

    private CodeRecordList mSidoList;
    private CodeRecordList mSigList;

    ViewPagerUploadAdapter pagerSearchAdapter;
    TextView upload_title;
    private Intent movePage;
    ImageView navHome;
    private NetcheckMoniter networkMoniter = null;

    @AfterViews
    protected void InitView() {
        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        mTabhost = (MaterialTabHost) this.findViewById(R.id.tabhost);
        mapPager = (ViewPager) this.findViewById(R.id.pager);

        //ViewPage create
        pagerSearchAdapter = new ViewPagerUploadAdapter(getSupportFragmentManager());
        mapPager.setAdapter(pagerSearchAdapter);
        mapPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabhost.setSelectedNavigationItem(position);

            }
        });

        int tabColor = getResources().getColor(R.color.mapSearchTabColor);
        mTabhost.setTextColor(Color.WHITE);
        mTabhost.setPrimaryColor(tabColor);

        for(int i=0; i<pagerSearchAdapter.getCount(); i++) {
            MaterialTab tab = mTabhost.newTab();
            tab.setText(pagerSearchAdapter.getPageTitle(i));
            tab.setTabListener(new uploadTabListener());

            mTabhost.addTab(tab);
        }

        mTabhost.setSelectedNavigationItem(0);

        //뒤로가기 버튼
        upload_title = (TextView) findViewById(R.id.titleTextView);
        navHome = (ImageView) findViewById(R.id.btn_nav_back);

        ViewPager.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.titleTextView :
                        finish();
                        break;
                    case R.id.btn_nav_back :
                        movePage = new Intent(MapSearchActivity.this, MainActivity.class);
                        movePage.putExtra(getString(R.string.key_bonline), true);
                        startActivity(movePage);
                        break;
                }//end switch
            }//end Onclick
        };

        upload_title.setOnClickListener(listener);
        navHome.setOnClickListener(listener);

        //시도 리스트 get & 스피너 set
        getSidoList();

        //선택 이벤트 등록
        spinnerSido.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSigList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try
        {
            unregisterReceiver(networkMoniter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 탭을 선택했을 때 처리할 리스너 정의
     */
    private class uploadTabListener implements MaterialTabListener {

        public uploadTabListener() {
        }

        @Override
        public void onTabSelected(MaterialTab tab) {
            mapPager.setCurrentItem(tab.getPosition());

        }

        @Override
        public void onTabReselected(MaterialTab tab) {
        }

        @Override
        public void onTabUnselected(MaterialTab tab) {

        }
    }

    @UiThread
    public void getSidoList() {
        String url = getString(R.string.server_ip) + getString(R.string.url_get_sido_list);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, url, sidoSuccessListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> map = new HashMap<>();
                return map;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }

    @UiThread
    public void setSigList() {
        int selSidoIndex = spinnerSido.getSelectedItemPosition();
        final String sidoCode = mSidoList.getCodeList().get(selSidoIndex);
        String url = getString(R.string.server_ip) + getString(R.string.url_get_sig_list);
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        Request request = new StringRequest(Request.Method.POST, url, sigSuccessListener, failListener)
        {
            @Override
            public Map<String, String> getParams() throws AuthFailureError
            {
                HashMap<String, String> map = new HashMap<>();
                map.put("sidoCode", sidoCode);
                return map;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(getResources().getInteger(R.integer.socket_timeout), DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        queue.add(request);
    }


    //공통 Error 리스너
    private Response.ErrorListener failListener = new Response.ErrorListener()
    {
        @Override
        public void onErrorResponse(VolleyError e)
        {
            Toast.makeText(getApplicationContext(), getString(R.string.message_error_server_trans), Toast.LENGTH_SHORT).show();
        }
    };

    //시도 successListener
    private Response.Listener<String> sidoSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            mSidoList = JsonPaserUtil.getCodeRecordList(response);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MapSearchActivity.this, android.R.layout.simple_spinner_dropdown_item, mSidoList.getCodeValueList());
            spinnerSido.setAdapter(adapter);
            //시군구 리스트 get & 스피너 set
            setSigList();
        }
    };

    //시군구 successListener
    private Response.Listener<String> sigSuccessListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            mSigList = JsonPaserUtil.getCodeRecordList(response);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MapSearchActivity.this, android.R.layout.simple_spinner_dropdown_item, mSigList.getCodeValueList());
            spinnerSig.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    };

    public String getSelectedSigCode() {
        int selIndex = spinnerSig.getSelectedItemPosition();

        if (selIndex == -1) {
            return null;
        } else {
            return mSigList.getCodeList().get(spinnerSig.getSelectedItemPosition());
        }
    }

    /**
     * 뷰페이저 어댑터를 정의합니다.
     */
    private class ViewPagerUploadAdapter extends FragmentStatePagerAdapter {
        public ViewPagerUploadAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int index) {
            Fragment frag = null;

            switch (index) {
                case 0:
                    frag = new PointSearchFragment();
                    break;
                case 1:
                    frag = new JibunSearchFragment();
                    break;
                case 2:
                    frag = new BuildSearchFragment();
                    break;
                case 3:
                    frag = new DoroSearchFragment();
                    break;
            }

            return frag;
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "기준점";
                case 1:
                    return "지번";
                case 2:
                    return "건물명";
                case 3:
                    return "도로명주소";
                default:
                    return null;
            }

        }

    }//end ViewPagerDownAdapter











}
