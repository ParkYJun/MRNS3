package com.mrns.invest.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrns.invest.innerDetail.history.investHistoryListNcpActivity;
import com.mrns.invest.innerDetail.install.InstallHistoryListNcpActivity_;
import com.mrns.invest.record.RnsNpUnity;
import com.mrns.invest.util.DBUtil;
import com.mrns.invest.util.InsertUtil;
import com.mrns.invest.view.fragment.InvestNcpDetailFragment1_;
import com.mrns.invest.view.fragment.InvestNcpDetailFragment2_;
import com.mrns.invest.view.fragment.InvestNcpDetailFragment3_;
import com.mrns.invest.view.fragment.InvestNcpDetailFragment4_;
import com.mrns.main.R;
import com.mrns.map.MapWebActivity;

import com.neokree.materialtabs.MaterialTab;
import com.neokree.materialtabs.MaterialTabHost;
import com.neokree.materialtabs.MaterialTabListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;


@EActivity(R.layout.activity_invest_detail)
public class InvestDetailNcpActivity extends AppCompatActivity {
    boolean isGeo;
    boolean bOnline;
    long npuSn;

    InsertUtil mInsertUtil;

    ViewPagerAdapter pagerAdapter;

    @ViewById(R.id.ib_invest_detail_nav_back)
    ImageView ib_invest_detail_nav_back;
    @ViewById(R.id.tv_invest_detail_title)
    TextView tv_invest_detail_title;
    @ViewById(R.id.tab_invest_detail)
    MaterialTabHost tab_invest_detail;
    @ViewById(R.id.pager_invest_detail)
    ViewPager pager_invest_detail;


    @ViewById(R.id.layout_invest)
    LinearLayout layout_invest;
    @ViewById(R.id.btn_invest_detail_input)
    ImageView btn_invest_detail_input;
    @ViewById(R.id.btn_invest_detail_history)
    ImageView btn_invest_detail_history;
    @ViewById(R.id.btn_invest_detail_install)
    ImageView btn_invest_detail_install;
    @ViewById(R.id.btn_invest_detail_gis)
    ImageView btn_invest_detail_gis;
    @ViewById(R.id.btn_invest_detail_point)
    ImageView btn_invest_detail_point;

    @ViewById(R.id.btn_invest_detail_input_txt)
    TextView btn_invest_detail_input_txt;
    @ViewById(R.id.btn_invest_detail_install_txt)
    TextView btn_invest_detail_install_txt;
    @ViewById(R.id.btn_invest_detail_gis_txt)
    TextView btn_invest_detail_gis_txt;
    @ViewById(R.id.btn_invest_detail_history_txt)
    TextView btn_invest_detail_history_txt;

    String title;
    String pointNm;
    String url;
    RnsNpUnity item;

    @AfterViews
    protected void InitView() {
        bOnline = getIntent().getBooleanExtra(getString(R.string.key_bonline), false);
        npuSn = getIntent().getLongExtra(getString(R.string.key_npu_sn), 0);
        isGeo = getIntent().getBooleanExtra(getString(R.string.key_is_geo), false);
        mInsertUtil = new InsertUtil(getApplicationContext(), npuSn, getResources().getInteger(R.integer.index_insert_others), bOnline);
        if(isGeo) {
            tv_invest_detail_title.setText(getIntent().getStringExtra(getString(R.string.key_title)));
            layout_invest.setVisibility(View.GONE);
        } else {
            setTitle(RnsNpUnity.FindByNpuSn(npuSn));
        }

        if(!bOnline){
            btn_invest_detail_history.setAlpha(70);
            btn_invest_detail_install.setAlpha(70);
            btn_invest_detail_gis.setAlpha(70);
            btn_invest_detail_history_txt.setTextColor(Color.GRAY);
            btn_invest_detail_install_txt.setTextColor(Color.GRAY);
            btn_invest_detail_gis_txt.setTextColor(Color.GRAY);
        }

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager_invest_detail.setAdapter(pagerAdapter);
        pager_invest_detail.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tab_invest_detail.setSelectedNavigationItem(position);
            }
        });
        //탭 글자색
        tab_invest_detail.setTextColor(Color.WHITE);

        //탭 배경색
        tab_invest_detail.setPrimaryColor(Color.rgb(58,188,99));
        tab_invest_detail.setAccentColor(Color.YELLOW);
        MaterialTabHost tabHost;
        tabHost = (MaterialTabHost) this.findViewById(R.id.tab_invest_detail);
        //탭 추가
        for (int i=0; i < pagerAdapter.getCount(); i++) {
            MaterialTab tab = tab_invest_detail.newTab();
            tab.setText(pagerAdapter.getPageTitle(i));
            tab.setTabListener(new ProductTabListener());
            tab_invest_detail.addTab(tab);
        }
        //처음 선택될 탭 지정
        tab_invest_detail.setSelectedNavigationItem(0);
    }
    void setTitle(RnsNpUnity item) {
        if(item != null) {
            this.item = item;
            pointNm = item.getPointsNm() != null ? item.getPointsNm() : "";
            String pointknd = item.getPointsKndCd() != null ? DBUtil.getCmmnCode("100", item.getPointsKndCd()) : "";
            title = String.format("%s(%s)", pointNm, pointknd);
            tv_invest_detail_title.setText(title);
        }
    }
