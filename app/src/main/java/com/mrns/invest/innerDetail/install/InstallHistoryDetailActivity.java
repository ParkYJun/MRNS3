package com.mrns.invest.innerDetail.install;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrns.common.NetcheckMoniter;

import com.neokree.materialtabs.MaterialTab;
import com.neokree.materialtabs.MaterialTabHost;
import com.neokree.materialtabs.MaterialTabListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(com.mrns.main.R.layout.activity_install_history_detail)
public class InstallHistoryDetailActivity extends AppCompatActivity {
    boolean bOnline;

    @Extra("instl_ih_sn")
    long instl_ih_sn;
    String title;

    ViewPagerAdapter pagerAdapter;

    @ViewById(com.mrns.main.R.id.ib_invest_nav_back)
    ImageView ib_invest_nav_back;

    @ViewById(com.mrns.main.R.id.tab_invest_install_detail)
    MaterialTabHost tab_invest_install_detail;

    @ViewById(com.mrns.main.R.id.pager_invest_install_detail)
    ViewPager pager_invest_install_detail;

    @ViewById(com.mrns.main.R.id.titleTextView)
    TextView titleTextView;

    @ViewById(com.mrns.main.R.id.titleTextViewSub)
    TextView titleTextViewSub;

    private NetcheckMoniter networkMoniter = null;

    @AfterViews
    protected void InitView() {
        bOnline = getIntent().getBooleanExtra(getString(com.mrns.main.R.string.key_bonline), false);
        getIntent().getStringExtra(getString(com.mrns.main.R.string.key_title));
        //RnsStdrspotUnity item = RnsStdrspotUnity.FindByStdrspotSn(stdrspotSn);

        //Network 상태 리시버 등록
        networkMoniter = new NetcheckMoniter(this);
        registerReceiver(networkMoniter, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager_invest_install_detail.setAdapter(pagerAdapter);
        pager_invest_install_detail.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tab_invest_install_detail.setSelectedNavigationItem(position);
            }
        });
        //탭 글자색
        tab_invest_install_detail.setTextColor(Color.WHITE);

        //탭 배경색
        tab_invest_install_detail.setPrimaryColor(Color.rgb(58,188,99));
        tab_invest_install_detail.setAccentColor(Color.YELLOW);

        //탭 추가
        for (int i=0; i < pagerAdapter.getCount(); i++) {
            MaterialTab tab = tab_invest_install_detail.newTab();
            tab.setText(pagerAdapter.getPageTitle(i));
            tab.setTabListener(new ProductTabListener());

            tab_invest_install_detail.addTab(tab);
        }
        //처음 선택될 탭 지정
        tab_invest_install_detail.setSelectedNavigationItem(0);
        titleTextViewSub.setText(title);

    }

    @Click(com.mrns.main.R.id.ib_invest_nav_back)
    void OnBack() {
        super.onBackPressed();
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
            if (index == 0) {
                frag = HistoryDetailFragment1_.builder()
                        .instl_ih_sn(instl_ih_sn)
                        .build();
            } else if (index == 1) {
                frag = HistoryDetailFragment2_.builder()
                        .instl_ih_sn(instl_ih_sn)
                        .build();
            } else if (index == 2) {
                frag = HistoryDetailFragment3_.builder()
                        .instl_ih_sn(instl_ih_sn)
                        .build();
            }
            return frag;
        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "기본";
                case 1: return "관측/기지";
                case 2: return "좌표";
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
            pager_invest_install_detail.setCurrentItem(tab.getPosition());
        }
        @Override
        public void onTabReselected(MaterialTab tab) {}

        @Override
        public void onTabUnselected(MaterialTab tab) {}
    }
}