/*
    @Click(R.id.ib_invest_detail_nav_back)
    void OnBack() {
        startActivity(mInsertUtil.getResultIntent(true));
    }
*/

    //! @brief 기준점 상세조회 화면으로 이동
    @Click(R.id.layout_back_home)
    void OnBack() {
        startActivity(mInsertUtil.getResultIntent(true));
    }

    @Click(R.id.btn_invest_detail_input)
    void OnDetailInput() {
        startActivity(mInsertUtil.getMoveIntentNcp(R.id.imgbtn_mtr_info));
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
    }
    @Click(R.id.btn_invest_detail_install)
    void OnDetailInstaill() {
        Log.d("DEBUG", "설치연혁");
        if(bOnline){
            Intent intent = new Intent(getApplicationContext(), InstallHistoryListNcpActivity_.class);
            intent.putExtra(getString(R.string.key_bonline), bOnline);
            intent.putExtra(getString(R.string.key_npu_sn), npuSn);
            intent.putExtra(getString(R.string.key_title), title);
            startActivity(intent);
        }else{
            Toast.makeText(InvestDetailNcpActivity.this, "오프라인 모드에서는 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
        }

    }
    @Click(R.id.btn_invest_detail_history)
    void OnDetailHistory() {
        Log.d("DEBUG", "현황이력 조회");
        if(bOnline){
            Intent intent = new Intent(InvestDetailNcpActivity.this, investHistoryListNcpActivity.class);
            intent.putExtra(getString(R.string.key_bonline), bOnline);
            intent.putExtra(getString(R.string.key_npu_sn), npuSn);
            intent.putExtra(getString(R.string.key_title), title);
            startActivity(intent);
        }else{
            Toast.makeText(InvestDetailNcpActivity.this, "오프라인 모드에서는 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
        }

    }
    @Click(R.id.btn_invest_detail_gis)
    void OnDetailGis() {
        if(bOnline){
            long npuSn = item.getNpuSn();
            Intent movePage = new Intent(InvestDetailNcpActivity.this, MapWebActivity.class);
            movePage.putExtra(getString(R.string.key_type), item.getPointsSecd().equals("0") ? getString(R.string.key_national_point_type_for_map_query) : getString(R.string.key_common_point_type_for_map_query));
            movePage.putExtra(getString(R.string.key_fid),  String.valueOf(npuSn));
            movePage.putExtra(getString(R.string.key_mode), 1);
            movePage.putExtra(getString(R.string.key_bonline), bOnline);
            movePage.putExtra(getString(R.string.key_npu_sn), npuSn);
            startActivity(movePage);
        }else {
            Toast.makeText(InvestDetailNcpActivity.this, "오프라인 모드에서는 지원하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    @Click(R.id.btn_invest_detail_point)
    void OnDetailPoint() {
        startActivity(mInsertUtil.getResultIntent(true));
    }


    /**
     * 뷰페이저 어댑터를 정의합니다.
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        public Fragment getItem(int index) {
            Fragment frag = null;
            boolean bInCase = false;

            switch (index) {
                case 0:
                    frag = InvestNcpDetailFragment1_.builder().build();
                    bInCase = true;
                    break;
                case 1:
                    frag = InvestNcpDetailFragment2_.builder().build();
                    bInCase = true;
                    break;
                case 2:
                    frag = InvestNcpDetailFragment3_.builder().build();
                    bInCase = true;
                    break;
                case 3:
                    frag = InvestNcpDetailFragment4_.builder().build();
                    bInCase = true;
                    break;
            }

            if (bInCase) {
                Bundle bundle = new Bundle();
                bundle.putLong(getString(R.string.key_npu_sn), npuSn);
                bundle.putBoolean(getString(R.string.key_is_geo), isGeo);
                frag.setArguments(bundle);
            }

            return frag;
        }
        @Override
        public int getCount() {
            return 4;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "현황조사";
                case 1: return "기본";
                case 2: return "성과";
                case 3: return "방위표/보조점";
                default: return null;
            }
        }
    }

    /**
     * 탭을 선택했을 때 처리할 리스너 정의
     */
    private class ProductTabListener implements MaterialTabListener {
        public ProductTabListener() {}
        @Override
        public void onTabSelected(MaterialTab tab) {
            pager_invest_detail.setCurrentItem(tab.getPosition());
        }
        @Override
        public void onTabReselected(MaterialTab tab) {}

        @Override
        public void onTabUnselected(MaterialTab tab) {}
    }
}
